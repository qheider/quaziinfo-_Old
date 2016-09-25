/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.witchcraft.jbpm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.StaleStateException;
import org.jboss.seam.async.AbstractDispatcher;
import org.jboss.seam.bpm.ManagedJbpmContext;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.contexts.Lifecycle;
import org.jboss.seam.transaction.Transaction;
import org.jboss.seam.transaction.UserTransaction;
import org.jbpm.JbpmContext;
import org.jbpm.db.JobSession;
import org.jbpm.job.Job;
import org.jbpm.persistence.JbpmPersistenceException;


/**
 * This is a rework of org.jbpm.job.executor.JobExecutorServlet to integrate it into Seam
 * It's a naive code port - there are probably better and more proper ways to do it.
 *
 * See jbpm-3.2.2 for original sources
 *
 * @author Marko Strukelj <strukelj@parsek.net>
 */

public class JobExecutorThread extends Thread {

    public JobExecutorThread(String name,
                             JobExecutor jobExecutor,
                             int idleInterval,
                             int maxIdleInterval,
                             long maxLockTime,
                             int maxHistory
    ) {
        super(name);
        this.jobExecutor = jobExecutor;
        this.idleInterval = idleInterval;
        this.maxIdleInterval = maxIdleInterval;
        this.maxLockTime = maxLockTime;
        this.maxHistory = maxHistory;
    }

    JobExecutor jobExecutor;
    int idleInterval;
    int maxIdleInterval;
    long maxLockTime;
    int maxHistory;

    Collection history = new ArrayList();
    int currentIdleInterval;
    boolean isActive = true;

    public void run() {
        try {
            currentIdleInterval = idleInterval;
            while (isActive) {

                boolean createContexts = !Contexts.isEventContextActive() && !Contexts.isApplicationContextActive();
                if (createContexts) Lifecycle.beginCall();
                Contexts.getEventContext().set(AbstractDispatcher.EXECUTING_ASYNCHRONOUS_CALL, true);
                sleep(2000);
                UserTransaction ut;
                try {
                    ut = Transaction.instance();
                    ut.begin();

                    Collection acquiredJobs = null;
                    boolean isCallCompleted = false;
                    try {
                        acquiredJobs = acquireJobs();
                        isCallCompleted = true;
                        ut.commit();

                    } finally {
                        if (!isCallCompleted)
                            ut.rollback();
                    }

                    if (!acquiredJobs.isEmpty()) {
                        Iterator iter = acquiredJobs.iterator();
                        while (iter.hasNext() && isActive) {
                            Job job = (Job) iter.next();
                            ut.begin();
                            isCallCompleted = false;
                            try {

                                executeJob(job);
                                isActive = false;
                                isCallCompleted = true;
                                ut.commit();
                                

                            } finally {
                                if (!isCallCompleted)
                                    ut.rollback();
                            }

                        }

                    } else { // no jobs acquired
                        if (isActive) {

                            ut.begin();
                            isCallCompleted = false;
                            long waitPeriod = 0;
                            try {

                                waitPeriod = getWaitPeriod();
                                isCallCompleted = true;
                                ut.commit();

                            } finally {
                                if (!isCallCompleted)
                                    ut.rollback();
                            }

                            if (waitPeriod > 0) {
                                synchronized (jobExecutor) {
                                    jobExecutor.wait(waitPeriod);
                                }
                            }

                        }
                    }

                    // no exception so resetting the currentIdleInterval
                    currentIdleInterval = idleInterval;

                } catch (InterruptedException e) {
                    log.info((isActive ? "active" : "inactivated") + " job executor thread '" + getName() + "' got interrupted");

                } catch (Exception e) {
                    log.error("exception in job executor thread. waiting " + currentIdleInterval + " milliseconds", e);

                    try {
                        synchronized (jobExecutor) {
                            jobExecutor.wait(currentIdleInterval);
                        }
                    } catch (InterruptedException e2) {
                        log.debug("delay after exception got interrupted", e2);
                    }
                    // after an exception, the current idle interval is doubled to prevent
                    // continuous exception generation when e.g. the db is unreachable
                    currentIdleInterval = currentIdleInterval * 2;

                } finally {
                    Contexts.getEventContext().remove(AbstractDispatcher.EXECUTING_ASYNCHRONOUS_CALL);
                    if (createContexts) Lifecycle.endCall();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            log.info(getName() + " leaves cyberspace");
            jobExecutor.startThread();
        }
    }

    protected Collection acquireJobs() {
        Collection acquiredJobs = null;
        synchronized (jobExecutor) {
            Collection jobsToLock = new ArrayList();
            log.debug("acquiring jobs for execution...");

            JbpmContext jbpmContext = ManagedJbpmContext.instance();
            
            try {
                try {
                    JobSession jobSession = jbpmContext.getJobSession();
                    log.debug("querying for acquirable job...");
                    Job job = jobSession.getFirstAcquirableJob(getName());
                    if (job != null) {
                        if (job.isExclusive()) {
                            log.debug("exclusive acquirable job found (" + job + "). querying for other exclusive jobs to lock them all in one tx...");
                            List otherExclusiveJobs = jobSession.findExclusiveJobs(getName(), job.getProcessInstance());
                            jobsToLock.addAll(otherExclusiveJobs);
                            log.debug("trying to obtain a process-instance exclusive locks for '" + otherExclusiveJobs + "'");
                        } else {
                        	//jobSession.find
                            log.debug("trying to obtain a lock for '" + job + "'");
                            jobsToLock.add(job);
                        }

                        Iterator iter = jobsToLock.iterator();
                        while (iter.hasNext()) {
                            job = (Job) iter.next();
                            job.setLockOwner(getName());
                            job.setLockTime(new Date());
                            jbpmContext.getSession().update(job);
                        }

                        // HACKY HACK : this is a workaround for a hibernate problem that is fixed in hibernate 3.2.1
                        if (job instanceof org.jbpm.job.Timer) {
                            Hibernate.initialize(((org.jbpm.job.Timer) job).getGraphElement());
                        }

                    } else {
                        log.debug("no acquirable jobs in job table");
                    }

                } finally {
                    //jbpmContext.close();
                }
                acquiredJobs = jobsToLock;
                log.debug("obtained locks on following jobs: " + acquiredJobs);

            } catch (StaleStateException e) {
                log.debug("couldn't acquire lock on job(s): " + jobsToLock);
            }
        }
        return acquiredJobs;
    }

    protected void executeJob(Job job) {
        JbpmContext jbpmContext = ManagedJbpmContext.instance();
        try {
            JobSession jobSession = jbpmContext.getJobSession();
            job = jobSession.loadJob(job.getId());

            try {
                log.debug("executing job " + job );
               
                if (job.execute(jbpmContext)) {
                	
                    // TODO: This is a HACK - somewhere job already gets deleted - can't find where
                    // another delete here causes it to blow up
                    // jobSession.deleteJob(job);
                }else{
                	//js
                	log.warn("job failed to execute " + job);
                }

            } catch (Exception e) {
                log.debug("exception while executing '" + job + "'", e);
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                job.setException(sw.toString());
                job.setRetries(job.getRetries() - 1);
            }

            // The following code marks tx for rollback, but I changed tx demarcation so now it
            // always tries this right before commit - which makes no sense
            // Also tx are not container managed but UserManaged - which makes setRollbackOnly invalid

            // if this job is locked too long
            //long totalLockTimeInMillis = System.currentTimeMillis() - job.getLockTime().getTime();
            //if (totalLockTimeInMillis > maxLockTime) {
            //    jbpmContext.setRollbackOnly();
            //}

        } finally {
            try {
                //jbpmContext.close();
            } catch (JbpmPersistenceException e) {
                // if this is a stale object exception, the jbpm configuration has control over the logging
                if ("org.hibernate.StaleObjectStateException".equals(e.getCause().getClass().getName())) {
                    log.info("problem committing job execution transaction: optimistic locking failed");
                   // StaleObjectLogConfigurer..error("problem committing job execution transaction: optimistic locking failed", e);
                } else {
                    log.error("problem committing job execution transaction", e);
                }
            } catch (RuntimeException e) {
                log.error("problem committing job execution transaction", e);

                throw e;
            }
        }
    }


    protected Date getNextDueDate() {
        Date nextDueDate = null;
        JbpmContext jbpmContext = ManagedJbpmContext.instance();
        try {
            JobSession jobSession = jbpmContext.getJobSession();
            Collection jobIdsToIgnore = jobExecutor.getMonitoredJobIds();
            Job job = jobSession.getFirstDueJob(getName(), jobIdsToIgnore);
            if (job != null) {
                nextDueDate = job.getDueDate();
                jobExecutor.addMonitoredJobId(getName(), job.getId());
            }
        } finally {
            //jbpmContext.close();
        }
        return nextDueDate;
    }

    protected long getWaitPeriod() {
        long interval = currentIdleInterval;
        Date nextDueDate = getNextDueDate();
        if (nextDueDate != null) {
            long currentTimeMillis = System.currentTimeMillis();
            long nextDueDateTime = nextDueDate.getTime();
            if (nextDueDateTime < currentTimeMillis + currentIdleInterval) {
                interval = nextDueDateTime - currentTimeMillis;
            }
        }
        if (interval < 0) {
            interval = 0;
        }
        return interval;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    private static Log log = LogFactory.getLog(JobExecutorThread.class);
}

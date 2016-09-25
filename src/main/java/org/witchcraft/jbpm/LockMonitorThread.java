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

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.bpm.ManagedJbpmContext;
import org.jbpm.JbpmContext;
import org.jbpm.db.JobSession;
import org.jbpm.job.Job;

/**
 * This is a rework of org.jbpm.job.executor.JobExecutorServlet to integrate it into Seam
 * It's a naive code port - there are probably better and more proper ways to do it.
 *
 * See jbpm-3.2.2 for original sources
 *
 * @author Marko Strukelj <strukelj@parsek.net>
 */

public class LockMonitorThread extends Thread {

  int lockMonitorInterval;
  int maxLockTime;
  int lockBufferTime;

  boolean isActive = true;

  public LockMonitorThread(int lockMonitorInterval, int maxLockTime, int lockBufferTime) {
    this.lockMonitorInterval = lockMonitorInterval;
    this.maxLockTime = maxLockTime;
    this.lockBufferTime = lockBufferTime;
  }

  public void run() {
    try {
      while (isActive) {
        try {
          unlockOverdueJobs();
          if ( (isActive)
               && (lockMonitorInterval>0)
             ) {
            sleep(lockMonitorInterval);
          }
        } catch (InterruptedException e) {
          log.info("lock monitor thread '"+getName()+"' got interrupted");
        } catch (Exception e) {
          log.error("exception in lock monitor thread. waiting "+lockMonitorInterval+" milliseconds", e);
          try {
            sleep(lockMonitorInterval);
          } catch (InterruptedException e2) {
            log.debug("delay after exception got interrupted", e2);
          }
        }
      }
    } catch (Throwable t) {
      t.printStackTrace();
    } finally {
      log.info(getName()+" leaves cyberspace");
    }
  }


  protected void unlockOverdueJobs() {
    JbpmContext jbpmContext = ManagedJbpmContext.instance();
    try {
      JobSession jobSession = jbpmContext.getJobSession();

      Date treshold = new Date(System.currentTimeMillis()-maxLockTime-lockBufferTime);
      List jobsWithOverdueLockTime = jobSession.findJobsWithOverdueLockTime(treshold);
      Iterator iter = jobsWithOverdueLockTime.iterator();
      while (iter.hasNext()) {
        Job job = (Job) iter.next();
        // unlock
        log.debug("unlocking "+job+ " owned by thread "+job.getLockOwner());
        job.setLockOwner(null);
        job.setLockTime(null);
        jobSession.saveJob(job);
      }

    } finally {
      try {
        //jbpmContext.close();
      } catch (RuntimeException e) {
        log.error("problem committing job execution transaction", e);
        throw e;
      }
    }
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  private static Log log = LogFactory.getLog(LockMonitorThread.class);
}

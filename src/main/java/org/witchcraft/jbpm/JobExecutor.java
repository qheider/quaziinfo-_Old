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

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;

/**
 * This is a rework of org.jbpm.job.executor.JobExecutorServlet to integrate it
 * into Seam It's a naive code port - there are probably better and more proper
 * ways to do it.
 * 
 * See jbpm-3.2.2 for original sources
 * 
 * @author Marko Strukelj <strukelj@parsek.net>
 */

//@Name("jbpm.JobExecutor")
//@Scope(ScopeType.APPLICATION)
//@Startup(depends = "org.jboss.seam.bpm.jbpm")
public class JobExecutor implements Serializable {

	private static final long serialVersionUID = 1L;

	String name = "JBPM Job Executor";
	int nbrOfThreads = 2;
	int idleInterval = 1000;
	int maxIdleInterval = 10000;
	int historyMaxSize = 10000;

	int maxLockTime = 120000;
	int lockMonitorInterval = 60000;
	int lockBufferTime = 10000;

	Map threads = new HashMap();
	LockMonitorThread lockMonitorThread;
	Map monitoredJobIds = Collections.synchronizedMap(new HashMap());

	boolean isStarted = false;

//	@Create      //only component class can have @Create annotation 
	public synchronized void start() {
		if (!isStarted) {
			log.debug("starting thread group '" + name + "'...");
			for (int i = 0; i < nbrOfThreads; i++) {
				startThread();
			}
			isStarted = true;
		} else {
			log.debug("ignoring start: thread group '" + name
					+ "' is already started'");
		}

		lockMonitorThread = new LockMonitorThread(lockMonitorInterval,
				maxLockTime, lockBufferTime);
	}

	/**
	 * signals to all threads in this job executor to stop. It may be that
	 * threads are in the middle of something and they will finish that firts.
	 * Use {@link #stopAndJoin()} in case you want a method that blocks until
	 * all the threads are actually finished.
	 * 
	 * @return a list of all the stopped threads. In case no threads were
	 *         stopped an empty list will be returned.
	 */
	@Destroy
	public synchronized List stop() {
		List stoppedThreads = new ArrayList(threads.size());
		if (isStarted) {
			log.debug("stopping thread group '" + name + "'...");
			for (int i = 0; i < nbrOfThreads; i++) {
				stoppedThreads.add(stopThread());
			}
			isStarted = false;
		} else {
			log.debug("ignoring stop: thread group '" + name + "' not started");
		}
		return stoppedThreads;
	}

	public void stopAndJoin() throws InterruptedException {
		Iterator iter = stop().iterator();
		while (iter.hasNext()) {
			Thread thread = (Thread) iter.next();
			thread.join();
		}
	}

	public synchronized void startThread() {
		String threadName = getNextThreadName();
		Thread thread = new JobExecutorThread(threadName, this, idleInterval,
				maxIdleInterval, maxLockTime, historyMaxSize);
		threads.put(threadName, thread);
		log.debug("starting new job executor thread '" + threadName + "'");
		thread.start();
	}

	protected String getNextThreadName() {
		return getThreadName(threads.size() + 1);
	}

	protected String getLastThreadName() {
		return getThreadName(threads.size());
	}

	private String getThreadName(int index) {
		return name + ":" + getHostName() + ":" + index;
	}

	private String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			return "unknown";
		}
	}

	protected synchronized Thread stopThread() {
		String threadName = getLastThreadName();
		JobExecutorThread thread = (JobExecutorThread) threads
				.remove(threadName);
		log.debug("removing job executor thread '" + threadName + "'");
		thread.setActive(false);
		thread.interrupt();
		return thread;
	}

	public Set getMonitoredJobIds() {
		return new HashSet(monitoredJobIds.values());
	}

	public void addMonitoredJobId(String threadName, long jobId) {
		monitoredJobIds.put(threadName, new Long(jobId));
	}

	public void removeMonitoredJobId(String threadName) {
		monitoredJobIds.remove(threadName);
	}

	public int getHistoryMaxSize() {
		return historyMaxSize;
	}

	public int getIdleInterval() {
		return idleInterval;
	}

	public boolean isStarted() {
		return isStarted;
	}

	public int getMaxIdleInterval() {
		return maxIdleInterval;
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return nbrOfThreads;
	}

	public Map getThreads() {
		return threads;
	}

	public int getMaxLockTime() {
		return maxLockTime;
	}

	public int getLockBufferTime() {
		return lockBufferTime;
	}

	public int getLockMonitorInterval() {
		return lockMonitorInterval;
	}

	public int getNbrOfThreads() {
		return nbrOfThreads;
	}

	private static Log log = LogFactory.getLog(JobExecutor.class);
}

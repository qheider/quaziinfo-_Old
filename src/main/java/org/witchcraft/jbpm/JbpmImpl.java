package org.witchcraft.jbpm;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.bpm.BusinessProcessInterceptor;
import org.jboss.seam.core.Init;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jbpm.job.executor.JobExecutor;

/**
 * Seam provides no mechanism for initialising the scheduler used for timers and
 * reminders. This class extends the Jbpm component to initialise the scheduler
 * at startup.
 * 
 * <p>
 * This class should be configured in your components xml instead of the regular
 * jbpm configuration. An example of this is:
 * </p>
 * 
 * <xmp> 
 * <component name="org.jboss.seam.bpm.jbpm" 
 *          class="nz.co.softwarefactory.risingstars.seam.Jbpm"> 
 *      <property name="pageflow-definitions">
 *          <value>create_registration_details.jpdl.xml</value> 
 *      </property> 
 *      <property name="process-definitions">
 *          <value>processes/reporting.jpdl.xml</value> 
 *      </property> 
 *      <property name="schedulerEnabled">true</property> 
 * </component> 
 * </xmp>
 * 
 * <p>
 * In addition to this the following is also needed in your components.xml:
 * </p>
 * 
 * <xmp> <bpm:jbpm name="notused"></bpm:jbpm> </xmp>
 * 
 * This is needed because seam looks for this element at startup to determine if
 * the {@link BusinessProcessInterceptor} should be installed.
 * 
 * @see http 
 *      ://seamframework.org/Documentation/JBPMDeploymentInProductionEnvironments
 * 
 * @author craig
 */
@Scope(ScopeType.APPLICATION)
@BypassInterceptors
@Startup
@Name("org.jboss.seam.bpm.jbpm")
@Install(value = false, precedence = Install.APPLICATION)
@Transactional
public class JbpmImpl extends org.jboss.seam.bpm.Jbpm {
	// private final Log log = Logging.getLog(JbpmImpl.class);
	private static final Log log = Logging.getLog(JbpmImpl.class);
	private static final String PI_TARGET = "JbpmExtensions";
	private boolean schedulerEnabled = false;

	/**
	 * Returns where the jbpm scheduler is enabled.
	 * @return
	 */
	public boolean isSchedulerEnabled() {
		return schedulerEnabled;
	}

	public void setSchedulerEnabled(boolean schedulerEnabled) {
		this.schedulerEnabled = schedulerEnabled;
	}

	/**
	 * Prevents the default jbpm component from installing all processes. 
	 */
	@Override
	protected boolean isProcessDeploymentEnabled() {
		return false;
	}

	/**
	 * Overrides the default component to use conditional deployment.
	 */
	@Override
	public void startup() throws Exception {
		super.startup();

		// work around to let Seam know jbpm is actually installed.
		Init.instance().setJbpmInstalled(true);


		if (isSchedulerEnabled()) {

			log.info("Starting the jBPM scheduler");

			startScheduler();

			if (isRunning()) {
				log.info("jBPM scheduler has started.");
			} else {
				log.error("jBPM scheduler was not started.");
			}

		}

	}

	private String resource;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}


	/**
	 * Returns the jbpm job executor.
	 */
	public JobExecutor getJobExecutor() {
		return getJbpmConfiguration().getJobExecutor();
	}

	/**
	 * Starts the jbpm scheduler
	 */
	private void startScheduler() {
		JobExecutor jobExecutor = getJobExecutor();
		if (jobExecutor != null) {
			jobExecutor.start();
		}
	}

	/**
	 * Stops the jbpm scheduler.
	 */
	private void stopScheduler() {
		JobExecutor jobExecutor = getJobExecutor();
		if (jobExecutor != null) {
			try {
				jobExecutor.stopAndJoin();
			} catch (InterruptedException e) {
				log.warn("Could not wait for job executor.", e);
			}
		}
	}

	/**
	 * Returns true if the jbpm scheduler is running.
	 * @return
	 */
	private boolean isRunning() {
		return getJobExecutor() != null && getJobExecutor().isStarted();
	}

	/**
	 * Overridden to stop the jbpm scheduler if its running.
	 */
	@Override
	public void shutdown() {
		if (isRunning()) {
			log.info("Stopping the jBPM scheduler.");
			stopScheduler();
		} else if (isSchedulerEnabled()) {
			log.debug("jBPM Scheduler can't be stopped because it was not running.");
		}
		super.shutdown();
	}

}
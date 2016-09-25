package com.nrs.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.bpm.BusinessProcess;
import org.jboss.seam.log.Log;
import org.jbpm.JbpmContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

@Name("processLoader")
public class ProcessLoader {
	private String processId;
	private String taskId;
	
	@Logger
	Log log;
	@In
	protected JbpmContext jbpmContext;
	
	@In BusinessProcess businessProcess;
	
	private DataModel tasksDataModel;
	
	public DataModel getTasksDataModel() {
		return tasksDataModel;
	}

	public void setTasksDataModel(DataModel dm) {
		this.tasksDataModel = dm;
	}
	
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	private List<TaskInstance> getProcessAllTasks()
	{
		org.jbpm.graph.exe.ProcessInstance  processInstance = org.jboss.seam.bpm.ProcessInstance.instance();
		System.out.println("processInstance: " + processInstance);
		List<TaskInstance> tasks = new ArrayList<TaskInstance>();
		try {
			@SuppressWarnings("rawtypes")
			Collection<TaskInstance> c = processInstance.getTaskMgmtInstance().getTaskInstances();
			for (TaskInstance o:c)
			{
				tasks.add((TaskInstance)o);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return tasks;
	}
	
	public void resumeProcess(long processId)
	{
		businessProcess.setProcessId(processId);
		
		tasksDataModel = new ListDataModel(getProcessAllTasks());
		
	}
	
	public void resumeTask(long taskId)
	{
		businessProcess.setTaskId(taskId);
	}
	
	public List<TaskInstance> getActiveTasks() {
		List<TaskInstance> tasks = new ArrayList<TaskInstance>();
		ProcessInstance pi = jbpmContext.getProcessInstance(Long.parseLong(processId));

		Collection<TaskInstance> tasksCollection = pi.getTaskMgmtInstance().getTaskInstances();
		for (TaskInstance task : tasksCollection) {
			if (task.isOpen() || task.isSuspended()) {
				tasks.add(task);
			}
		}
		return tasks;
	}
	
	
	public List<TaskInstance> getAllActiveTasksByProcessId(Long historicProcessId) {
		List<TaskInstance> tasks = new ArrayList<TaskInstance>();
		ProcessInstance pi = jbpmContext.getProcessInstance(historicProcessId);

		Collection<TaskInstance> tasksCollection = pi.getTaskMgmtInstance().getTaskInstances();
		for (TaskInstance task : tasksCollection) {
			if (task.isOpen() || task.isSignalling()) {
				tasks.add(task);
			}
		}
		return tasks;
	}

}

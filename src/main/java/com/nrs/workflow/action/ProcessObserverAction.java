package com.nrs.workflow.action;

import java.util.Collection;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;

//import com.nas.recovery.domain.realestate.PropertyStatus;
//import com.nas.recovery.web.action.loan.LoanDataAction;
//import com.nas.recovery.web.action.realestate.RealEstateInfoAction;

@Name("processObserver")
public class ProcessObserverAction {

	@Logger Log log;
	@In
	protected JbpmContext jbpmContext;
//	@In(create = true, value = "loanDataAction")
//	protected LoanDataAction loanDataAction;
//	
//	@Transactional
//	@Observer("endPropertyListedWhenSold")
//	public void executePropertyListedWhenSold() {
//		log.error("receive event from complete 'checkConditionsWaived' realEstateInfoAction.updateStatus('SOLD_FIRM'), endPropertyListedWhenSold");
//
//		RealEstateInfoAction realEstateInfoAction = (RealEstateInfoAction) Component
//				.getInstance("realEstateInfoAction");
//		ExecutionContext executionContext = ExecutionContext
//				.currentExecutionContext();
//		
//		
//		Node nodeState = executionContext.getProcessDefinition().getNode(
//				"propertyListed"); // when hiddenNode is Workflow State. check
//									// BaseJbpmProcessAction for hiddenNode as
//									// taskNode
//		
//		List<Token> tokens = executionContext.getProcessInstance()
//				.getRootToken().getChildrenAtNode(nodeState);
//		
//		if (realEstateInfoAction.getInstance().getStatus()
//				.equals(PropertyStatus.SOLD_FIRM)) {
//			if (!tokens.isEmpty()) {
//				//tokens.get(0).signal();
//				
//				Token token = tokens.get(0); // 'propertyListed' node token
//				List<TaskInstance> tasks = this.getTasksForToken(token.getId());
//				
//				if (tasks.size() > 0)
//				{	
//					TaskInstance t = tasks.get(0);
//					log.error("endPropertyListedWhenSold event auto complete taskId #0", t.getId());
//					if (t.isOpen())
//					{
//						t.start();
//						t.end();
//					}
//				}
////				token.signal();
//				
//			}
//		}
//	}
//	
//	@Transactional
//	@Observer("notRelistWhenSold")
//	public void statusResponse2() {
//		log.error("receive event from complete 'checkConditionsWaived' realEstateInfoAction.updateStatus('SOLD_FIRM'), notRelistWhenSold");
//
//		if (!loanDataAction.checkFork("relist",
//				ExecutionContext.currentExecutionContext())) {
//			RealEstateInfoAction realEstateInfoAction = (RealEstateInfoAction) Component
//					.getInstance("realEstateInfoAction");
//			Collection<TaskInstance> c = jbpmContext
//					.getProcessInstance(
//							loanDataAction.getInstance().getProcessId())
//					.getTaskMgmtInstance().getTaskInstances();
//			// ExecutionContext executionContext= (ExecutionContext)
//			// Component.getInstance("executionContext");
//			// executionContext.currentExecutionContext().getTaskMgmtInstance().getTaskInstances();
//
//			
//			for (TaskInstance taskInstance : c) {
//				if (taskInstance.getName().equalsIgnoreCase("relistProperty")
//						&& taskInstance.isOpen()) {
//					if (realEstateInfoAction.getInstance().getStatus()
//							.equals(PropertyStatus.SOLD_FIRM)) {
//						
//						log.error("notRelistWhenSold event auto complete taskId #0", taskInstance.getId());
//						
//						taskInstance.start();
//						taskInstance.end("notRelist");
////						taskInstance.getToken().signal("notRelist");
//						//taskInstance.setEnd(new Date());
//
//						break;
//
//					}
//				}
//
//			}
//		}// if condition closed
//	}
//
//
//	
//	private List<TaskInstance> getTasksForToken(Long tkId) {
//		try {
//			List<TaskInstance> taskInstancesByToken = jbpmContext
//					.getTaskMgmtSession().findTaskInstancesByToken(tkId);
//
//			return taskInstancesByToken;
//
//		} catch (Exception e) {
//			log.error("Error getting tasks for token id {0}", e, tkId);
//
//			return null;
//
//		}
//	}
//	
//	
//	/* *****************************************************
//	 * Observing updateRealtorStatus that has been raised
//	 * realEstateInfoAction.java
//	 * 
//	 * *******************************************************
//	 */
////	 @Transactional
////	 @Observer("updateRealtorStatus")
////	 public void statusResponse() {
////	
////	 RealEstateInfoAction realEstateInfoAction = (RealEstateInfoAction)
////	 Component.getInstance("realEstateInfoAction");
////	 Collection<TaskInstance> c = jbpmContext.getProcessInstance(loanDataAction.getInstance().getProcessId()).getTaskMgmtInstance().getTaskInstances();
////	 i = c.size();
////	 for (TaskInstance taskInstance : c) {
////	 if (i <= 0) {
////	 break;
////	 }
////	 i--;
////	
////	 if (taskInstance.getName().equalsIgnoreCase("hiddenNode") &&
////	 !taskInstance.hasEnded()) {
////	 if
////	 (realEstateInfoAction.getInstance().getStatus().equals(PropertyStatus.SOLD_FIRM))
////	 {
////	 taskInstance.getToken().signal();
////	 taskInstance.setEnd(new Date());
////	 break;
////	 }
////	 }
////	
////	
////	 }
////	 }
//	 
//		@Observer("signalRealtorHiddenNode")
//		public void observingSignalVacantModule() {
//			RealEstateInfoAction realEstateInfoAction = (RealEstateInfoAction) Component.getInstance("realEstateInfoAction");
//			ExecutionContext executionContext = ExecutionContext.currentExecutionContext();
//			Node nodeState = executionContext.getProcessDefinition().getNode("hiddenNode");
//			List<Token> tokens = executionContext.getProcessInstance().getRootToken().getChildrenAtNode(nodeState);
//			if(realEstateInfoAction.getInstance().getStatus().equals(PropertyStatus.SOLD_FIRM))
//			 {
//			if (!tokens.isEmpty()) {
//				tokens.get(0).signal();
//			}
//			 }
//		}
//	
	
	
}

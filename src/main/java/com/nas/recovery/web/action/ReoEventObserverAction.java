 
package com.nas.recovery.web.action;

/**
 *
 * @author QUAZI
 * LenderAction.java **** Quebec workflow
 * AlbertaEventObserverAction.java **** Alberta workflow
 * NewBrunswickEventObserverAction.java ****  NewBrunswick workflow

 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.datamodel.DataModel;
 

 
import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManager;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Events;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.witchcraft.base.entity.BusinessEntity;
import org.witchcraft.seam.action.BaseAction;

//@Scope(ScopeType.CONVERSATION)
@Name("reoEventObserverAction")
public class ReoEventObserverAction extends BaseAction<BusinessEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
      
	//LenderAction  lenderAction=(LenderAction) Component.getInstance("lenderAction");
	//LoanDataAction loanDataAction=(LoanDataAction) Component.getInstance("loanDataAction");
	
	@In(create=true,required=false) 
	JbpmContext jbpmContext;
	
	
	 
	/**************************************************************************************
	 * @Raise Event
     * @REOWorkflo
     * @swimlane:Lender 
     * @task: appraisalReceived
     * @transition:complete
     */
	
	//public void raiseEventAndAddVariable(String var,String event){
	//	loanDataAction.updateFork(var);	
	//	lenderAction.createCustomEvent(event);
	//}
	
	 
	
	 
 
	

	
//	public void closeAllHiddenNodes(){
//		lenderAction.createCustomEvent("executeHiddenNode6Final");
//		lenderAction.createCustomEvent("executeHiddenNode4Final");
//		lenderAction.createCustomEvent("signalRFPJudgmentHiddenNodeToFinalClose");
//		lenderAction.createCustomEvent("signalHiddenNodeUtdToFinalClose");
//		lenderAction.createCustomEvent("signalhiddenNode7NbFinal");
//		
//	}
	
	
	
     
	/**************************************************************************************
	 * @Observing Event
     * @REOWorkflo
     * @swimlane:Lender 
     * @task: appraisalReceived
     * @transition:complete
      
    @Observer("valuationReceived")
	public void observingValuationReceived() {
		ExecutionContext executionContext = ExecutionContext .currentExecutionContext();
		Node nodeState = executionContext.getProcessDefinition().getNode("waitState1");
		List<Token> tokens = executionContext.getProcessInstance().getRootToken().getChildrenAtNode(nodeState);
		if (!tokens.isEmpty()) {
			tokens.get(0).signal("gotoReviewValuation");
		}

	}
	*/
      
}
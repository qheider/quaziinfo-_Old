package com.nrs.workflow.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ExecutionContext;
 

/**
 * the holder of actions called from workflow definition
 * naming convention: taskName+transition or taskName+event
 * @author jack
 *
 */
@Name("processAction")
public class ProcessAction {
//
//	@In LenderAction lenderAction;
//	@In LoanDataAction loanDataAction;
//	
//	@Logger Log log;
//	
//	public void reviewForListingProceedWithValuations()
//	{
//		setProcessVariable("fromReviewForListingProceedWithValuations",true);
//		lenderAction.createCustomEvent("fullCMAHit");
//	}
//	
//	public void orderAppraisalAssigned()
//	{
//		log.debug("fromReviewForListingProceedWithValuations: #0", getProcessBooleanvariable("fromReviewForListingProceedWithValuations"));
//		try {
//			if (!getProcessBooleanvariable("fromReviewForListingProceedWithValuations"))
//				loanDataAction.updateDueDate("legal","getMortgageSaleDate",-15);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public void reviewClaimsAssigned()
//	{
//		log.debug("reviewClaimsAssigned");
//		loanDataAction.updateLenderClaimModuleDueDate(45);
//	}
//	
//	public void setProcessVariable(String name,Object value)
//	{
//		ContextInstance contextInstance = (ContextInstance) ExecutionContext
//				.currentExecutionContext().getProcessInstance()
//				.getInstance(ContextInstance.class);
//		contextInstance.setVariable(name,value);
//		
//	}
//	
//	public Object getProcessVariable(String name)
//	{
//		ContextInstance contextInstance = (ContextInstance) ExecutionContext
//				.currentExecutionContext().getProcessInstance()
//				.getInstance(ContextInstance.class);
//		return contextInstance.getVariable(name);
//	}
//	
//	public boolean getProcessBooleanvariable(String name)
//	{
//		return (Boolean)getProcessVariable(name) == null ? false:(Boolean)getProcessVariable(name);
//	}
}

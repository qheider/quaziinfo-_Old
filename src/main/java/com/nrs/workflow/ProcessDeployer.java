package com.nrs.workflow;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.bpm.Jbpm;
import org.jboss.seam.log.Log;
import org.jbpm.JbpmContext;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;

//import com.nas.recovery.web.action.loan.LoanDataAction;
//import com.nas.recovery.web.action.workflowmgt.Tt_mod2ProcessAction;
//import com.nas.recovery.web.action.workflowmgt.UnscrdLoanWorkflowProcessAction;

@Name("processDeployer")
public class ProcessDeployer {

	@RequestParameter String processDefinition;
	
	@In(value="org.jboss.seam.bpm.jbpm") Jbpm jbpm;
	
	@Logger Log log;

	@Transactional
	public void deployProcess()
	{
		log.error("deploy process: #0",processDefinition);
		JbpmContext jbpmContext = jbpm.getJbpmConfiguration().createJbpmContext();
		
		try {
			jbpmContext.deployProcessDefinition(ProcessDefinition
					.parseXmlResource(processDefinition));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jbpmContext.close();
		}
		

	}
	
    @Transactional
    public void launchModulerProcess(String ModName) {
    	JbpmContext jbpmContext = jbpm.getJbpmConfiguration().createJbpmContext();
//    LoanDataAction loanDataAction=(LoanDataAction) Component.getInstance("loanDataAction");
//        try {
//        	 if (ModName.equalsIgnoreCase("mod2")){
//        		 Tt_mod2ProcessAction tt_mod2ProcessAction = (Tt_mod2ProcessAction) Component
//					.getInstance("tt_mod2ProcessAction");
//        		 tt_mod2ProcessAction.startProcess();
//        	 }
//        	GraphSession graphSession=jbpmContext.getGraphSession();
//            if (ModName.equalsIgnoreCase("mod2")){
//            
//            ProcessDefinition processDefinition=graphSession.findLatestProcessDefinition("tt_mod2");
////            org.jbpm.graph.exe.ProcessInstance processInstanceGraph=jbpmContext.newProcessInstance(processDefinition.getName());
//            jbpmContext.deployProcessDefinition(processDefinition);
//            org.jbpm.graph.exe.ProcessInstance processInstanceGraph=jbpmContext.getProcessInstance(processDefinition.getId());
////            processInstanceGraph.signal();
//            Token primeToken=new Token(processInstanceGraph);
//            Node firstNode=primeToken.getNode();
////            ExecutionContext executionContext=ExecutionContext.currentExecutionContext();
//            ExecutionContext executionContext=new ExecutionContext(primeToken);
//            firstNode.leave(executionContext);
//            
//            
//            loanDataAction.getInstance().setProcessId(processInstanceGraph.getId());
////            jbpmContext.getSession().flush();
//            }
        } 
//    catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally{
////        	jbpmContext.close();
//        }
//
//    }
	
	
}

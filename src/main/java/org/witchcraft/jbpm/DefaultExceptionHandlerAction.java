package org.witchcraft.jbpm;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;

public class DefaultExceptionHandlerAction implements ActionHandler {

	public void execute(ExecutionContext executionContext) throws Exception {
		Node targetNode = executionContext.getProcessDefinition().getNode(
				"BackendError");
		Token token = executionContext.getProcessInstance().getRootToken();
		executionContext.setVariable("BackendErrorOriginatingNode", token
				.getNode().getName());
		Transition errorTransition = new Transition("errorTransition");
		errorTransition.setTo(targetNode);
		token.signal(errorTransition);
	}

}

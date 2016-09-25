package com.nrs.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.bpm.Jbpm;
import org.jboss.seam.log.Log;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.Transition;

@Name("jbpmHelper")
public class JbpmHelper {

	@Logger Log log;
	@RequestParameter String processDefinitionId;
	
	private DataModel nodedm;
	
	public DataModel getNodedm() {
		return nodedm;
	}

	public void setNodedm(DataModel nodedm) {
		this.nodedm = nodedm;
	}

	@In(value="org.jboss.seam.bpm.jbpm") Jbpm jbpm;
	@In
	protected JbpmContext jbpmContext;
	
	
	public List<Transition> getNodeArrivingTransitions(Node node)
	{
		List<Transition> list = new ArrayList<Transition>();
		Set s = node.getArrivingTransitions();
		for (Object t:s)
		{
			list.add((Transition)t);
		}
		return list;
	}
	
	public void buildProcessNodes(String paramPdId)
	{
		log.debug("buildProcessNodes param: #0", paramPdId);
		log.debug("buildProcessNodes requestparam: #0", processDefinitionId);
		if (paramPdId != null)
			processDefinitionId = paramPdId;
		org.jbpm.graph.def.ProcessDefinition pd = jbpmContext.getGraphSession().getProcessDefinition(Long.parseLong(processDefinitionId));
		nodedm = new ListDataModel(pd.getNodes());
		
	}
	
}

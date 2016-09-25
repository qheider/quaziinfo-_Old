package com.nrs.workflow;

import org.jboss.seam.annotations.Name;

@Name("jbpmNodeFilter")
public class JbpmNodeFilter {

	private String name;
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}

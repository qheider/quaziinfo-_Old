package com.oreon.kg.domain;

public enum WebLogType {

	Public,

	Private,

	;

	WebLogType() {
	}

	public String getName() {
		return this.toString();
	}

	public String getDisplayName() {
		return this.toString();
	}
}

package org.orion.ss.model.core;

public enum OrderTime {

	/*			denomination 		*/
	NONE("0"),
	ALL("entire turn"),
	VARIABLE("variable");

	private final String denomination;

	private OrderTime(String denomination) {
		this.denomination = denomination;
	}

	public String getDenomination() {
		return denomination;
	}

}

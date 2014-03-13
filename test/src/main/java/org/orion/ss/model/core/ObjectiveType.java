package org.orion.ss.model.core;

public enum ObjectiveType {

	/*				denomination	*/
	MAIN(			"Main"),
	SECONDARY(		"Secondary"),
	DEFENDER(		"Secondary");

	private String denomination;
	
	private ObjectiveType(String denomination){
		this.denomination = denomination;
	}

	public String getDenomination() {
		return denomination;
	}
	
}

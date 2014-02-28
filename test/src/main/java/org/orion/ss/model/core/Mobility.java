package org.orion.ss.model.core;

public enum Mobility {

	/* 					denomination */
	FOOT(				"foot"),
	WHEELED(			"wheeled"), 
	ALL_TRACTION(		"4x4"),
	HALFTRACK(			"half-tracked"),
	TRACK(				"full-tracked"),
	SEA(				"sea"),
	AIR(				"air");
	
	private String denomination;
	
	private Mobility(String denomination){
		this.denomination = denomination;
	}

	public String getDenomination() {
		return denomination;
	}

}

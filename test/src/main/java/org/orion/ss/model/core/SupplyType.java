package org.orion.ss.model.core;

public enum SupplyType {

	/*		denomination		*/
	AMMO(	"ammo"),
	FUEL(	"fuel");
	
	private String denomination;
	
	private SupplyType(String denomination){
		this.denomination = denomination;
	}

	public String getDenomination() {
		return denomination;
	}

}

package org.orion.ss.model.core;

public enum FormationLevel {

	COMPANY(		"I"),
	BATTALION( 		"II"),
	REGIMENT(		"III"),
	BRIGADE(		"X"),
	DIVISION( 		"XX"),
	CORPS( 			"XXX"),
	ARMY( 			"XXXX"),
	ARMY_GROUP(		"XXXXX"),
	AIR_SQUADRON(	"I"),
	SHIP(			"I");
	
	private String code;
	
	private FormationLevel(String code){
		this.code = code;
	}
	
	public String getCode(){
		return this.code;
	}

}

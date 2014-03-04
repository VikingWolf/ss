package org.orion.ss.model.core;

public enum FormationLevel {

	/* level		ordinal		abrv		code		supplyLimit 	uniqueId*/
	COMPANY(		0,			"C",		"I",		1,				false),
	BATTALION( 		1,			"Bn",		"II",		5,				false),
	REGIMENT(		2,			"Reg",		"III",		15,				true),
	BRIGADE(		3,			"Bde",		"X",		45,				true),
	DIVISION( 		4,			"Div",		"XX",		135,			true),
	CORPS( 			5,			"Corps",	"XXX",		810,			true),
	ARMY( 			6,			"Army",		"XXXX",		4860,			true),
	ARMY_GROUP(		7,			"Group",	"XXXXX",	19440,			true),
	AIR_SQUADRON(	0,			"Sqd",		"I",		1,				false),
	SHIP(			0,			"HMS",		"I",		1,				true);
	
	private int ordinal;
	private String abbreviation;
	private String code;
	private int supplyLimit;
	private boolean uniqueId;
	
	private FormationLevel(int ordinal, String abbreviation, String code, int supplyLimit, boolean uniqueId){
		this.ordinal = ordinal;
		this.abbreviation = abbreviation;
		this.code = code;
		this.supplyLimit = supplyLimit;
		this.uniqueId = uniqueId;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public int getSupplyLimit(){
		return this.supplyLimit;
	}
	
	public int getOrdinal(){
		return this.ordinal;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public boolean isUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(boolean uniqueId) {
		this.uniqueId = uniqueId;
	}

	public void setAbbreviation(String abreviation) {
		this.abbreviation = abreviation;
	}
	
}

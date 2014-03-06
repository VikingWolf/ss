package org.orion.ss.model.core;

public enum FormationLevel {

	/* level 	ordinal abbrev	denomination	 code 	supplyLimit uniqueId */
	COMPANY(		1, 	"Co", 	"Company", 		"I", 		1, 		false),
	BATTALION(		2, 	"Bn", 	"Battalion", 	"II", 		5, 		false),
	REGIMENT(		3, 	"Rg", 	"Regiment", 	"III", 		15, 	true),
	BRIGADE(		4, 	"Bd", 	"Brigade", 		"X", 		45, 	true),
	DIVISION(		5, 	"D", 	"Division", 	"XX", 		135, 	true),
	CORPS(			6, 	"C", 	"Corps", 		"XXX", 		810, 	true),
	ARMY(			7, 	"A", 	"Army", 		"XXXX", 	4860, 	true),
	ARMY_GROUP(		8, 	"G", 	"Army Group", 	"XXXXX", 	19440, 	true),
	AIR_SQUADRON(	1, 	"Sqd", "Squadron", 		"I", 		1, 		false),
	SHIP(			1, 	"HMS", "Ship", 			"I", 		1, 		true);

	private int ordinal;
	private String denomination;
	private String abbreviation;
	private String code;
	private int supplyLimit;
	private boolean uniqueId;

	private FormationLevel(int ordinal, String abbreviation, String denomination, String code, int supplyLimit, boolean uniqueId) {
		this.ordinal = ordinal;
		this.denomination = denomination;
		this.abbreviation = abbreviation;
		this.code = code;
		this.supplyLimit = supplyLimit;
		this.uniqueId = uniqueId;
	}

	public String getCode() {
		return code;
	}

	public int getSupplyLimit() {
		return supplyLimit;
	}

	public int getOrdinal() {
		return ordinal;
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
		abbreviation = abreviation;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

}

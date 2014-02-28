package org.orion.ss.model.core;

public enum Country {

	/*			name	manpowerModifier	*/
	GER(		"Germany",			0.8d), 
	UK(			"United Kingdom",	0.7d);
	
	private String name;
	private double manpowerModifier;
	
	private Country(String name, double manpowerModifier){
		this.name = name;
		this.manpowerModifier = manpowerModifier;
	}

	public String getName() {
		return name;
	}

	public double getManpowerModifier() {
		return manpowerModifier;
	}
	
}

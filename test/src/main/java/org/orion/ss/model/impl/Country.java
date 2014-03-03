package org.orion.ss.model.impl;

public class Country {

	private String name;
	private double manpowerModifier;
	private Market market;

	public Country(String name, double manpowerModifier) {
		this.name = name;
		this.manpowerModifier = manpowerModifier;
		market = new Market();
	}

	/* adders */
	
	/* getters & setters */
	
	
	public String getName() {
		return name;
	}

	public double getManpowerModifier() {
		return manpowerModifier;
	}

	public Market getMarket() {
		return market;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setManpowerModifier(double manpowerModifier) {
		this.manpowerModifier = manpowerModifier;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

}

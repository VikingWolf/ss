package org.orion.ss.model.impl;

public class Country {

	private final String name;
	private final double manpowerModifier;

	public Country(String name, double manpowerModifier) {
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

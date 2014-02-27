package org.orion.ss.model.impl;

import org.orion.ss.model.core.DefenseType;

public class Defense {

	private DefenseType type;
	private double strength;

	public Defense(DefenseType type, double strength) {
		super();
		this.type = type;
		this.strength = strength;
	}

	/* getters & setters */

	public DefenseType getType() {
		return type;
	}

	public void setType(DefenseType type) {
		this.type = type;
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

}

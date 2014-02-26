package org.orion.ss.model.impl;

import org.orion.ss.model.core.DefenseType;

public class Defense {

	private DefenseType type;
	private int strength;

	public Defense(DefenseType type, int strength) {
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

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

}

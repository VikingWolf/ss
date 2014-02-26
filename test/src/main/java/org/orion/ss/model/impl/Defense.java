package org.orion.ss.model.impl;

import org.orion.ss.model.core.DefenseType;

public class Defense {

	private DefenseType type;
	private short strength;

	/* getters & setters */

	public DefenseType getType() {
		return type;
	}

	public void setType(DefenseType type) {
		this.type = type;
	}

	public short getStrength() {
		return strength;
	}

	public void setStrength(short strength) {
		this.strength = strength;
	}

}

package org.orion.ss.model.impl;

import org.orion.ss.model.core.AttackType;

public class Attack {

	private AttackType type;
	private double range;
	private short strength;

	/* getters & setters */
	public AttackType getType() {
		return type;
	}

	public void setType(AttackType type) {
		this.type = type;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public short getStrength() {
		return strength;
	}

	public void setStrength(short strength) {
		this.strength = strength;
	}

}

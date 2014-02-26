package org.orion.ss.model.impl;

import org.orion.ss.model.core.AttackType;

public class Attack {

	private AttackType type;
	private double range;
	private int strength;

	public Attack(AttackType type, double range, int strength) {
		super();
		this.type = type;
		this.range = range;
		this.strength = strength;
	}

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

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

}

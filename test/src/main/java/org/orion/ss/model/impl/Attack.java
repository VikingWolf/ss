package org.orion.ss.model.impl;

import org.orion.ss.model.core.AttackType;

public class Attack {

	private AttackType type;
	private double range;
	private double strength;
	private Stock consumption;

	public Attack(AttackType type, double range, double strength) {
		super();
		this.type = type;
		this.range = range;
		this.strength = strength;
		consumption = new Stock();
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

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	public Stock getConsumption() {
		return consumption;
	}

	public void setConsumption(Stock consumption) {
		this.consumption = consumption;
	}
	
}

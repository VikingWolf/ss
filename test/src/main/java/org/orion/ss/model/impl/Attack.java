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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(range);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Attack other = (Attack) obj;
		if (Double.doubleToLongBits(range) != Double.doubleToLongBits(other.range)) return false;
		return true;
	}

	public void increaseStrength(double strength) {
		this.strength += strength;
	}

	public void increaseConsumption(Stock consumption) {
		this.consumption.add(consumption);
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

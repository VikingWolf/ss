package org.orion.ss.model.impl;

import java.util.Map;

import org.orion.ss.model.ActivableImpl;

public class Fortification extends ActivableImpl {

	private byte strength;
	private double state;
	private Map<Company, Byte> garrison;

	/* getters & setters */

	public byte getStrength() {
		return strength;
	}

	public void setStrength(byte strength) {
		this.strength = strength;
	}

	public double getState() {
		return state;
	}

	public void setState(double state) {
		this.state = state;
	}

	public Map<Company, Byte> getGarrison() {
		return garrison;
	}

	public void setGarrison(Map<Company, Byte> garrison) {
		this.garrison = garrison;
	}

}

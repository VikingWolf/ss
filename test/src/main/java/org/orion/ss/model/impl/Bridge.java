package org.orion.ss.model.impl;

import org.orion.ss.model.core.BridgeType;

public class Bridge {

	private BridgeType type;
	private double state;

	/* getters & setters */
	public BridgeType getType() {
		return type;
	}

	public void setType(BridgeType type) {
		this.type = type;
	}

	public double getState() {
		return state;
	}

	public void setState(double state) {
		this.state = state;
	}

}

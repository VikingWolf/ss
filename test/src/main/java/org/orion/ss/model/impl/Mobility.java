package org.orion.ss.model.impl;

import org.orion.ss.model.core.MobilityType;

public class Mobility {

	private final MobilityType type;

	private final double speed;

	public Mobility(MobilityType type, double speed) {
		super();
		this.type = type;
		this.speed = speed;
	}

	/* getters & setters */

	public MobilityType getType() {
		return type;
	}

	public double getSpeed() {
		return speed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Mobility other = (Mobility) obj;
		if (type != other.type) return false;
		return true;
	}

}

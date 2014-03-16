package org.orion.ss.model.core;

public enum PositionRole {

	ATTACKER,
	DEFENDER;

	public final static PositionRole enemy(PositionRole role) {
		if (role == ATTACKER)
			return DEFENDER;
		else if (role == DEFENDER)
			return ATTACKER;
		else return null;
	}

}

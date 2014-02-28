package org.orion.ss.model.core;

import java.util.Arrays;
import java.util.List;

public enum WeaponType {

	SMALL_ARM("Small Arm", new AttackType[] { AttackType.SOFT });

	private String denomination;
	private List<AttackType> attackTypes;

	private WeaponType(String denomination, AttackType[] attackTypes) {
		this.denomination = denomination;
		this.attackTypes = Arrays.asList(attackTypes);
	}

	public String getDenomination() {
		return denomination;
	}

	public List<AttackType> getAttackTypes() {
		return attackTypes;
	}

}

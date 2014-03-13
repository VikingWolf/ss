package org.orion.ss.model.core;

import java.util.Arrays;
import java.util.List;

public enum WeaponType {

	/* denomination unitOfFireModifier attacks */
	SMALL_ARM("Small Arm", 0.05d, new AttackType[] { AttackType.SOFT }),
	FIELD_GUN("Field Gun", 0.05d, new AttackType[] { AttackType.BARRAGE });

	private String denomination;
	private double unitOfFireModifier;
	private List<AttackType> attackTypes;

	private WeaponType(String denomination, double unitOfFireModifier, AttackType[] attackTypes) {
		this.denomination = denomination;
		this.attackTypes = Arrays.asList(attackTypes);
		this.unitOfFireModifier = unitOfFireModifier;
	}

	public String getDenomination() {
		return denomination;
	}

	public List<AttackType> getAttackTypes() {
		return attackTypes;
	}

	public double getUnitOfFireModifier() {
		return unitOfFireModifier;
	}

	public void setUnitOfFireModifier(double unitOfFireModifier) {
		this.unitOfFireModifier = unitOfFireModifier;
	}

}

/*
 * the use modifier is the modifier refered to habitual use of weapon rof, a concept related to the unit of fire. the unit of fire modifier
 * is the ratio between the maximum rof of a weapon and the weapon unit of fire the unit of fire represents a specified number of rounds per
 * weapon used in an hour of normal combat action
 */
/**
 * QF 25-pdr Gunfire, 6–8 rpm Intense, 5 rpm Rapid, 4 rpm Normal, 3 rpm Slow, 2 rpm Very slow, 1 rpm
 */

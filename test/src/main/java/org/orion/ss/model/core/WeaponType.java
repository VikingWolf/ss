package org.orion.ss.model.core;

public enum WeaponType {

	SMALL_ARM("Small Arm", AttackType.SOFT);
	
	private String denomination;
	private AttackType attackType;
	
	private WeaponType(String denomination, AttackType attackType){
		this.denomination = denomination;
		this.attackType = attackType;
	}

	public String getDenomination() {
		return denomination;
	}

	public AttackType getAttackType() {
		return attackType;
	}
	
}

package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.AttackCapable;
import org.orion.ss.model.core.AttackType;
import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.core.WeaponType;

public class WeaponModel implements AttackCapable {

	private String denomination;
	private int maxROF; /* shots per minutes */
	private WeaponType type;
	private double maxRange;
	private double muzzleVelocity;
	private double shellWeight;
	private int crew;

	public WeaponModel(String denomination, WeaponType type, int maxROF, double muzzleVelocity, double maxRange, double shellWeight, int crew) {
		super();
		this.type = type;
		this.denomination = denomination;
		this.maxROF = maxROF;
		this.maxRange = maxRange;
		this.shellWeight = shellWeight;
		this.muzzleVelocity = muzzleVelocity;
		this.crew = crew;
	}

	@Override
	public List<Attack> computeAttacks() {
		List<Attack> result = new ArrayList<Attack>();
		for (AttackType attackType : type.getAttackTypes()) {
			double strength = maxRange * maxROF * muzzleVelocity * shellWeight;
			Attack attack = new Attack(attackType, maxRange, strength);
			result.add(attack);
		}
		return result;
	}

	public Stock computeSupplyConsumption() {
		Stock result = new Stock();
		result.put(SupplyType.AMMO, shellWeight * maxROF);
		return result;
	}

	public boolean hasAttackType(AttackType type) {
		boolean result = false;
		for (Attack attack : this.computeAttacks()) {
			if (attack.getType().equals(type)) {
				result = true;
				break;
			}
		}
		return result;
	}

	/* getters & setters */

	public int getMaxROF() {
		return maxROF;
	}

	public WeaponType getType() {
		return type;
	}

	public void setType(WeaponType type) {
		this.type = type;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public void setMaxROF(int maxROF) {
		this.maxROF = maxROF;
	}

	public double getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(double maxRange) {
		this.maxRange = maxRange;
	}

	public double getShellWeight() {
		return shellWeight;
	}

	public void setShellWeight(double shellWeight) {
		this.shellWeight = shellWeight;
	}

	public double getMuzzleVelocity() {
		return muzzleVelocity;
	}

	public void setMuzzleVelocity(double muzzleVelocity) {
		this.muzzleVelocity = muzzleVelocity;
	}

	public int getCrew() {
		return crew;
	}

	public void setCrew(int crew) {
		this.crew = crew;
	}

}

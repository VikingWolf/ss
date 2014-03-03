package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.CombatUnitModel;
import org.orion.ss.model.Upgradable;
import org.orion.ss.model.core.CompanyTrait;
import org.orion.ss.model.core.CompanyType;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.Mobility;

public class CompanyModel extends CombatUnitModel implements Upgradable {

	private String code;
	private CompanyType type;
	private Mobility mobility;
	private double speed;
	private List<CompanyTrait> traits;
	private List<CompanyModel> upgrades;
	private int initiative;
	private int maxStrength;

	public CompanyModel(String code, CompanyType type, Mobility mobility, double speed, int initiative, int maxStrength, Country country) {
		super(country);
		this.code = code;
		this.type = type;
		this.mobility = mobility;
		this.speed = speed;
		this.initiative = initiative;
		this.maxStrength = maxStrength;
		traits = new ArrayList<CompanyTrait>();
		upgrades = new ArrayList<CompanyModel>();
	}

	@Override
	public FormationLevel getFormationLevel() {
		return FormationLevel.COMPANY;
	}

	@Override
	public String toString() {
		return this.getCode();
	}

	/* adders */
	public void addUpgrade(CompanyModel model) {
		upgrades.add(model);
	}

	/* getters & setters */

	@Override
	public Mobility getMobility() {
		return mobility;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public CompanyType getType() {
		return type;
	}

	public void setType(CompanyType type) {
		this.type = type;
	}

	public List<CompanyTrait> getTraits() {
		return traits;
	}

	public void setTraits(List<CompanyTrait> traits) {
		this.traits = traits;
	}

	public List<CompanyModel> getUpgrades() {
		return upgrades;
	}

	public void setUpgrades(List<CompanyModel> upgrades) {
		this.upgrades = upgrades;
	}

	public void setMobility(Mobility mobility) {
		this.mobility = mobility;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getInitiative() {
		return initiative;
	}

	public void setInitiative(int initiative) {
		this.initiative = initiative;
	}

	public int getMaxStrength() {
		return maxStrength;
	}

	public void setMaxStrength(int strength) {
		maxStrength = strength;
	}

}

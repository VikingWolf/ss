package org.orion.ss.model.impl;

import java.util.List;

import org.orion.ss.model.CombatUnitModel;
import org.orion.ss.model.core.CompanyTrait;
import org.orion.ss.model.core.CompanyType;
import org.orion.ss.model.core.Mobility;

public class CompanyModel extends CombatUnitModel {

	private String code;
	private CompanyType type;
	private Mobility mobility;
	private List<CompanyTrait> traits;
	private List<CompanyModel> upgrades;

	@Override
	public Mobility getMobility() {
		return mobility;
	}

	/* getters & setters */

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

}

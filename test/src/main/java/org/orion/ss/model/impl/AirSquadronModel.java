package org.orion.ss.model.impl;

import java.util.List;

import org.orion.ss.model.CombatUnitModel;
import org.orion.ss.model.core.AirSquadronType;
import org.orion.ss.model.core.AttackType;
import org.orion.ss.model.core.Country;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.Mobility;

public class AirSquadronModel extends CombatUnitModel {

	private String code;
	private AirSquadronType type;
	private List<AirSquadronModel> upgrades;

	public AirSquadronModel(Country country){
		super(country);
	}
	
	@Override
	public double computeWeaponAmountModifier(AttackType attackType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Defense> computeDefenses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mobility getMobility() {
		return Mobility.AIR;
	}

	@Override
	public FormationLevel getFormationLevel() {
		return FormationLevel.AIR_SQUADRON;
	}

	/* getters & setters */

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public AirSquadronType getType() {
		return type;
	}

	public void setType(AirSquadronType type) {
		this.type = type;
	}

	public List<AirSquadronModel> getUpgrades() {
		return upgrades;
	}

	public void setUpgrades(List<AirSquadronModel> upgrades) {
		this.upgrades = upgrades;
	}

}

package org.orion.ss.model.impl;

import java.util.List;

import org.orion.ss.model.CombatUnitModel;
import org.orion.ss.model.core.AttackType;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.Mobility;
import org.orion.ss.model.core.ShipType;

public class ShipModel extends CombatUnitModel {

	private String code;
	private ShipType type;

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
		return Mobility.SEA;
	}

	@Override
	public FormationLevel getFormationLevel() {
		return FormationLevel.SHIP;
	}

	/* getters & setters */

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ShipType getType() {
		return type;
	}

	public void setType(ShipType type) {
		this.type = type;
	}

}

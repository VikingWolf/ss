package org.orion.ss.model.impl;

import org.orion.ss.model.CombatUnitModel;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.Mobility;
import org.orion.ss.model.core.ShipType;

public class ShipModel extends CombatUnitModel {

	private String code;
	private ShipType type;
	private double speed;

	public ShipModel(Country country) {
		super(country);
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

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

}

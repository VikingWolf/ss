package org.orion.ss.model.impl;

import org.orion.ss.model.CombatUnitModel;
import org.orion.ss.model.core.Mobility;
import org.orion.ss.model.core.ShipType;

public class ShipModel extends CombatUnitModel {

	private String code;
	private ShipType type;

	@Override
	public Mobility getMobility() {
		return Mobility.SEA;
	}

}

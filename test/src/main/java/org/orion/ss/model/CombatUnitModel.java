package org.orion.ss.model;

import java.util.List;

import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.Defense;

public abstract class CombatUnitModel extends UnitModel {

	private List<Attack> attacks;
	private List<Defense> denfenses;

	/* getters & setters */

	public List<Attack> getAttacks() {
		return attacks;
	}

	public void setAttacks(List<Attack> attacks) {
		this.attacks = attacks;
	}

	public List<Defense> getDenfenses() {
		return denfenses;
	}

	public void setDenfenses(List<Defense> denfenses) {
		this.denfenses = denfenses;
	}

}

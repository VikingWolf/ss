package org.orion.ss.model;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.Defense;

public abstract class CombatUnitModel extends UnitModel {

	private List<Attack> attacks;
	private List<Defense> defenses;

	public CombatUnitModel() {
		super();
		attacks = new ArrayList<Attack>();
		defenses = new ArrayList<Defense>();
	}

	/* adders */
	public void addAttack(Attack attack) {
		attacks.add(attack);
	}

	public void addDefense(Defense defense) {
		defenses.add(defense);
	}

	/* getters & setters */

	public List<Attack> getAttacks() {
		return attacks;
	}

	public void setAttacks(List<Attack> attacks) {
		this.attacks = attacks;
	}

	public List<Defense> getDefenses() {
		return defenses;
	}

	public void setDefenses(List<Defense> defenses) {
		this.defenses = defenses;
	}

}

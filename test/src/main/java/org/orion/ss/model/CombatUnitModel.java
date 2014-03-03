package org.orion.ss.model;

import org.orion.ss.model.impl.Country;
import org.orion.ss.model.impl.WeaponModel;
import org.orion.ss.model.impl.Weaponry;

public abstract class CombatUnitModel extends UnitModel {

	private Weaponry weaponry;

	public CombatUnitModel(Country country) {
		super(country);
		weaponry = new Weaponry();
	}

	/* adders */
	public void addWeaponry(WeaponModel weaponModel, int amount) {
		weaponry.put(weaponModel, amount);
	}

	/* getters & setters */

	public Weaponry getWeaponry() {
		return weaponry;
	}

	public void setWeaponry(Weaponry weaponry) {
		this.weaponry = weaponry;
	}

}

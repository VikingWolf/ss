package org.orion.ss.model;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.core.AttackType;
import org.orion.ss.model.core.Country;
import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.model.impl.WeaponModel;
import org.orion.ss.model.impl.Weaponry;

public abstract class CombatUnitModel extends UnitModel implements AttackCapable {

	private final static double WEAPON_AMOUNT_MODIFIER_EXPONENT = 0.5d;

	private Weaponry weaponry;

	public CombatUnitModel(Country country) {
		super(country);
		weaponry = new Weaponry();
	}

	public abstract double computeWeaponAmountModifier(AttackType attackType);

	@Override
	public List<Attack> computeAttacks() {
		List<Attack> result = new ArrayList<Attack>();
		for (WeaponModel weaponModel : getWeaponry().keySet()) {
			for (Attack attack : weaponModel.computeAttacks()) {
				double strength = attack.getStrength() * getWeaponry().get(weaponModel)
						* Math.pow(this.computeWeaponAmountModifier(attack.getType()), WEAPON_AMOUNT_MODIFIER_EXPONENT);
				Stock adjustedStock = new Stock();
				for (SupplyType supplyType : attack.getConsumption().keySet()) {
					double amount = attack.getConsumption().get(supplyType) * getWeaponry().get(weaponModel);
					adjustedStock.put(supplyType, amount);
				}
				Attack adjustedAttack = new Attack(attack.getType(), attack.getRange(), strength);
				adjustedAttack.setConsumption(adjustedStock);
				result.add(adjustedAttack);
			}
		}
		return result;
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

package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.core.AttackType;
import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.AttackSet;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Defense;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.model.impl.WeaponModel;

public class CombatService extends Service {

	private final static double WEAPON_AMOUNT_MODIFIER_EXPONENT = 0.5d;

	public CombatService(Game game) {
		super(game);
	}

	public AttackSet computeAttacks(Company company) {
		AttackSet result = new AttackSet();
		for (WeaponModel weaponModel : company.getWeaponry().keySet()) {
			for (Attack attack : weaponModel.computeAttacks()) {
				double strength = attack.getStrength()
						* company.getWeaponry().get(weaponModel)
						* Math.pow(this.computeWeaponAmountModifier(company, attack.getType()), WEAPON_AMOUNT_MODIFIER_EXPONENT)
						* Math.pow(company.getExperience(), attack.getType().getExperienceExponent())
						* Math.pow(company.getOrganization(), attack.getType().getOrganizationExponent());
				Stock adjustedStock = new Stock();
				for (SupplyType supplyType : attack.getConsumption().keySet()) {
					double amount = attack.getConsumption().get(supplyType)
							* company.getModel().getWeaponry().get(weaponModel)
							* company.getStrength();
					adjustedStock.put(supplyType, amount);
				}
				Attack adjustedAttack = new Attack(attack.getType(), attack.getRange(), strength);
				adjustedAttack.setConsumption(adjustedStock);
				result.add(adjustedAttack);
			}
		}
		return result;
	}

	public List<Defense> computeDefenses(Company company) {
		List<Defense> result = new ArrayList<Defense>();
		for (Defense defense : company.getModel().getType().getDefenses()) {
			double adjustedStrength = defense.getStrength() * Math.pow(company.getExperience(), defense.getType().getExperienceExponent())
					* Math.pow(company.getOrganization(), defense.getType().getOrganizationExponent());
			result.add(new Defense(defense.getType(), adjustedStrength));
		}
		return result;
	}

	public boolean isAttackCapable(Company company, AttackType type) {
		boolean result = false;
		for (Attack attack : computeAttacks(company)) {
			if (attack.getType().equals(type)) {
				result = true;
				break;
			}
		}
		return result;
	}

	private double computeWeaponAmountModifier(Company company, AttackType attackType) {
		int amount = 0;
		for (WeaponModel weapon : company.getModel().getWeaponry().keySet()) {
			if (weapon.hasAttackType(attackType)) {
				amount += weapon.getCrew() * company.getWeaponry().get(weapon);
			}
		}
		return (double) company.getModel().getMaxStrength() / amount;
	}

}

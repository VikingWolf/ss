package org.orion.ss.service;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.WeaponModel;
import org.orion.ss.utils.Maths;
import org.orion.ss.utils.NumberFormats;

public class ManagementService extends Service {

	private final static int REGULAR_REINFORCEMENTS_COST = 100;
	private final static double ELITE_REINFORCEMENTS_UNITARY_DEVIATION = 0.20d;
	private final static double ELITE_REINFORCEMENT_COST_EXPONENT = 1.75d;
	private final static double ELITE_REINFORCEMENT_AVAILABILITY_EXPONENT = 1.5d;

	public ManagementService(Game game) {
		super(game);
	}

	public ReinforceCost regularReinforceCost(Company company) {
		// TODO armament lost / breakdown replacement cost
		double toReinforce = (1 - company.getStrength());
		double cost = weaponryResupplyCost(company);
		cost +=
				REGULAR_REINFORCEMENTS_COST
						/ company.getCountry().getManpowerModifier()
						* toReinforce
						* company.getModel().getMaxStrength();
		return new ReinforceCost(toReinforce, (int) cost);
	}

	public ReinforceCost eliteReinforceCost(Company company) {
		double toReinforce = (1 - company.getStrength());
		double availability = Math.max(0, Maths.gaussianRandomize(1 / Math.pow(company.getExperience(), ELITE_REINFORCEMENT_AVAILABILITY_EXPONENT), ELITE_REINFORCEMENTS_UNITARY_DEVIATION));
		toReinforce = Math.min(toReinforce, availability);
		double cost = weaponryResupplyCost(company);
		cost += REGULAR_REINFORCEMENTS_COST
				/ company.getCountry().getManpowerModifier()
				* toReinforce
				* company.getModel().getMaxStrength()
				* Math.pow(company.getExperience(), ELITE_REINFORCEMENT_COST_EXPONENT);
		return new ReinforceCost(toReinforce, (int) cost);
	}

	private double weaponryResupplyCost(Company company) {
		double cost = 0;
		for (WeaponModel weaponModel : company.getModel().getWeaponry().keySet()) {
			int amount = company.getModel().getWeaponry().get(weaponModel) - company.getWeaponry().get(weaponModel);
			cost += amount * weaponModel.getCost();
		}
		return cost;
	}

	public int upgradeCost(Company target, CompanyModel upgrade) {
		return getValue(upgrade) - getValue(target.getModel());
	}

	public int getValue(CompanyModel model) {
		int value = 0;
		for (WeaponModel weaponModel : model.getWeaponry().keySet()) {
			value += weaponModel.getCost() * model.getWeaponry().get(weaponModel);
		}
		return value;
	}

	public void regularReinforce(Company company, ReinforceCost cost) {
		double avgXp = company.getExperience() * company.getStrength() + cost.getStrength() / (company.getStrength() + cost.getStrength());
		company.getPosition().decreasePrestige(cost.getCost());
		company.increaseStrength(cost.getStrength());
		company.setExperience(avgXp);
		getGame().getLog().addEntry(company.getId() + " reinforced " + NumberFormats.PERCENT.format(cost.getStrength()) + " strength with regular replacements spending " + cost.getCost() + " prestige.");
	}

	public void eliteReinforce(Company company, ReinforceCost cost) {
		company.getPosition().decreasePrestige(cost.getCost());
		company.increaseStrength(cost.getStrength());
		getGame().getLog().addEntry(company.getId() + " reinforced " + NumberFormats.PERCENT.format(cost.getStrength()) + " strength with elite replacements spending " + cost.getCost() + " prestige.");
	}

	public void upgrade(Company company, CompanyModel upgrade) {
		int cost = this.upgradeCost(company, upgrade);
		company.getPosition().decreasePrestige(cost);
		company.setModel(upgrade);
		getGame().getLog().addEntry(company.getId() + " upgraded to " + upgrade.getCode() + " spending " + cost + " prestige.");
	}

}

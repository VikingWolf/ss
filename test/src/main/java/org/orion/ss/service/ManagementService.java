package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.core.TroopTypesCompatibility;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.model.impl.WeaponModel;
import org.orion.ss.utils.NumberFormats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagementService extends Service {

	protected final static Logger logger = LoggerFactory.getLogger(ManagementService.class);

	private final static int REGULAR_REINFORCEMENTS_COST = 10;
	private final static double ELITE_REINFORCEMENT_COST_EXPONENT = 1.75d;
	private final static double REINFORCEMENT_AVAILABILITY_EXPONENT = 0.4d;
	private final static double FORMATION_LEVEL_COST_EXPONENT = 0.45d;

	protected ManagementService(Game game) {
		super(game);
	}

	public int regularReinforceCost(Formation formation) {
		ReinforceCost result = new ReinforceCost();
		for (Company company : formation.getAllCompanies()) {
			result.add(regularReinforceCost(company));
		}
		return result.getCost();
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
		double availability = (double) getReinforceAvailability(company.getExperience(), company.getPosition()) / company.getModel().getMaxStrength();
		toReinforce = Math.min(toReinforce, availability);
		double cost = weaponryResupplyCost(company);
		cost += REGULAR_REINFORCEMENTS_COST
				/ company.getCountry().getManpowerModifier()
				* toReinforce
				* company.getModel().getMaxStrength()
				* Math.pow(company.getExperience(), ELITE_REINFORCEMENT_COST_EXPONENT);
		return new ReinforceCost(toReinforce, (int) cost);
	}

	public int resupplyCost(Formation formation) {
		int result = 0;
		for (Company company : formation.getAllCompanies()) {
			result += resupplyCost(company);
		}
		return result;
	}

	private double weaponryResupplyCost(Company company) {
		double cost = 0;
		for (WeaponModel weaponModel : company.getModel().getWeaponry().keySet()) {
			int amount = company.getModel().getWeaponry().get(weaponModel) - company.getWeaponry().get(weaponModel);
			cost += amount * weaponModel.getCost();
		}
		return cost;
	}

	public void regularReinforce(Formation formation) {
		for (Company company : formation.getAllCompanies()) {
			regularReinforce(company);
		}
	}

	public void regularReinforce(Company company) {
		regularReinforce(company, this.regularReinforceCost(company));
	}

	public void regularReinforce(Company company, ReinforceCost cost) {
		double avgXp = company.getExperience() * company.getStrength() + cost.getStrength() / (company.getStrength() + cost.getStrength());
		company.getPosition().decreasePrestige(cost.getCost());
		company.increaseStrength(cost.getStrength());
		company.setExperience(avgXp);
		getGame().getLog().addEntry(company.getFullLongName() + " reinforced " + NumberFormats.PERCENT.format(cost.getStrength()) + " strength with regular replacements spending " + cost.getCost() + " prestige.");
	}

	public void eliteReinforce(Company company, ReinforceCost cost) {
		company.getPosition().decreasePrestige(cost.getCost());
		company.increaseStrength(cost.getStrength());
		getGame().getLog().addEntry(company.getFullLongName() + " reinforced " + NumberFormats.PERCENT.format(cost.getStrength()) + " strength with elite replacements spending " + cost.getCost() + " prestige.");
	}

	public void resupply(Formation formation) {
		for (Company company : formation.getAllCompanies()) {
			resupply(company);
		}
	}

	public void resupply(Company company) {
		int cost = this.resupplyCost(company);
		company.getPosition().decreasePrestige(cost);
		company.resupply();
		getGame().getLog().addEntry(company.getFullLongName() + " re-supplied spending " + cost + " prestige.");
	}

	public int resupplyCost(Company company) {
		int cost = 0;
		for (SupplyType supplyType : company.getMaxSupplies().keySet()) {
			double amount = company.getMaxSupplies().get(supplyType) - company.getSupplies().get(supplyType);
			cost += amount * company.getCountry().getMarket().get(supplyType);
		}
		return cost;
	}

	public void buySupplies(Stock stock, Position position, Location location) {
		int cost = stockValue(stock, position);
		position.decreasePrestige(cost);
		position.addStock(stock, location);
		getGame().getLog().addEntry(position.getFullLongName() + " bought " + stock.toString() + " to location " + location.toString() + " spending " + cost + " prestige.");
	}

	public int getCompanyValue(CompanyModel model) {
		int value = 0;
		for (WeaponModel weaponModel : model.getWeaponry().keySet()) {
			value += weaponModel.getCost() * model.getWeaponry().get(weaponModel);
		}
		return value;
	}

	public int stockValue(Stock stock, Position position) {
		int cost = 0;
		for (SupplyType supply : stock.keySet()) {
			cost += stock.get(supply) * position.getCountry().getMarket().get(supply);
		}
		return cost;
	}

	public int upgradeCost(Company target, CompanyModel upgrade) {
		return getCompanyValue(upgrade) - getCompanyValue(target.getModel());
	}

	public void upgradeCompany(Company company, CompanyModel upgrade) {
		int cost = this.upgradeCost(company, upgrade);
		company.getPosition().decreasePrestige(cost);
		company.setModel(upgrade);
		getGame().getLog().addEntry(company.getFullLongName() + " upgraded to " + upgrade.getCode() + " spending " + cost + " prestige.");
	}

	public int getReinforceAvailability(double xp, Position position) {
		double available = Math.pow(position.getPrestige() / REGULAR_REINFORCEMENTS_COST, Math.pow(position.getCountry().getManpowerModifier() / xp, REINFORCEMENT_AVAILABILITY_EXPONENT));
		return (int) available;

	}

	public void dismiss(Company company) {
		company.getParent().getCompanies().remove(company);
		getGame().getLog().addEntry(company.getFullLongName() + " moved to the reserve. ");

	}

	public void dismiss(Formation formation) {
		formation.getParent().getSubordinates().remove(formation);
		getGame().getLog().addEntry(formation.getFullLongName() + " moved to the reserve. ");
	}

	public int purchaseCost(Formation formation, CompanyModel companyModel) {
		if (companyModel != null) {
			Company company = new Company(companyModel);
			company.setParent(formation);
			ReinforceCost cost = this.regularReinforceCost(company);
			company.setStrength(1.0d);
			int suppliesCost = this.resupplyCost(company);
			return cost.getCost() + suppliesCost;
		} else return 0;
	}

	public Company purchaseCompany(Formation formation, CompanyModel companyModel) {
		int cost = purchaseCost(formation, companyModel);
		logger.error("cost=" + cost);
		formation.getPosition().decreasePrestige(cost);
		int id = 1;
		for (Company company : formation.getCompanies()) {
			if (company.getId() >= id) {
				id = company.getId() + 1;
			}
		}
		Company company = new Company(companyModel);
		company.setId(id);
		company.setStrength(1.0d);
		company.resupply();
		formation.addCompany(company);
		getGame().getLog().addEntry(company.getFullLongName() + " purchased to " + formation.getFullLongName() + " spending " + cost + " prestige.");
		return company;
	}

	public int createFormationCost(Formation parent, CompanyModel hqModel, FormationLevel level) {
		int cost = purchaseCost(parent, hqModel);
		double levelModifier = Math.pow(level.getOrdinal(), FORMATION_LEVEL_COST_EXPONENT);
		cost *= levelModifier;
		return cost;
	}

	public void createFormation(Formation parent, CompanyModel hqModel, FormationLevel level, TroopType troopType) {
		int cost = createFormationCost(parent, hqModel, level);
		parent.getPosition().decreasePrestige(cost);
		int id = 1;
		if (level.isUniqueId()) {
			id = parent.getCountry().getLastIdFor(level) + 1;
		} else {
			for (Formation subordinate : parent.getSubordinates()) {
				if (subordinate.getId() >= id) {
					id = subordinate.getId() + 1;
				}
			}

		}
		Formation formation = new Formation(level, troopType, id);
		Company company = new Company(hqModel, 0, 1.00d, 1.00d, 1.00d);
		Stock supplies = (Stock) company.getMaxSupplies().clone();
		company.setSupplies(supplies);
		formation.addCompany(company);
		parent.addSubordinate(formation);
		getGame().getLog().addEntry(formation.getFullLongName() + " created " + formation.getFullLongName() + " spending " + cost + " prestige.");
	}

	public List<CompanyModel> getCompanyModelsFor(Formation formation) {
		List<CompanyModel> result = new ArrayList<CompanyModel>();
		List<TroopType> validTroopTypes = getSubordinateTroopTypes(formation);
		for (CompanyModel model : formation.getCountry().getCompanyModels()) {
			if (validTroopTypes.contains(model.getType())) {
				result.add(model);
			}
		}
		return result;
	}

	public List<FormationLevel> getSubordinables(Formation formation) {
		List<FormationLevel> subordinables = new ArrayList<FormationLevel>();
		for (FormationLevel level : FormationLevel.values()) {
			if ((level.getOrdinal() > FormationLevel.COMPANY.getOrdinal()) && (level.getOrdinal() < formation.getFormationLevel().getOrdinal())) {
				subordinables.add(level);
			}
		}
		return subordinables;
	}

	public List<TroopType> getSubordinateTroopTypes(Formation formation) {
		return TroopTypesCompatibility.loadCompatibles(formation.getTroopType(), formation.getFormationLevel());
	}

}

package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.ActivableImpl;
import org.orion.ss.model.AttackCapable;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.core.AttackType;
import org.orion.ss.model.core.Country;
import org.orion.ss.model.core.SupplyType;

public class Company extends ActivableImpl implements Mobile, AttackCapable {

	private final static double INITIATIVE_EXPERIENCE_EXPONENT = 0.5d;
	private final static double INITIATIVE_ORGANIZATION_EXPONENT = 0.5d;

	private String code;
	private CompanyModel model;
	private Location location;
	private double strength;
	private double experience;
	private double organization;
	private double morale;
	private Stock supplies;
	private Formation parent;

	public Company(Company company) {
		super();
		model = company.getModel();
		supplies = company.getSupplies();
		code = company.getCode();
		strength = company.getStrength();
		experience = company.getExperience();
		organization = company.getOrganization();
		morale = company.getMorale();
	}

	public Company(CompanyModel model, String code, Location location, double experience, double morale, double strength) {
		super();
		this.model = model;
		supplies = new Stock();
		this.code = code;
		this.location = location;
		this.strength = strength;
		this.experience = experience;
		organization = 1.0d;
		this.morale = morale;
	}

	public boolean isAttackCapable(AttackType type) {
		boolean result = false;
		for (Attack attack : model.computeAttacks()) {
			if (attack.getType().equals(type)) {
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public MobilitySet getMobilities() {
		MobilitySet result = new MobilitySet();
		result.add(this.getModel().getMobility());
		return result;
	}

	public double computeInitiative() {
		return this.getModel().getInitiative()
				* Math.pow(this.getExperience(), INITIATIVE_EXPERIENCE_EXPONENT)
				* Math.pow(this.getOrganization(), INITIATIVE_ORGANIZATION_EXPONENT);
	}

	@Override
	public String toString() {
		return code;
	}

	public String getId() {
		String result = parent.getId();
		result += "/" + this.getCode();
		return result;
	}

	@Override
	public AttackSet computeAttacks() {
		AttackSet result = new AttackSet();
		for (Attack attack : this.getModel().computeAttacks()) {
			double adjustedStrength = attack.getStrength() * Math.pow(this.getExperience(), attack.getType().getExperienceExponent())
					* Math.pow(this.getOrganization(), attack.getType().getOrganizationExponent());
			Attack adjustedAttack = new Attack(attack.getType(), attack.getRange(), adjustedStrength);
			Stock adjustedStock = new Stock();
			for (SupplyType supplyType : attack.getConsumption().keySet()) {
				adjustedStock.put(supplyType, attack.getConsumption().get(supplyType) * this.getStrength());
			}
			adjustedAttack.setConsumption(adjustedStock);
			result.add(adjustedAttack);
		}
		return result;
	}

	public List<Defense> computeDefenses() {
		List<Defense> result = new ArrayList<Defense>();
		for (Defense defense : this.getModel().computeDefenses()) {
			double adjustedStrength = defense.getStrength() * Math.pow(this.getExperience(), defense.getType().getExperienceExponent())
					* Math.pow(this.getOrganization(), defense.getType().getOrganizationExponent());
			result.add(new Defense(defense.getType(), adjustedStrength));
		}
		return result;
	}

	public Stock getMaxSupplies() {
		// TODO esto es en caso de la infanter�a, gestionar el uso de transportes
		Stock result = new Stock();
		for (SupplyType type : this.getModel().getMaxSupplies().keySet()) {
			double amount = this.getModel().getMaxSupplies().get(type) * this.getStrength();
			result.put(type, amount);
		}
		return result;
	}

	public Country getCountry() {
		return parent.getCountry();
	}

	public Weaponry getWeaponry() {
		Weaponry result = new Weaponry();
		for (WeaponModel weaponModel : this.getModel().getWeaponry().keySet()) {
			result.put(weaponModel, (int) (this.getModel().getWeaponry().get(weaponModel) * this.getStrength()));
		}
		return result;
	}

	public Position getPosition() {
		return this.getParent().getPosition();
	}

	public void increaseStrength(double strength) {
		this.strength += strength;
	}

	/* getters & setters */

	public CompanyModel getModel() {
		return model;
	}

	public void setModel(CompanyModel model) {
		this.model = model;
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}

	public double getOrganization() {
		return organization;
	}

	public void setOrganization(double organization) {
		this.organization = organization;
	}

	public Stock getSupplies() {
		return supplies;
	}

	public void setSupplies(Stock supplies) {
		this.supplies = supplies;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Formation getParent() {
		return parent;
	}

	public void setParent(Formation parent) {
		this.parent = parent;
	}

	public double getMorale() {
		return morale;
	}

	public void setMorale(double morale) {
		this.morale = morale;
	}

}

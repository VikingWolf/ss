package org.orion.ss.model.impl;

import org.orion.ss.model.ActivableImpl;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.core.AttackType;

public class Company extends ActivableImpl implements Mobile {

	private final static double INITIATIVE_EXPERIENCE_EXPONENT = 0.5d;
	private final static double INITIATIVE_ORGANIZATION_EXPONENT = 0.5d;
	
	private CompanyModel model;
	private Location location;
	private double strength;
	private double experience;
	private double organization;
	private Stock supplies;
	
	public Company(CompanyModel model, Location location, double strength, double experience, double organization) {
		super();
		supplies = new Stock();
		this.model = model;
		this.location = location;
		this.strength = strength;
		this.experience = experience;
		this.organization = organization;
	}

	public boolean isAttackCapable(AttackType type) {
		boolean result = false;
		for (Attack attack : model.getAttacks()) {
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
	
	public double computeInitiative(){
		return 
				this.getModel().getInitiative() 
				* Math.pow(this.getExperience(), INITIATIVE_EXPERIENCE_EXPONENT)
				* Math.pow(this.getOrganization(), INITIATIVE_ORGANIZATION_EXPONENT);
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

}

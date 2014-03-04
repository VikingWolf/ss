package org.orion.ss.model.impl;

import org.orion.ss.model.ActivableImpl;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.Unit;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.geo.Location;
import org.orion.ss.utils.FormationFormats;

public class Company extends ActivableImpl implements Mobile, Unit {

	private final static double INITIATIVE_EXPERIENCE_EXPONENT = 0.5d;
	private final static double INITIATIVE_ORGANIZATION_EXPONENT = 0.5d;

	private int id;
	private CompanyModel model;
	private Location location;
	private double strength;
	private double experience;
	private double organization;
	private double morale;
	private Stock supplies;
	private Formation parent;

	public Company(CompanyModel model){
		super();
		this.model = model;
		supplies = new Stock();
		this.strength = 0.0d;
		this.experience = 1.0d;
		this.organization = 1.0d;
		this.morale = 1.0d;
		this.supplies = new Stock();
	}
	
	public Company(Company company) {
		super();
		model = company.getModel();
		supplies = company.getSupplies();
		id = company.getId();
		strength = company.getStrength();
		experience = company.getExperience();
		organization = company.getOrganization();
		morale = company.getMorale();
	}

	public Company(CompanyModel model, int id, Location location, double experience, double morale, double strength) {
		super();
		this.model = model;
		supplies = new Stock();
		this.id = id;
		this.location = location;
		this.strength = strength;
		this.experience = experience;
		organization = 1.0d;
		this.morale = morale;
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
		return getName();
	}

	public String getFullName(){
		return FormationFormats.fullFormat(this);
	}
	
	public String getName() {
		return FormationFormats.format(this);
	}

	@Override
	public FormationLevel getFormationLevel() {
		return getModel().getFormationLevel();
	}

	public Stock getMaxSupplies() {
		// TODO esto es en caso de la infanter�a, gestionar el uso de transportes
		Stock result = new Stock();
		for (SupplyType type : this.getModel().getMaxSupplies().keySet()) {
			double amount = this.getModel().getMaxSupplies().get(type) * this.getStrength() * this.getModel().getMaxStrength();
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
	
	public int getAbsoluteStrength(){
		return (int) (this.getStrength() * (double) this.getModel().getMaxStrength());
	}
	
	public void resupply(){
		for (SupplyType type : this.getModel().getMaxSupplies().keySet()){
			double defect = this.getMaxSupplies().get(type) - this.getSupplies().get(type);
			this.getSupplies().put(type, defect);
		}
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

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

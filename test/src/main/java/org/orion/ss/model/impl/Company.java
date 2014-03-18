package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.CompanyTrait;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.geo.Location;
import org.orion.ss.service.GeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Company extends Unit {

	protected final static Logger logger = LoggerFactory.getLogger(Company.class);

	private final static double INITIATIVE_EXPERIENCE_EXPONENT = 0.5d;
	private final static double INITIATIVE_ORGANIZATION_EXPONENT = 0.5d;

	private CompanyModel model;
	private double strength;
	private double experience;
	private double organization;
	private double morale;
	private Stock supplies;
	private List<CompanyTrait> traits;
	private Location location = null;

	private Company() {
		super();
		supplies = new Stock();
		strength = 0.0d;
		experience = 1.0d;
		organization = 1.0d;
		morale = 1.0d;
		traits = new ArrayList<CompanyTrait>();

	}

	public Company(CompanyModel model) {
		this();
		this.model = model;
	}

	public Company(Company company) {
		this();
		model = company.getModel();
		supplies = company.getSupplies();
		setId(company.getId());
		strength = company.getStrength();
		experience = company.getExperience();
		organization = company.getOrganization();
		morale = company.getMorale();
	}

	public Company(CompanyModel model, int id, double experience, double morale, double strength) {
		this();
		this.model = model;
		setId(id);
		this.strength = strength;
		this.experience = experience;
		this.morale = morale;
	}

	@Override
	public MobilitySet getMobilities() {
		MobilitySet result = new MobilitySet();
		result.put(this.getModel().getMobility(), this.getModel().getSpeed());
		return result;
	}

	public double computeInitiative() {
		return this.getModel().getInitiative()
				* Math.pow(this.getExperience(), INITIATIVE_EXPERIENCE_EXPONENT)
				* Math.pow(this.getOrganization(), INITIATIVE_ORGANIZATION_EXPONENT);
	}

	@Override
	public String toString() {
		return getLongName();
	}

	@Override
	public FormationLevel getFormationLevel() {
		return FormationLevel.COMPANY;
	}

	public Stock getMaxSupplies() {
		// TODO esto es en caso de la infanteria, gestionar el uso de transportes
		Stock result = new Stock();
		for (SupplyType type : this.getModel().getMaxSupplies().keySet()) {
			double amount = this.getModel().getMaxSupplies().get(type) * this.getStrength() * this.getModel().getMaxStrength();
			result.put(type, amount);
		}
		return result;
	}

	@Override
	public Country getCountry() {
		return getParent().getCountry();
	}

	public Weaponry getWeaponry() {
		Weaponry result = new Weaponry();
		for (WeaponModel weaponModel : this.getModel().getWeaponry().keySet()) {
			result.put(weaponModel, (int) (this.getModel().getWeaponry().get(weaponModel) * this.getStrength()));
		}
		return result;
	}

	@Override
	public Position getPosition() {
		return this.getParent().getPosition();
	}

	public void increaseStrength(double strength) {
		this.strength += strength;
	}

	public int getAbsoluteStrength() {
		return (int) (this.getStrength() * this.getModel().getMaxStrength());
	}

	public void resupply() {
		for (SupplyType type : this.getModel().getMaxSupplies().keySet()) {
			double defect = this.getMaxSupplies().get(type) - this.getSupplies().get(type);
			this.getSupplies().put(type, defect);
		}
	}

	@Override
	public int stackSize() {
		return 1;
	}

	@Override
	public TroopType getTroopType() {
		return this.getModel().getType();
	}

	public boolean isHQ() {
		return getId() == 0;
	}

	@Override
	public double getSpotCapacity() {
		// TODO Auto-generated method stub
		return GeoService.BASE_SPOTTING;
	}

	@Override
	public boolean isDetachable() {
		return !this.isHQ() && !this.isDetached();
	}

	/* getters & setters */
	@Override
	public void setId(int id) {
		super.setId(id);
		if (id == 0) {
			traits.add(CompanyTrait.HQ);
		}
	}

	public CompanyModel getModel() {
		return model;
	}

	public List<CompanyTrait> getTraits() {
		return traits;
	}

	public void setTraits(List<CompanyTrait> traits) {
		this.traits = traits;
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

	public double getMorale() {
		return morale;
	}

	public void setMorale(double morale) {
		this.morale = morale;
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

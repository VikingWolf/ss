package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.ActivableImpl;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.Unit;
import org.orion.ss.model.core.CompanyTrait;
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
	private List<CompanyTrait> traits;

	private Company() {
		super();
		supplies = new Stock();
		location = new Location();
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

	@Override
	public String getFullName() {
		return FormationFormats.fullFormat(this);
	}

	public String getName() {
		return FormationFormats.longFormat(this);
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

	@Override
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
	public Formation getParentFormation(FormationLevel level) {
		if (this.getParent() != null)
			return this.getParent().getParentFormation(level);
		else return null;
	}

	/* getters & setters */

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
		if (id == 0) {
			traits.add(CompanyTrait.HQ);
		}
	}

	@Override
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

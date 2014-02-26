package org.orion.ss.model.impl;

import org.orion.ss.model.ActivableImpl;
import org.orion.ss.model.Mobile;

public class Company extends ActivableImpl implements Mobile {

	private int id;
	private CompanyModel model;
	private double strength;
	private byte experience;
	private double organization;
	private Stock supplies;
	private Location location;

	@Override
	public MobilitySet getMobilities() {
		MobilitySet result = new MobilitySet();
		result.add(this.getModel().getMobility());
		return result;
	}

	/* getters & setters */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public byte getExperience() {
		return experience;
	}

	public void setExperience(byte experience) {
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

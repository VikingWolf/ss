package org.orion.ss.model.impl;

import org.orion.ss.model.ActivableImpl;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.geo.Location;

public class AirSquadron extends ActivableImpl implements Mobile {

	private int id;
	private AirSquadronModel model;
	private double strength;
	private byte experience;
	private double organization;
	private Stock supplies;
	private Formation parent;
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

	public AirSquadronModel getModel() {
		return model;
	}

	public void setModel(AirSquadronModel model) {
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

	public Formation getParent() {
		return parent;
	}

	public void setParent(Formation parent) {
		this.parent = parent;
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

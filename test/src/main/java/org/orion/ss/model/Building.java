package org.orion.ss.model;

import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Country;

public abstract class Building implements Located, SpotCapable, SelfSupplier {

	private final static double BUILDING_DEFAULT_SPOTTING = 8.0d;

	private Country controller;
	private Location location;

	public Building(Location location, Country controller) {
		super();
		this.controller = controller;
		this.location = location;
	}

	@Override
	public double getSpotCapacity() {
		return BUILDING_DEFAULT_SPOTTING;
	}

	/* getters & setters */

	public Country getController() {
		return controller;
	}

	public void setController(Country controller) {
		this.controller = controller;
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

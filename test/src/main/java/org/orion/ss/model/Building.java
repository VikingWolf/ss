package org.orion.ss.model;

import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Country;
import org.orion.ss.service.GeoService;

public abstract class Building implements Localizable, SpotCapable {

	private Country controller;
	private Location location;

	public Building(Location location, Country controller) {
		super();
		this.controller = controller;
		this.location = location;
	}

	@Override
	public double getSpotCapacity() {
		// TODO Auto-generated method stub
		return GeoService.BASE_SPOTTING;
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

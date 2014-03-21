package org.orion.ss.model.geo;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Building;
import org.orion.ss.model.ZOCProjecter;
import org.orion.ss.model.impl.Country;

public class Fortification extends Building implements Activable, ZOCProjecter {

	private final static int DEFAULT_URBAN_ZOC_RADIUS = 8;

	private int strength;
	private final int size;
	private double status;

	public Fortification(int strength, int size, Location location, Country controller) {
		super(location, controller);
		this.strength = strength;
		this.size = size;
		status = 1.00d;

	}

	@Override
	public boolean isActivable() {
		// TODO
		return false;
	}

	@Override
	public String toString() {
		return "Fortification";
	}

	@Override
	public int getZOCRadius() {
		return DEFAULT_URBAN_ZOC_RADIUS;
	}

	/* getters & setters */

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public double getStatus() {
		return status;
	}

	public void setStatus(double status) {
		this.status = status;
	}

	public int getSize() {
		return size;
	}

}

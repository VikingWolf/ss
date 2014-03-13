package org.orion.ss.model.geo;

import org.orion.ss.model.Airbase;
import org.orion.ss.model.Building;
import org.orion.ss.model.impl.Country;

public class Airfield extends Building implements Airbase {

	private int capacity;

	public Airfield(int capacity, Location location, Country controller) {
		super(location, controller);
		this.capacity = capacity;
	}

	/* getters & setters */

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}

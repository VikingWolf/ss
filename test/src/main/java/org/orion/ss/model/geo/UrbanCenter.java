package org.orion.ss.model.geo;

import org.orion.ss.model.Building;
import org.orion.ss.model.MovementSupplier;
import org.orion.ss.model.ZOCProjecter;
import org.orion.ss.model.impl.Country;

public class UrbanCenter extends Building implements MovementSupplier, ZOCProjecter {

	private final static double DEFAULT_URBAN_SUPPLY_RANGE = 2.0;
	private final static int DEFAULT_URBAN_ZOC_RADIUS = 2;

	private String name;
	private int value;
	private int pop;

	public UrbanCenter(String name, int value, int pop, Location location, Country controller) {
		super(location, controller);
		this.name = name;
		this.value = value;
		this.pop = pop;
	}

	/* getters & setters */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getPop() {
		return pop;
	}

	public void setPop(int pop) {
		this.pop = pop;
	}

	@Override
	public double getSpupplyRange() {
		return DEFAULT_URBAN_SUPPLY_RANGE;
	}

	@Override
	public int getZOCRadius() {
		return DEFAULT_URBAN_ZOC_RADIUS;
	}

}

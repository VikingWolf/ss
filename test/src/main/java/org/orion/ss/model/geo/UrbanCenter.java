package org.orion.ss.model.geo;

import org.orion.ss.model.Building;
import org.orion.ss.model.ZOCProjecter;
import org.orion.ss.model.impl.Country;

public class UrbanCenter extends Building implements ZOCProjecter {

	private final static int DEFAULT_URBAN_ZOC_RADIUS = 8;

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
	public int getZOCRadius() {
		return DEFAULT_URBAN_ZOC_RADIUS;
	}

}

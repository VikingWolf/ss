package org.orion.ss.model.geo;

import java.util.HashMap;
import java.util.Map;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Building;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Country;

public class Fortification extends Building implements Activable {

	private int strength;
	private double state;
	private Map<Company, Integer> garrison;

	public Fortification(int strength, Location location, Country controller) {
		super(location, controller);
		this.strength = strength;
		state = 1.00d;
		garrison = new HashMap<Company, Integer>();
	}

	@Override
	public boolean isActivable() {
		// TODO
		return false;
	}

	/* getters & setters */

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public double getState() {
		return state;
	}

	public void setState(double state) {
		this.state = state;
	}

	public Map<Company, Integer> getGarrison() {
		return garrison;
	}

	public void setGarrison(Map<Company, Integer> garrison) {
		this.garrison = garrison;
	}

}

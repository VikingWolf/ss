package org.orion.ss.model.impl;

import java.util.ArrayList;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.SpotCapable;
import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Location;
import org.orion.ss.service.GeoService;

public class UnitStack extends ArrayList<Unit> implements Activable, Mobile, SpotCapable {

	private static final long serialVersionUID = -4815492861835612058L;

	private Location location;

	private transient boolean hasBeenActivated = false;

	public UnitStack(Location location) {
		super();
		this.location = location;
	}

	@Override
	public boolean isActivable() {
		return !hasBeenActivated;
	}

	@Override
	public MobilitySet getMobilities() {
		MobilitySet result = new MobilitySet();
		for (Unit unit : this) {
			result.putAll(unit.getMobilities());
		}
		return result;
	}

	public boolean containsAnyElementOf(Unit unit) {
		boolean result = false;
		for (Unit candidate : this) {
			if (unit.equals(candidate)) {
				result = true;
				break;
			} else if (candidate instanceof Formation) {
				Formation formation = (Formation) candidate;
				if (formation.getHQCompany().equals(unit)) {
					result = true;
					break;
				}
			}
			if (unit.isAnyParentOf(candidate)) {
				result = true;
				break;
			}

		}
		return result;
	}

	@Override
	public double getSpotCapacity() {
		double max = Double.NEGATIVE_INFINITY;
		for (Unit unit : this) {
			double spot = unit.getSpotCapacity();
			if (spot > max) {
				max = spot;
			}
		}
		return Math.min(GeoService.MAX_SPOTTING, max);
	}

	public Unit[] getArray() {
		Unit[] result = new Unit[this.size()];
		for (int i = 0; i < this.size(); i++) {
			result[i] = this.get(i);
		}
		return result;
	}

	/* getters and setters */
	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public double getSpentMovement() {
		double max = 0;
		for (Unit unit : this) {
			if (unit.getSpentMovement() > max) {
				max = unit.getSpentMovement();
			}
		}
		return max;
	}

}

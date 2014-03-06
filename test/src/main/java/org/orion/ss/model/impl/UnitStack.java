package org.orion.ss.model.impl;

import java.util.ArrayList;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Location;

public class UnitStack extends ArrayList<Unit> implements Activable, Mobile {

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
			result.addAll(unit.getMobilities());
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

}
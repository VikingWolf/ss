package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Game;

public class GeoService extends Service {

	public GeoService(Game game) {
		super(game);
	}

	public HexSet fullMap() {
		HexSet result = new HexSet();
		for (Hex hex : getGame().getMap()) {
			result.add(hex);
		}
		return result;
	}

	public List<Location> getDeployArea(Unit unit) {
		List<Location> result = new ArrayList<Location>();
		for (Location candidate : unit.getPosition().getDeployArea()) {
			int stackSize = getStackSizeAt(candidate);
			if (getGame().getSettings().getStackLimit() >= stackSize + unit.stackSize()) {
				result.add(candidate);
			}
		}
		return result;

	}

	protected int getStackSizeAt(Location location) {
		int result = 0;
		for (Company company : getGame().getAllCompanies()) {
			if (company.getLocation() != null) {
				if (company.getLocation().equals(location)) {
					result++;
				}
			}
		}
		return result;
	}

	private boolean canBeDeployedAt(Unit unit, Location location) {
		return getDeployArea(unit).contains(location);
	}

	public boolean deploy(Unit unit, Location location) {
		boolean result = false;
		if (canBeDeployedAt(unit, location)) {
			unit.setLocation(location);
			getGame().getLog().addEntry(unit.getFullName() + " has been deployed at (" + location.getX() + "," + location.getY() + ")");
			result = true;
		}
		return result;
	}
}
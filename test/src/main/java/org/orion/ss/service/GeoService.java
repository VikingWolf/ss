package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.UnitStack;

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

	public UnitStack getStackAt(Location location) {
		UnitStack result = new UnitStack(location);
		for (Company company : getGame().getAllCompanies()) {
			if (company.getLocation() != null) {
				if (company.getLocation().equals(location)) {
					result.add(company);
				}
			}
		}
		return result;

	}

	protected int getStackSizeAt(Location location) {
		return getStackAt(location).size();
	}

	private boolean canBeDeployedAt(Unit unit, Location location) {
		return getDeployArea(unit).contains(location);
	}

	public boolean deploy(Unit unit, Location location) {
		boolean result = false;
		if (canBeDeployedAt(unit, location)) {
			unit.setLocation(location);
			getGame().getLog().addEntry(unit.getFullLongName() + " has been deployed at (" + location.getX() + "," + location.getY() + ")");
			result = true;
		}
		return result;
	}

	public List<Unit> undeployedUnits(Position position) {
		List<Unit> result = new ArrayList<Unit>();
		for (Company company : position.getAllCompanies()) {
			if (company.getLocation().equals(new Location(-1, -1))) {
				result.add(company);
			}
		}
		return result;
	}
}

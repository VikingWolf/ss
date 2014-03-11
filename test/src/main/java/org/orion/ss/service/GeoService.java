package org.orion.ss.service;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.model.impl.UnitStack;

public class GeoService extends Service {

	protected GeoService(Game game) {
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
		for (Formation formation : getGame().getPositions().values()) {
			addFormationToStackAtLocation(formation, location, result);
		}
		return result;

	}

	private void addFormationToStackAtLocation(Formation formation, Location location, UnitStack stack) {
		if (formation.getLocation() != null && formation.getLocation().equals(location)) {
			stack.add(formation);
		} else {
			for (Company company : formation.getCompanies()) {
				if ((company.getLocation() != null) && (company.getLocation().equals(location)))
					stack.add(company);
			}
			for (Formation subordinate : formation.getSubordinates()) {
				addFormationToStackAtLocation(subordinate, location, stack);
			}
		}

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
			if (company.getLocation() == null) {
				result.add(company);
			}
		}
		return result;
	}

	public Map<Location, UnitStack> getAllUnitsLocatedAt(Rectangle bounds) {
		Map<Location, UnitStack> result = new HashMap<Location, UnitStack>();
		for (int i = (int) bounds.getX(); i <= (int) (bounds.getX() + bounds.getWidth()); i++) {
			for (int j = (int) bounds.getY(); j <= (int) (bounds.getY() + bounds.getHeight()); j++) {
				UnitStack stack = this.getStackAt(new Location(i, j));
				if (stack.size() > 0)
					result.put(new Location(i, j), stack);
			}
		}
		return result;
	}

	public Map<Location, Stock> getStocksLocatedAt(Rectangle bounds, Position position) {
		Map<Location, Stock> result = new HashMap<Location, Stock>();
		for (int i = (int) bounds.getX(); i <= (int) (bounds.getX() + bounds.getWidth()); i++) {
			for (int j = (int) bounds.getY(); j <= (int) (bounds.getY() + bounds.getHeight()); j++) {
				Stock stock = position.getStockAt(new Location(i, j));
				if (stock != null)
					result.put(new Location(i, j), stock);
			}
		}
		return result;
	}

	public Hex getHexAt(Location coords) {
		return getGame().getMap().getHexAt(coords);
	}

}

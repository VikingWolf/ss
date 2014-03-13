package org.orion.ss.service;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orion.ss.model.Building;
import org.orion.ss.model.SpotCapable;
import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.GeoMap;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.HexSide;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.OrientedLocation;
import org.orion.ss.model.geo.River;
import org.orion.ss.model.geo.Road;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.model.impl.UnitStack;

public class GeoService extends Service {

	public final static double BASE_SPOTTING = 2.0d;
	/* para evitar que las unidades que están fuera de la porción de mapa visible pierdan su visibilidad dentro de el */
	public final static int MAX_SPOTTING = 10;

	protected GeoService(Game game) {
		super(game);
	}

	public GeoMap getMap() {
		return getGame().getMap();
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
		if (unit != null) {
			for (Location candidate : unit.getPosition().getDeployArea()) {
				int stackSize = getStackSizeAt(candidate);
				if (getGame().getSettings().getStackLimit() >= stackSize + unit.stackSize()) {
					result.add(candidate);
				}
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

	public List<River> getRiversOf(Location location) {
		List<River> rivers = new ArrayList<River>();
		for (River river : this.getMap().getRivers()) {
			for (OrientedLocation candidate : river.getLocations()) {
				if ((candidate.getX() == location.getX()) && (candidate.getY() == location.getY())) {
					rivers.add(river);
				}
			}
		}
		return rivers;
	}

	public List<River> getRiversAt(Rectangle bounds) {
		List<River> result = new ArrayList<River>();
		for (River river : this.getMap().getRivers()) {
			if (river.flowsBy(bounds)) {
				result.add(river);
			}
		}
		return result;
	}

	public Map<Location, List<Road>> getRoadsAt(Rectangle bounds) {
		Map<Location, List<Road>> roads = new HashMap<Location, List<Road>>();
		for (Road road : this.getMap().getRoads()) {
			if (road.getLocation().getX() >= bounds.getX()
					&& road.getLocation().getX() <= bounds.getX() + bounds.getWidth()
					&& road.getLocation().getY() >= bounds.getY()
					&& road.getLocation().getY() <= bounds.getY() + bounds.getHeight()) {
				Location location = new Location(road.getLocation().getX(), road.getLocation().getY());
				List<Road> list = roads.get(location);
				if (list == null) {
					list = new ArrayList<Road>();
				}
				list.add(road);
				roads.put(location, list);
			}
		}
		return roads;
	}

	public Map<Location, Building> getBuildingsAt(Rectangle bounds) {
		Map<Location, Building> result = new HashMap<Location, Building>();
		for (Location location : getGame().getMap().getBuildings().keySet()) {
			if (location.getX() >= bounds.getX()
					&& location.getX() <= bounds.getX() + bounds.getWidth()
					&& location.getY() >= bounds.getY()
					&& location.getY() <= bounds.getY() + bounds.getHeight()) {
				result.put(location, getGame().getMap().getBuildings().get(location));
			}
		}
		return result;
	}

	public Building getBuildingAt(Location location) {
		return getGame().getMap().getBuildings().get(location);
	}

	public Hex getHexAt(Location coords) {
		return getGame().getMap().getHexAt(coords);
	}

	private Rectangle expandViewVisibilityBounds(Rectangle bounds) {
		int x = (int) Math.max(0, bounds.getX() - MAX_SPOTTING);
		int y = (int) Math.max(0, bounds.getY() - MAX_SPOTTING);
		int w = (int) Math.min(getMap().getRows(), bounds.getWidth() + MAX_SPOTTING);
		int h = (int) Math.min(getMap().getColumns(), bounds.getHeight() + MAX_SPOTTING);
		Rectangle result = new Rectangle(x, y, w, h);
		return result;
	}

	public HexSet getVisibleArea(Position position, Rectangle bounds) {
		HexSet result = new HexSet();
		for (Building building : this.getBuildingsAt(expandViewVisibilityBounds(bounds)).values()) {
			if (building.getController().equals(position.getCountry())) {
				result.add(getVisibleArea(building));
			}
		}
		for (UnitStack stack : this.getAllUnitsLocatedAt(expandViewVisibilityBounds(bounds)).values()) {
			result.add(getVisibleArea(stack));
		}
		return result;
	}

	protected double computeVisibility(SpotCapable spotCapable) {
		// TODO
		return spotCapable.getSpotCapacity();
	}

	protected double computeSpot(Hex hex) {
		return 1.0d;
	}

	public HexSet getVisibleArea(SpotCapable spotter) {
		HexSet result = new HexSet();
		if (spotter.getLocation() != null) {
			recursiveGetAreaByVisibility(this.getHexAt(spotter.getLocation()), result, computeVisibility(spotter));
		}
		return result;
	}

	private void recursiveGetAreaByVisibility(Hex hex, HexSet result, double visibility) {
		if (hex != null) {
			if (computeSpot(hex) <= visibility) {
				result.add(hex);
				for (HexSide side : HexSide.values()) {
					Hex adjacent = this.getHexAt(side.getAdjacent(hex.getCoords()));
					recursiveGetAreaByVisibility(adjacent, result, visibility - computeSpot(hex));
				}
			}
		}
	}

}

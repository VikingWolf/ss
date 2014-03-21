package org.orion.ss.service;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orion.ss.model.Building;
import org.orion.ss.model.ZOCProjecter;
import org.orion.ss.model.core.PositionRole;
import org.orion.ss.model.geo.Bridge;
import org.orion.ss.model.geo.GeoMap;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.HexSide;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.MultiLocatedFeature;
import org.orion.ss.model.geo.Railway;
import org.orion.ss.model.geo.River;
import org.orion.ss.model.geo.Road;
import org.orion.ss.model.impl.Country;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;

public class GeoService extends Service {

	protected GeoService(Game game) {
		super(game);
	}

	/* terrain features */
	public List<River> getRiversOf(Location location) {
		return getMultiLocatedFeaturesOf(location, getMap().getRivers());
	}

	public List<River> getRiversOf(Rectangle rectangle) {
		return getMultiLocatedFeaturesOf(rectangle, getMap().getRivers());
	}

	public List<Road> getRoadsOf(Location location) {
		return getMultiLocatedFeaturesOf(location, getMap().getRoads());
	}

	public List<Road> getRoadsOf(Rectangle rectangle) {
		return getMultiLocatedFeaturesOf(rectangle, getMap().getRoads());
	}

	public List<Railway> getRailwaysOf(Location location) {
		return getMultiLocatedFeaturesOf(location, getMap().getRailways());
	}

	public List<Railway> getRailwaysOf(Rectangle rectangle) {
		return getMultiLocatedFeaturesOf(rectangle, getMap().getRailways());
	}

	public List<Railway> getRailwaysOf(HexSet area) {
		return getMultiLocatedFeaturesOf(area, getMap().getRailways());
	}

	public List<Bridge> getBridgesOf(Location location) {
		return getMultiLocatedFeaturesOf(location, getMap().getBridges());
	}

	public List<Bridge> getBridgesOf(Rectangle rectangle) {
		return getMultiLocatedFeaturesOf(rectangle, getMap().getBridges());
	}

	protected <T extends MultiLocatedFeature> List<T> getMultiLocatedFeaturesOf(HexSet area, List<T> list) {
		List<T> result = new ArrayList<T>();
		for (T item : list) {
			for (Hex hex : area) {
				if (item.passesBy(hex.getCoords())) {
					if (!result.contains(item))
						result.add(item);
					break;
				}
			}
		}
		return result;
	}

	protected <T extends MultiLocatedFeature> List<T> getMultiLocatedFeaturesOf(Location location, List<T> list) {
		List<T> result = new ArrayList<T>();
		for (T item : list) {
			if (item.passesBy(location)) {
				if (!result.contains(item))
					result.add(item);
			}
		}
		return result;
	}

	protected <T extends MultiLocatedFeature> List<T> getMultiLocatedFeaturesOf(Rectangle rectangle, List<T> list) {
		List<T> result = new ArrayList<T>();
		for (T item : list) {
			if (item.passesBy(rectangle)) {
				if (!result.contains(item))
					result.add(item);
			}
		}
		return result;
	}

	/* buildings */

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

	public List<Building> getBuildingsOf(Country country) {
		List<Building> result = new ArrayList<Building>();
		for (Building building : getGame().getMap().getBuildings().values()) {
			if (building.getController().equals(country)) result.add(building);
		}
		return result;
	}

	/* ZOC */

	private HexSet getZOC(ZOCProjecter projecter) {
		HexSet result = new HexSet();
		if (projecter.getLocation() != null) {
			recursiveAreaByRadius(this.getHexAt(projecter.getLocation()), result, (int) (projecter.getZOCRadius() / getGame().getHexDistance()));
		}
		return result;
	}

	public HexSet getFullZOC(PositionRole role) {
		HexSet result = new HexSet();
		List<Country> countries = new ArrayList<Country>();
		for (Position position : getGame().getPositions().values()) {
			if (position.getRole() == role) {
				if (!countries.contains(position.getRole())) {
					countries.add(position.getCountry());
				}
			}
		}
		for (Country country : countries) {
			for (Building building : this.getBuildingsOf(country)) {
				if (building instanceof ZOCProjecter) {
					result.addAll(getZOC((ZOCProjecter) building));
				}
			}
		}
		//TODO units
		//TODO intersection of enemy zocs
		return result;
	}

	/* util */

	private void recursiveAreaByRadius(Hex hex, HexSet result, int radius) {
		if (hex != null) {
			if (radius > 0) {
				result.add(hex);
				for (HexSide side : HexSide.values()) {
					Hex adjacent = this.getHexAt(side.getAdjacent(hex.getCoords()));
					recursiveAreaByRadius(adjacent, result, radius - 1);
				}
			}
		}
	}

	protected double getDistance(Location first, Location second) {
		return Math.pow(Math.pow(first.getX() - second.getX(), 2) + Math.pow(first.getY() - second.getY(), 2), 0.5d);
	}

	public List<Location> getAdjacents(Location location) {
		List<Location> result = new ArrayList<Location>();
		if (location != null) {
			for (HexSide side : HexSide.values()) {
				result.add(side.getAdjacent(location));
			}
		}
		return result;
	}

	public HexSet fullMap() {
		HexSet result = new HexSet();
		for (Hex hex : getGame().getMap()) {
			result.add(hex);
		}
		return result;
	}

	public GeoMap getMap() {
		return getGame().getMap();
	}

	public Hex getHexAt(Location coords) {
		if (coords != null)
			return getGame().getMap().getHexAt(coords);
		else return null;
	}

	protected Hex getHexAt(Hex hex, HexSide side) {
		Location adjacent = side.getAdjacent(hex.getCoords());
		return getHexAt(adjacent);
	}

}

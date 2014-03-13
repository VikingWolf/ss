package org.orion.ss.model.geo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orion.ss.model.Building;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeoMap extends ArrayList<Hex> {

	private final static long serialVersionUID = -8390150342604230092L;
	protected final static Logger logger = LoggerFactory.getLogger("GameMap.class");

	private final List<River> rivers;
	private final List<Road> roads;
	private final int rows;
	private final int columns;
	private final Map<Location, Building> buildings;

	public GeoMap(int rows, int columns, Terrain defaultTerrain, Vegetation defaultVegetation) {
		super();
		rivers = new ArrayList<River>();
		roads = new ArrayList<Road>();
		buildings = new HashMap<Location, Building>();
		this.rows = rows;
		this.columns = columns;
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				Hex hex = new Hex(defaultTerrain, defaultVegetation);
				hex.setCoords(new Location(i, j));
				addOrReplace(hex);
			}
		}
	}

	public void setHexAt(Location coords, Hex hex) {
		hex.setCoords(coords);
		addOrReplace(hex);
	}

	private GeoMap addOrReplace(Hex hex) {
		if (!this.contains(hex)) {
			this.add(hex);
		} else {
			this.set(this.indexOf(hex), hex);
		}
		return this;
	}

	public Hex getHexAt(int x, int y) {
		return getHexAt(new Location(x, y));
	}

	public Hex getHexAt(Location location) {
		Hex candidate = new Hex();
		candidate.setCoords(location);
		if (this.contains(candidate))
			return this.get(this.indexOf(candidate));
		else return null;
	}

	public void addRoad(Road road) {
		roads.add(road);
	}

	public List<Road> getRoadsOf(int x, int y) {
		List<Road> roads = new ArrayList<Road>();
		for (Road road : this.roads) {
			if ((road.getLocation().getX() == x) && (road.getLocation().getY() == y)) {
				roads.add(road);
			}
		}
		return roads;
	}

	public List<Road> getRoadsOf(Point loc) {
		return getRoadsOf((int) loc.getX(), (int) loc.getY());
	}

	public double getDistance(Hex hex1, Hex hex2) {
		return getDistance(hex1.getCoords(), hex2.getCoords());
	}

	private double getDistance(Location point1, Location point2) {
		return Math.pow(Math.pow(point1.getX() - point2.getX(), 2)
				+ Math.pow(point1.getY() - point2.getY(), 2), 0.5);
	}

	public boolean isInsideMap(Hex loc) {
		if (loc.getCoords() != null) {
			return isInsideMap(loc.getCoords());
		} else return false;
	}

	public boolean isInsideMap(Location loc) {
		if ((loc.getX() <= 0) ||
				(loc.getY() <= 0) ||
				(loc.getX() >= getRows()) ||
				(loc.getY() >= getColumns()))
			return false;
		else return true;
	}

	/* adders */
	public void addBuilding(Building building) {
		buildings.put(building.getLocation(), building);
	}

	public void addRiver(River river) {
		rivers.add(river);
	}

	/* getters & setters */

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public Map<Location, Building> getBuildings() {
		return buildings;
	}

	public List<River> getRivers() {
		return rivers;
	}

	public List<Road> getRoads() {
		return roads;
	}

}

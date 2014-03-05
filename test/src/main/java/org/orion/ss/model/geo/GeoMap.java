package org.orion.ss.model.geo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeoMap extends ArrayList<Hex> {

	private final static long serialVersionUID = -8390150342604230092L;
	protected final static Logger logger = LoggerFactory.getLogger("GameMap.class");

	private final List<River> rivers;
	private final List<Road> roads;

	private final int rows;
	private final int columns;

	public GeoMap(int rows, int columns, Terrain defaultTerrain, Vegetation defaultVegetation) {
		super();
		rivers = new ArrayList<River>();
		roads = new ArrayList<Road>();
		this.rows = rows;
		this.columns = columns;
		for (int i = 0; i <= rows; i++) {
			for (int j = 0; j <= columns; j++) {
				Hex hex = new Hex(defaultTerrain, defaultVegetation);
				hex.setCoords(new Point(i, j));
				addOrReplace(hex);
			}
		}
	}

	public void setHexAt(Point coords, Hex hex) {
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
		return getHexAt(new Point(x, y));
	}

	public Hex getHexAt(Point location) {
		Hex candidate = new Hex();
		candidate.setCoords(location);
		if (this.contains(candidate))
			return this.get(this.indexOf(candidate));
		else return null;
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public List<River> getRiversOf(int x, int y) {
		List<River> rivers = new ArrayList<River>();
		for (River river : this.rivers) {
			for (HexSidedPoint loc : river.getLocs()) {
				if (((int) loc.getX() == x) && ((int) loc.getY() == y)) {
					rivers.add(river);
				}
			}
		}
		return rivers;
	}

	public List<River> getRiversOf(Point loc) {
		return getRiversOf((int) loc.getX(), (int) loc.getY());
	}

	public void addRiver(River river) {
		rivers.add(river);
	}

	public void addRoad(Road road) {
		roads.add(road);
	}

	public List<Road> getRoadsOf(int x, int y) {
		List<Road> roads = new ArrayList<Road>();
		for (Road road : this.roads) {
			for (HexSidedPoint loc : road.getLocs()) {
				if (((int) loc.getX() == x) && ((int) loc.getY() == y)) {
					roads.add(road);
				}
			}
		}
		return roads;
	}

	public List<Road> getRoadsOf(Point loc) {
		return getRoadsOf((int) loc.getX(), (int) loc.getY());
	}

	private boolean isInsideMap(Hex loc) {
		if (loc.getCoords() != null) {
			if ((loc.getCoords().getX() < 0) ||
					(loc.getCoords().getY() < 0) ||
					(loc.getCoords().getX() >= rows) ||
					(loc.getCoords().getY() >= columns))
				return false;
			else return true;
		} else return false;
	}

	public double getDistance(Hex hex1, Hex hex2) {
		return getDistance(hex1.getCoords(), hex2.getCoords());
	}

	private double getDistance(Point point1, Point point2) {
		return Math.pow(Math.pow(point1.getX() - point2.getX(), 2)
				+ Math.pow(point1.getY() - point2.getY(), 2), 0.5);
	}

	public List<Hex> getArea(Hex loc, int radius) {
		List<Hex> initial = new ArrayList<Hex>();
		initial.add(loc);
		List<Hex> result = new ArrayList<Hex>();
		recursiveGetArea(result, new ArrayList<Hex>(), initial, 0, radius);
		return result;
	}

	private void recursiveGetArea(List<Hex> result, List<Hex> checked, List<Hex> unchecked, int stage, int radius) {
		/* clear unchecked of last stage, do here to avoid concurrent modification */
		for (Hex node : checked) {
			unchecked.remove(node);
		}
		List<Hex> toAdd = new ArrayList<Hex>();
		for (Hex node : unchecked) {
			if (!checked.contains(node)) {
				if (isInsideMap(node)) {
					if (!result.contains(node)) {
						result.add(node);
					}
					for (HexSide side : HexSide.values()) {
						Hex adjacent = this.getHexAt(side.getAdjacent(node.getCoords()));
						toAdd.add(adjacent);
					}
				}
				checked.add(node);
			}
		}
		if (stage < radius) {
			unchecked.addAll(toAdd);
			recursiveGetArea(result, checked, unchecked, stage + 1, radius);
		}
	}

}

package org.orion.ss.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.orion.ss.model.Mobile;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.HexSide;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Mobility;
import org.orion.ss.model.impl.MovementPath;

public class MovementService extends Service {

	private final static double SUPPLY_AREA_EXPONENT = 2.0d;

	private final GeoService geoService;

	private transient MovementPath movementPath = null;

	public MovementService(Game game) {
		super(game);
		geoService = ServiceFactory.getGeoService(game);
	}

	public void resetMovementPath(Mobile mobile) {
		movementPath = new MovementPath(mobile);
	}

	public void addToMovementPath(Location location) {
		Location lastLocation = movementPath.getLastLocation();
		double cost = computeMovementCost(lastLocation, location, movementPath.getMobile());
		movementPath.push(location, cost);
	}

	public List<Location> getMovementPath() {
		return movementPath.getLocations();
	}

	public List<Location> getMoveOptions() {
		List<Location> result = new ArrayList<Location>();
		for (Location location : geoService.getAdjacents(movementPath.getLastLocation())) {
			double currentCost = computePathMovementCost(location);
			//logger.error("currentCost=" + currentCost + ", turn duration=" + getGame().getTurnDuration());
			if (currentCost < getGame().getTurnDuration()) {
				result.add(location);
			}
		}
		return result;
	}

	public double computePathMovementCost(Location location) {
		double cost = computeMovementCost(movementPath.getLastLocation(), location, movementPath.getMobile());
		return cost + movementPath.getTotalCost();
	}

	private double computeMovementCost(Location from, Location to, Mobile mobile) {
		double cost = Double.NEGATIVE_INFINITY;
		for (Mobility mobility : mobile.getMobilities()) {
			double candidate = computeMovementCost(geoService.getHexAt(from), geoService.getHexAt(to), mobility);
			/* escogemos el coste mas alto */
			if (candidate > cost) cost = candidate;
		}
		return cost;
	}

	protected double computeMovementCost(Hex from, Hex to, Mobility mobility) {
		//TODO en funcion del terreno, vegetacion, soil, speed
		double cost = 0.0d;
		if (!from.equals(to)) {
			cost = getGame().getHexDistance() / mobility.getSpeed();
		}
		return cost;
	}

	protected void recursiveAreaByMovement(Location source, Hex from, HexSide dir, HexClosedList closedList, HexSet result, double time, Mobility mobility) {
		Hex to = geoService.getHexAt(from, dir);
		if (to != null && !closedList.isClosed(to)) {
			closedList.add(to, dir);
			double cost = computeMovementCost(from, to, mobility) * Math.pow(geoService.getDistance(source, to.getCoords()), SUPPLY_AREA_EXPONENT);
			if (cost <= time) {
				result.add(to);
				for (HexSide side : HexSide.values()) {
					recursiveAreaByMovement(source, to, side, closedList, result, time - cost, mobility);
				}
			}
		}
	}
}

class HexClosedList extends HashMap<Hex, List<HexSide>> {

	private static final long serialVersionUID = 1750012930305733689L;

	public void add(Hex hex, HexSide side) {
		List<HexSide> sides = this.get(hex);
		if (sides == null) {
			sides = new ArrayList<HexSide>();
			sides.add(side);
		} else {
			if (!sides.contains(side)) {
				sides.add(side);
			}
		}
		this.put(hex, sides);
	}

	public boolean isClosed(Hex hex) {
		boolean result = false;
		if (this.get(hex) != null && this.get(hex).size() == 6) {
			result = true;
		}
		return result;
	}

}

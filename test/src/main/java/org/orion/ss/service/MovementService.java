package org.orion.ss.service;

import java.util.List;

import org.orion.ss.model.Mobile;
import org.orion.ss.model.core.Mobility;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.HexSide;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.MovementPath;

public class MovementService extends Service {

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
		movementPath.push(location, computeMovementCost(lastLocation, location, movementPath.getMobile()));
	}

	public List<Location> getMovementPath() {
		return movementPath.getLocations();
	}

	public List<Location> getMoveOptions() {
		return geoService.getAdjacents(movementPath.getLastLocation());
	}

	public double computePathMovementCost(Location location) {
		double cost = computeMovementCost(movementPath.getLastLocation(), location, movementPath.getMobile());
		return cost + movementPath.getTotalCost();
	}

	private double computeMovementCost(Location from, Location to, Mobile mobile) {
		//TODO escoger el mayor coste
		double cost = Double.NEGATIVE_INFINITY;
		for (Mobility mobility : mobile.getMobilities().keySet()) {
			double candidate = computeMovementCost(geoService.getHexAt(from), geoService.getHexAt(to), mobility);
			if (candidate > cost) cost = candidate;
		}
		return cost;
	}

	protected double computeMovementCost(Hex from, Hex to, Mobility mobility) {
		//TODO en funcion del terreno, vegetacion, hexside
		double cost = 0.0d;
		if (!from.equals(to)) {
			cost = getGame().getHexDistance();
		}
		return cost;
	}

	public HexSet getMoveArea(MovementPath path, Mobile mobile) {
		/* movement is exclusive, a stack cannot move to a location if any of his units cannot move */
		HexSet[] result = new HexSet[mobile.getMobilities().size()];
		int index = 0;
		for (Mobility mobility : mobile.getMobilities().keySet()) {
			Location location = mobile.getLocation();
			if (path != null && !path.isEmpty()) {
				location = path.getLastLocation();
			}
			double speed = mobile.getMobilities().get(mobility);
			result[index] = new HexSet();
			//TODO move capacity depends on spent time and turn duration
			recursiveAreaByMovement(geoService.getHexAt(location), geoService.getHexAt(mobile.getLocation()), result[index], speed, mobility);
			index++;
		}
		return HexSet.intersect(result);
	}

	protected void recursiveAreaByMovement(Hex from, Hex to, HexSet result, double capacity, Mobility mobility) {
		if (to != null) {
			double cost = computeMovementCost(from, to, mobility);
			if (cost <= capacity) {
				result.add(to);
				for (HexSide side : HexSide.values()) {
					Hex adjacent = geoService.getHexAt(side.getAdjacent(to.getCoords()));
					recursiveAreaByMovement(to, adjacent, result, capacity - cost, mobility);
				}
			}
		}
	}

}

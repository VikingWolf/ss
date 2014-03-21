package org.orion.ss.service;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orion.ss.model.Building;
import org.orion.ss.model.Located;
import org.orion.ss.model.MovementSupplier;
import org.orion.ss.model.Unit;
import org.orion.ss.model.core.PositionRole;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.HexSide;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.Railway;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Mobility;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.Stock;

public class SupplyService extends Service {

	private final GeoService geoService;
	private final MovementService movementService;

	public SupplyService(Game game) {
		super(game);
		geoService = ServiceFactory.getGeoService(game);
		movementService = ServiceFactory.getMovementService(game);
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

	public Map<Location, Stock> getStocksLocatedAt(List<Location> area, Position position) {
		Map<Location, Stock> result = new HashMap<Location, Stock>();
		for (Location location : area) {
			Stock stock = position.getStockAt(location);
			if (stock != null)
				result.put(location, stock);
		}
		return result;
	}

	public List<Location> getSupplyArea(Position position) {
		HexSet result = new HexSet();
		for (Location location : position.getSupplyArea()) {
			result.add(geoService.getHexAt(location));
		}
		List<MovementSupplier> movSuppliers = new ArrayList<MovementSupplier>();
		movSuppliers.add(position);
		for (Unit unit : position.getAllUnits()) {
			if (unit instanceof MovementSupplier) {
				movSuppliers.add((MovementSupplier) unit);
			}
		}
		for (MovementSupplier supplier : movSuppliers) {
			result.addAll(this.getSupplyArea(supplier));
		}
		for (Building building : geoService.getBuildingsOf(position.getCountry())) {
			result.add(geoService.getHexAt(building.getLocation()));
		}
		for (Railway railway : geoService.getRailwaysOf(result)) {
			//TODO interrupted railways
			for (Location location : railway.getLocations()) {
				result.add(geoService.getHexAt(location));
			}
		}
		//TODO railways enlazados con otros railways		
		HexSet zocNegation = geoService.getFullZOC(PositionRole.enemy(position.getRole()));
		result.removeAll(zocNegation);
		return result.getLocations();
	}

	private HexSet getSupplyArea(MovementSupplier supplier) {
		/* supply area is additive, if any unit of the stack can supply and hex, the hex is in supply */
		HexSet result = new HexSet();
		for (Mobility mobility : supplier.getMobilities()) {
			Hex hex = geoService.getHexAt(((Located) supplier).getLocation());
			HexClosedList closedList = new HexClosedList();
			for (HexSide side : HexSide.values()) {
				movementService.recursiveAreaByMovement(supplier.getLocation(), hex, side, closedList, result, getGame().getTurnDuration(), mobility);
			}

		}
		return result;
	}

	public Stock getNearestSupplySource(Unit unit) {
		Stock result = null;
		List<Location> supplyArea = this.getSupplyArea(unit.getPosition());
		//TODO ojo con las areas no conexas
		if (supplyArea.contains(unit.getLocation())) {
			Map<Location, Stock> candidates = this.getStocksLocatedAt(supplyArea, unit.getPosition());
			double minDistance = Double.MAX_VALUE;
			for (Location stockLocation : candidates.keySet()) {
				double distance = geoService.getDistance(unit.getLocation(), stockLocation);
				if (distance < minDistance) {
					distance = minDistance;
					result = candidates.get(stockLocation);
				}
			}
		}
		return result;
	}

}

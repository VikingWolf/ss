package org.orion.ss.service;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orion.ss.model.Building;
import org.orion.ss.model.Located;
import org.orion.ss.model.MovementSupplier;
import org.orion.ss.model.MultiLocated;
import org.orion.ss.model.RadiusSupplier;
import org.orion.ss.model.SpotCapable;
import org.orion.ss.model.Unit;
import org.orion.ss.model.ZOCProjecter;
import org.orion.ss.model.core.PositionRole;
import org.orion.ss.model.geo.Bridge;
import org.orion.ss.model.geo.GeoMap;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.HexSide;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.MultiLocatedFeature;
import org.orion.ss.model.geo.OrientedLocation;
import org.orion.ss.model.geo.Railway;
import org.orion.ss.model.geo.River;
import org.orion.ss.model.geo.Road;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Country;
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

	//TODO here zoc
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
					result.add(getZOC((ZOCProjecter) building));
				}
			}
		}
		//TODO units
		//TODO intersection of enemy zocs
		return result;
	}

	public List<Location> getDeployArea(Unit unit) {
		List<Location> result = new ArrayList<Location>();
		if (unit != null) {
			for (Location candidate : unit.getPosition().getDeployArea()) {
				int stackSize = getStackSizeAt(unit.getPosition(), candidate);
				if (getGame().getSettings().getStackLimit() >= stackSize + unit.stackSize()) {
					result.add(candidate);
				}
			}
		}
		return result;
	}

	public UnitStack getStackAt(Position position, Location location) {
		UnitStack result = new UnitStack(location);
		addFormationToStackAtLocation(position, location, result);
		return result;

	}

	private void addFormationToStackAtLocation(Formation formation, Location location, UnitStack stack) {
		if (formation.getLocation() != null && formation.getLocation().equals(location) && formation.isDetached()) {
			stack.add(formation);
		}
		for (Company company : formation.getCompanies()) {
			if (company.getLocation() != null && company.getLocation().equals(location) && company.isDetached())
				stack.add(company);
		}
		for (Formation subordinate : formation.getSubordinates()) {
			addFormationToStackAtLocation(subordinate, location, stack);
		}
	}

	protected int getStackSizeAt(Position position, Location location) {
		return getStackAt(position, location).size();
	}

	private boolean canBeDeployedAt(Unit unit, Location location) {
		return getDeployArea(unit).contains(location);
	}

	public boolean deploy(Unit unit, Location location) {
		boolean result = false;
		if (canBeDeployedAt(unit, location)) {
			unit.setLocation(location);
			if (unit.isDetachable() && unit.getParent().getLocation() != null && unit.getParent().getLocation().equals(location)) {
				unit.setDetached(false);
			} else {
				unit.setDetached(true);
			}
			getGame().getLog().addEntry(unit.getFullLongName() + " has been deployed at (" + location.getX() + "," + location.getY() + ")");
			result = true;
		}
		return result;
	}

	public Map<Location, UnitStack> getAllUnitsLocatedAt(Position position, Rectangle bounds) {
		Map<Location, UnitStack> result = new HashMap<Location, UnitStack>();
		for (int i = (int) bounds.getX(); i <= (int) (bounds.getX() + bounds.getWidth()); i++) {
			for (int j = (int) bounds.getY(); j <= (int) (bounds.getY() + bounds.getHeight()); j++) {
				UnitStack stack = this.getStackAt(position, new Location(i, j));
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
		if (coords != null)
			return getGame().getMap().getHexAt(coords);
		else return null;
	}

	public List<Building> getBuildingsOf(Country country) {
		List<Building> result = new ArrayList<Building>();
		for (Building building : getGame().getMap().getBuildings().values()) {
			if (building.getController().equals(country)) result.add(building);
		}
		return result;
	}

	private Rectangle expandViewVisibilityBounds(Rectangle bounds) {
		int x = (int) Math.max(0, bounds.getX() - MAX_SPOTTING);
		int y = (int) Math.max(0, bounds.getY() - MAX_SPOTTING);
		int w = (int) Math.min(getMap().getColumns(), bounds.getHeight() + MAX_SPOTTING);
		int h = (int) Math.min(getMap().getRows(), bounds.getWidth() + MAX_SPOTTING);
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
		for (UnitStack stack : this.getAllUnitsLocatedAt(position, expandViewVisibilityBounds(bounds)).values()) {
			result.add(getVisibleArea(stack));
		}
		return result;
	}

	public List<Location> getSupplyArea(Position position) {
		HexSet result = new HexSet();
		List<MovementSupplier> suppliers = new ArrayList<MovementSupplier>();
		for (Building building : this.getBuildingsOf(position.getCountry())) {
			if (building instanceof MovementSupplier) {
				suppliers.add((MovementSupplier) building);
			}
		}
		suppliers.add(position);
		for (Unit unit : position.getAllUnits()) {
			if (unit instanceof MovementSupplier) {
				suppliers.add((MovementSupplier) unit);
			}
		}
		for (MovementSupplier supplier : suppliers) {
			result.add(this.getSupplyArea(supplier));
		}
		for (Railway railway : this.getRailwaysOf(result)) {
			result.add(this.getSupplyArea(railway));
		}
		//TODO railways enlazados con otros railways		
		HexSet zocNegation = getFullZOC(PositionRole.enemy(position.getRole()));
		result.removeAll(zocNegation);
		return result.getLocations();
	}

	protected double computeVisibility(SpotCapable spotCapable) {
		return spotCapable.getSpotCapacity();
	}

	protected double computeSpotCost(Hex hex) {
		//TODO en funcion del terreno y vegetacion
		return 1.0d;
	}

	protected double computeMovementCost(Hex hex) {
		//TODO en funcion del terreno y vegetacion
		return 1.0d;
	}

	public HexSet getSupplyArea(RadiusSupplier supplier) {
		HexSet result = new HexSet();
		if (supplier instanceof Located) {
			recursiveAreaByRadius(this.getHexAt(((Located) supplier).getLocation()), result, supplier.getSpupplyRadius());
		} else if (supplier instanceof MultiLocated) {
			for (OrientedLocation location : ((MultiLocated) supplier).getLocations()) {
				recursiveAreaByRadius(this.getHexAt(location), result, supplier.getSpupplyRadius());
			}
		}
		return result;
	}

	public HexSet getSupplyArea(MovementSupplier supplier) {
		HexSet result = new HexSet();
		if (supplier instanceof Located) {
			recursiveAreaByMovement(this.getHexAt(((Located) supplier).getLocation()), result, supplier.getSpupplyRange());
		} else if (supplier instanceof MultiLocated) {
			for (OrientedLocation location : ((MultiLocated) supplier).getLocations()) {
				recursiveAreaByMovement(this.getHexAt(location), result, supplier.getSpupplyRange());
			}
		}
		return result;
	}

	public HexSet getVisibleArea(SpotCapable spotter) {
		HexSet result = new HexSet();
		if (spotter.getLocation() != null) {
			recursiveAreaByVisibility(this.getHexAt(spotter.getLocation()), result, computeVisibility(spotter));
		}
		return result;
	}

	private HexSet getZOC(ZOCProjecter projecter) {
		HexSet result = new HexSet();
		if (projecter.getLocation() != null) {
			recursiveAreaByRadius(this.getHexAt(projecter.getLocation()), result, projecter.getZOCRadius());
		}
		return result;
	}

	private void recursiveAreaByVisibility(Hex hex, HexSet result, double visibility) {
		if (hex != null) {
			if (computeSpotCost(hex) <= visibility) {
				result.add(hex);
				for (HexSide side : HexSide.values()) {
					Hex adjacent = this.getHexAt(side.getAdjacent(hex.getCoords()));
					recursiveAreaByVisibility(adjacent, result, visibility - computeSpotCost(hex));
				}
			}
		}
	}

	private void recursiveAreaByMovement(Hex hex, HexSet result, double capacity) {
		if (hex != null) {
			if (computeMovementCost(hex) <= capacity) {
				result.add(hex);
				for (HexSide side : HexSide.values()) {
					Hex adjacent = this.getHexAt(side.getAdjacent(hex.getCoords()));
					recursiveAreaByMovement(adjacent, result, capacity - computeMovementCost(hex));
				}
			}
		}
	}

	private void recursiveAreaByRadius(Hex hex, HexSet result, int radius) {
		if (hex != null) {
			if (computeMovementCost(hex) <= radius) {
				result.add(hex);
				for (HexSide side : HexSide.values()) {
					Hex adjacent = this.getHexAt(side.getAdjacent(hex.getCoords()));
					recursiveAreaByRadius(adjacent, result, radius - 1);
				}
			}
		}
	}

}

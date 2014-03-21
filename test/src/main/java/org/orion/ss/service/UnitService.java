package org.orion.ss.service;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orion.ss.model.Building;
import org.orion.ss.model.SpotCapable;
import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Fortification;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.HexSide;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Country;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.UnitStack;

public class UnitService extends Service {

	private final GeoService geoService;

	protected UnitService(Game game) {
		super(game);
		geoService = ServiceFactory.getGeoService(game);
	}

	/* unit stacks */

	public UnitStack getStackAt(Position position, Location location) {
		UnitStack result = new UnitStack(location);
		addFormationToStackAtLocation(position, location, result);
		return result;

	}

	public UnitStack getStackAt(Country country, Location location) {
		UnitStack result = new UnitStack(location);
		for (Position position : getGame().getPositions().values()) {
			if (position.getCountry().equals(country)) {
				addFormationToStackAtLocation(position, location, result);
			}
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

	private int getStackSizeAt(Position position, Location location) {
		return getStackAt(position, location).size();
	}

	/* deployment */
	public List<Location> getDeployArea(Position position, int size) {
		List<Location> result = new ArrayList<Location>();
		for (Location candidate : position.getDeployArea()) {
			int stackSize = getStackSizeAt(position, candidate);
			if (getGame().getSettings().getStackLimit() >= stackSize + size) {
				result.add(candidate);
			}
		}
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

	/* garrisons */
	public boolean canGarrison(Unit unit) {
		boolean result = false;
		Building building = geoService.getBuildingAt(unit.getLocation());
		if (building instanceof Fortification && ((Fortification) building).getController().equals(unit.getCountry())) {
			Fortification fortification = (Fortification) building;
			if (fortification.getSize() >= unit.stackSize() + garrisonAt(fortification).size()) {
				result = true;
			}
		}
		return result;
	}

	private UnitStack garrisonAt(Fortification fortification) {
		UnitStack stack = new UnitStack(fortification.getLocation());
		for (Unit unit : this.getStackAt(fortification.getController(), fortification.getLocation())) {
			if (unit.isGarrison()) stack.add(unit);
		}
		return stack;
	}

	/* spotting */
	public HexSet getVisibleArea(Position position, Rectangle bounds) {
		HexSet result = new HexSet();
		for (Building building : geoService.getBuildingsAt(expandViewVisibilityBounds(bounds)).values()) {
			if (building.getController().equals(position.getCountry())) {
				result.addAll(getVisibleArea(building));
			}
		}
		for (UnitStack stack : getAllUnitsLocatedAt(position, expandViewVisibilityBounds(bounds)).values()) {
			result.addAll(getVisibleArea(stack));
		}
		return result;
	}

	private Rectangle expandViewVisibilityBounds(Rectangle bounds) {
		int x = (int) Math.max(0, bounds.getX() - getGame().getMaxSpotting());
		int y = (int) Math.max(0, bounds.getY() - getGame().getMaxSpotting());
		int w = (int) Math.min(geoService.getMap().getColumns(), bounds.getHeight() + getGame().getMaxSpotting());
		int h = (int) Math.min(geoService.getMap().getRows(), bounds.getWidth() + getGame().getMaxSpotting());
		Rectangle result = new Rectangle(x, y, w, h);
		return result;
	}

	protected double computeSpotCost(Hex hex) {
		//TODO en funcion del terreno, vegetacion, hexside
		return getGame().getHexDistance();
	}

	public HexSet getVisibleArea(SpotCapable spotter) {
		HexSet result = new HexSet();
		if (spotter.getLocation() != null) {
			recursiveAreaByVisibility(geoService.getHexAt(spotter.getLocation()), result, getSpotCapacity(spotter));
		}
		return result;
	}

	private double getSpotCapacity(SpotCapable spotter) {
		//TODO adjusted by terrain, elevation
		return spotter.getSpotCapacity();
	}

	private void recursiveAreaByVisibility(Hex hex, HexSet result, double visibility) {
		if (hex != null) {
			double cost = computeSpotCost(hex);
			if (cost <= visibility) {
				result.add(hex);
				for (HexSide side : HexSide.values()) {
					Hex adjacent = geoService.getHexAt(side.getAdjacent(hex.getCoords()));
					recursiveAreaByVisibility(adjacent, result, visibility - cost);
				}
			}
		}
	}

}

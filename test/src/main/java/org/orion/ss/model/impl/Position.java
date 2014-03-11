package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.PositionRole;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.geo.Fortification;
import org.orion.ss.model.geo.Location;

public class Position extends Formation {

	private List<Fortification> defenses;
	private int prestige;
	private List<Location> supplyArea;
	private List<Location> deployArea;
	private PositionRole role;
	private final Map<Location, Stock> supplies;

	public Position(FormationLevel level, TroopType type, int id, PositionRole role) {
		super(level, type, id);
		this.role = role;
		defenses = new ArrayList<Fortification>();
		supplyArea = new ArrayList<Location>();
		deployArea = new ArrayList<Location>();
		supplies = new HashMap<Location, Stock>();
	}

	@Override
	public boolean isActivable() {
		return true;
	}

	public void decreasePrestige(int prestige) {
		this.prestige -= prestige;
	}

	/* adders */

	protected void addSupplySource(Location coords) {
		supplyArea.add(coords);
	}

	protected void addDeployPoint(Location coords) {
		deployArea.add(coords);
	}

	public void addStock(Stock stock, Location location) {
		Stock target;
		target = supplies.get(location);
		if (target == null) target = new Stock();
		target.add(stock);
		supplies.put(location, target);
	}

	public Stock getStockAt(Location location) {
		return supplies.get(location);
	}

	/* getters & setters */

	public List<Fortification> getDefenses() {
		return defenses;
	}

	public void setDefenses(List<Fortification> defenses) {
		this.defenses = defenses;
	}

	public int getPrestige() {
		return prestige;
	}

	public void setPrestige(int prestige) {
		this.prestige = prestige;
	}

	public List<Location> getSupplyArea() {
		return supplyArea;
	}

	public void setSupplyArea(List<Location> supplyArea) {
		this.supplyArea = supplyArea;
	}

	public PositionRole getRole() {
		return role;
	}

	public void setRole(PositionRole role) {
		this.role = role;
	}

	public List<Location> getDeployArea() {
		return deployArea;
	}

	public void setDeployArea(List<Location> deployArea) {
		this.deployArea = deployArea;
	}

}

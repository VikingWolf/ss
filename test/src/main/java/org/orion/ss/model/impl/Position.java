package org.orion.ss.model.impl;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.PositionRole;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.geo.Fortification;

public class Position extends Formation {
	
	private List<Fortification> defenses;
	private int prestige;
	private List<Point> supplyArea;
	private List<Point> deployArea;
	private PositionRole role;

	public Position(FormationLevel level, TroopType type, int id, PositionRole role){
		super(level, type, id);
		this.role = role;
		defenses = new ArrayList<Fortification>();
		supplyArea = new ArrayList<Point>();
		deployArea = new ArrayList<Point>();
	}
	
	@Override
	public boolean isActivable() {
		return true;
	}
	
	public void decreasePrestige(int prestige){
		this.prestige -= prestige;
	}
	
	protected void addSupplySource(Point coords){
		supplyArea.add(coords);
	}
	
	protected void addDeployPoint(Point coords){
		deployArea.add(coords);
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

	public List<Point> getSupplyArea() {
		return supplyArea;
	}

	public void setSupplyArea(List<Point> supplyArea) {
		this.supplyArea = supplyArea;
	}

	public PositionRole getRole() {
		return role;
	}

	public void setRole(PositionRole role) {
		this.role = role;
	}

	public List<Point> getDeployArea() {
		return deployArea;
	}

	public void setDeployArea(List<Point> deployArea) {
		this.deployArea = deployArea;
	}
	
}

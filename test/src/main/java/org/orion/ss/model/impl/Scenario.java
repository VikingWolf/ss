package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orion.ss.model.core.PositionRole;
import org.orion.ss.model.geo.GeoMap;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.Minefield;
import org.orion.ss.model.geo.Terrain;
import org.orion.ss.model.geo.Vegetation;
import org.orion.ss.model.geo.WeatherForecast;

public class Scenario {

	private String name;
	private List<Position> positions;
	private GameSettings settings;
	private Map<Location, Minefield> minefields;
	private List<WeatherForecast> forecast;
	private GeoMap map;
	private List<Objective> objectives;

	public Scenario(String name, int mapRows, int mapColumns) {
		super();
		this.name = name;
		map = new GeoMap(mapRows, mapColumns, Terrain.PLAINS, Vegetation.NONE);
		positions = new ArrayList<Position>();
		objectives = new ArrayList<Objective>();
	}

	public void addSupplySource(Location location, Position position) {
		assert (this.getMap().isInsideMap(location));
		assert (positions.contains(position));
		position.addSupplySource(location);
	}

	public void addDeployPoint(Location location, Position position) {
		assert (this.getMap().isInsideMap(location));
		assert (positions.contains(position));
		position.addDeployPoint(location);
	}

	public List<Position> getDefender() {
		ArrayList<Position> result = new ArrayList<Position>();
		for (Position position : positions) {
			if (position.getRole() == PositionRole.DEFENDER) {
				result.add(position);
			}
		}
		return result;
	}

	/* adders */
	public void addPosition(Position position) {
		positions.add(position);
	}

	public void addObjective(Objective objective) {
		objectives.add(objective);
	}

	/* getters & setters */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Position> getAttacker() {
		ArrayList<Position> result = new ArrayList<Position>();
		for (Position position : positions) {
			if (position.getRole() == PositionRole.ATTACKER) {
				result.add(position);
			}
		}
		return result;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public void setSettings(GameSettings settings) {
		this.settings = settings;
	}

	public Map<Location, Minefield> getMinefields() {
		return minefields;
	}

	public void setMinefields(Map<Location, Minefield> minefields) {
		this.minefields = minefields;
	}

	public List<WeatherForecast> getForecast() {
		return forecast;
	}

	public void setForecast(List<WeatherForecast> forecast) {
		this.forecast = forecast;
	}

	public GeoMap getMap() {
		return map;
	}

	public void setMap(GeoMap map) {
		this.map = map;
	}

	public List<Objective> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<Objective> objectives) {
		this.objectives = objectives;
	}

}

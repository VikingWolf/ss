package org.orion.ss.model.impl;

import java.util.List;
import java.util.Map;

import org.orion.ss.model.geo.GeoMap;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.Minefield;
import org.orion.ss.model.geo.Railway;
import org.orion.ss.model.geo.Terrain;
import org.orion.ss.model.geo.Trench;
import org.orion.ss.model.geo.UrbanCenter;
import org.orion.ss.model.geo.Vegetation;
import org.orion.ss.model.geo.WeatherForecast;

public class Scenario {

	private String name;
	private List<Position> attacker;
	private List<Position> defender;
	private GameSettings settings;
	private Map<Location, Railway> railroads;
	private List<UrbanCenter> urbanCenters;
	private Map<Location, Trench> trenches;
	private Map<Location, Minefield> minefields;
	private Map<Location, Airfield> airfields;
	private List<WeatherForecast> forecast;
	private GeoMap map;

	public Scenario(int mapRows, int mapColumns) {
		super();
		map = new GeoMap(mapRows, mapColumns, Terrain.PLAINS, Vegetation.NONE);
	}

	/* getters & setters */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Position> getAttacker() {
		return attacker;
	}

	public void setAttacker(List<Position> attacker) {
		this.attacker = attacker;
	}

	public List<Position> getDefender() {
		return defender;
	}

	public void setDefender(List<Position> defender) {
		this.defender = defender;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public void setSettings(GameSettings settings) {
		this.settings = settings;
	}

	public Map<Location, Railway> getRailroads() {
		return railroads;
	}

	public void setRailroads(Map<Location, Railway> railroads) {
		this.railroads = railroads;
	}

	public List<UrbanCenter> getUrbanCenters() {
		return urbanCenters;
	}

	public void setUrbanCenters(List<UrbanCenter> urbanCenters) {
		this.urbanCenters = urbanCenters;
	}

	public Map<Location, Trench> getTrenches() {
		return trenches;
	}

	public void setTrenches(Map<Location, Trench> trenches) {
		this.trenches = trenches;
	}

	public Map<Location, Minefield> getMinefields() {
		return minefields;
	}

	public void setMinefields(Map<Location, Minefield> minefields) {
		this.minefields = minefields;
	}

	public Map<Location, Airfield> getAirfields() {
		return airfields;
	}

	public void setAirfields(Map<Location, Airfield> airfields) {
		this.airfields = airfields;
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

}

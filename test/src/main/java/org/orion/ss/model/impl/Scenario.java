package org.orion.ss.model.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Scenario {

	private String name;
	private List<Position> attacker;
	private List<Position> defender;
	private byte turnDuration; /* in hours */
	private Date initialTime;
	private short timeLimit; /* in turns */
	private short timeMargin; /* in turns */
	private double hexSide; /* in km */
	private short stackLimit; /* in companies */
	private List<Objective> objectives;
	private Map<Location, Road> roads;
	private Map<Location, Railway> railroads;
	private List<UrbanCenter> urbanCenters;
	private Map<Location, Trench> trenches;
	private Map<Location, Minefield> minefields;
	private Map<Location, Airfield> airfields;
	private List<Location> supplyArea;
	private List<WeatherForecast> forecast;
	private Map<Location, Stock> supplies;

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

	public byte getTurnDuration() {
		return turnDuration;
	}

	public void setTurnDuration(byte turnDuration) {
		this.turnDuration = turnDuration;
	}

	public Date getInitialTime() {
		return initialTime;
	}

	public void setInitialTime(Date initialTime) {
		this.initialTime = initialTime;
	}

	public short getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(short timeLimit) {
		this.timeLimit = timeLimit;
	}

	public short getTimeMargin() {
		return timeMargin;
	}

	public void setTimeMargin(short timeMargin) {
		this.timeMargin = timeMargin;
	}

	public double getHexSide() {
		return hexSide;
	}

	public void setHexSide(double hexSide) {
		this.hexSide = hexSide;
	}

	public short getStackLimit() {
		return stackLimit;
	}

	public void setStackLimit(short stackLimit) {
		this.stackLimit = stackLimit;
	}

	public List<Objective> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<Objective> objectives) {
		this.objectives = objectives;
	}

	public Map<Location, Road> getRoads() {
		return roads;
	}

	public void setRoads(Map<Location, Road> roads) {
		this.roads = roads;
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

	public List<Location> getSupplyArea() {
		return supplyArea;
	}

	public void setSupplyArea(List<Location> supplyArea) {
		this.supplyArea = supplyArea;
	}

	public List<WeatherForecast> getForecast() {
		return forecast;
	}

	public void setForecast(List<WeatherForecast> forecast) {
		this.forecast = forecast;
	}

	public Map<Location, Stock> getSupplies() {
		return supplies;
	}

	public void setSupplies(Map<Location, Stock> supplies) {
		this.supplies = supplies;
	}

}

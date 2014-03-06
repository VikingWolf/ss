package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.GamePhase;
import org.orion.ss.model.geo.GeoMap;
import org.orion.ss.model.geo.Location;

public class Game extends Observable {

	private Map<Player, Position> positions;
	private GameSettings settings;
	private String id;
	private int turn = 0;
	private Player currentPlayer;
	private GameLog log;
	private GamePhase phase = GamePhase.MANAGEMENT;
	private GeoMap map;

	public Game(String id, GameSettings settings) {
		super();
		this.id = id;
		this.settings = settings;
		positions = new HashMap<Player, Position>();
		log = new GameLog();
		logGameStart();
	}

	public void advanceTurn() {
		if (turn == 0) {
			if (this.getPhase().equals(GamePhase.MANAGEMENT)) {
				this.setPhase(GamePhase.DEPLOYMENT);
			} else if (this.getPhase().equals(GamePhase.DEPLOYMENT)) {
				this.setPhase(GamePhase.TURN);
				turn++;
			}
		} else {
			turn++;
		}
	}

	public Position getPositionFor(Player player) {
		return positions.get(player);
	}

	/* loggers */

	public void logGameStart() {
		log.addEntry("Game " + getId() + " created at " + new Date());
		log.addEntry("turn=" + getTurn());
		log.addEntry("date=" + getDate());
		log.addDisplay(settings);
		log.addSeparator();
	}

	private void logPosition(Player player, Position position) {
		String entry = player.getEmail() + " plays with " + position.getLongName() + "(" + position.getCountry().getName() + ") as " + position.getRole();
		this.getLog().addEntry(entry);
		log.addDisplay(position.getCountry().getMarket());
	}

	public void logScore() {
		this.getLog().addSeparator();
		this.getLog().addEntry("Game score");
		for (Player player : this.getPlayers()) {
			Position position = getPositionFor(player);
			this.getLog().addEntry(player.getEmail() + "\t\t" + position.getLongName() + "(" + position.getCountry() + ") =\t\t" + position.getPrestige());
		}
	}

	public List<Company> getAllCompanies() {
		List<Company> result = new ArrayList<Company>();
		for (Position position : positions.values()) {
			result.addAll(position.getAllCompanies());
		}
		return result;
	}

	public Map<Location, UnitStack> getAllUnitsLocated() {
		Map<Location, UnitStack> result = new HashMap<Location, UnitStack>();
		for (Position position : positions.values()) {
			for (Unit unit : position.getAllUnits()) {
				UnitStack stack = new UnitStack(unit.getLocation());
				if (result.get(unit.getLocation()) != null) {
					stack = result.get(unit.getLocation());
				}
				stack.add(unit);
				result.put(stack.getLocation(), stack);
			}
		}
		return result;
	}

	/* adders */

	public void addPosition(Player player, Position position) {
		positions.put(player, position);
		logPosition(player, position);
	}

	public Date getDate() {
		long time = this.getSettings().getInitialTime().getTime();
		time += this.getTurn() * this.getSettings().getTurnDuration() * GameSettings.HOUR_MILLIS;
		return new Date(time);
	}

	/* getters & setters */

	public List<Player> getPlayers() {
		List<Player> result = new ArrayList<Player>();
		result.addAll(positions.keySet());
		return result;
	}

	public void setPositions(Map<Player, Position> positions) {
		this.positions = positions;
	}

	public Map<Player, Position> getPositions() {
		return positions;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public void setSettings(GameSettings settings) {
		this.settings = settings;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public Position getCurrentPlayerPosition() {
		return this.getPositionFor(getCurrentPlayer());
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
		if (turn == 0) {
			getLog().addEntry(currentPlayer.getEmail() + " plays his " + getPhase() + " phase.");
		} else {
			getLog().addEntry(currentPlayer.getEmail() + " plays his turn " + getTurn());
		}
		setChanged();
		this.notifyObservers(currentPlayer);
	}

	public GameLog getLog() {
		return log;
	}

	public void setLog(GameLog log) {
		this.log = log;
	}

	public GamePhase getPhase() {
		return phase;
	}

	public void setPhase(GamePhase phase) {
		this.phase = phase;
	}

	public GeoMap getMap() {
		return map;
	}

	public void setMap(GeoMap map) {
		this.map = map;
	}

}

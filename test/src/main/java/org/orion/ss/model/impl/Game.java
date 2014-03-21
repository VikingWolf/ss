package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.orion.ss.model.core.GamePhase;
import org.orion.ss.model.geo.GeoMap;
import org.orion.ss.model.geo.WeatherType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game extends Observable {

	protected Logger logger = LoggerFactory.getLogger(Game.class);

	/* para evitar que las unidades que están fuera de la porción de mapa visible pierdan su visibilidad dentro de el */
	private final static double MAX_SPOTTING = 10.0d;

	private final static List<String> _games = new ArrayList<String>();

	private Map<Player, Position> positions;
	private GameSettings settings;
	private String id;
	private int turn = 0;
	private Player currentPlayer;
	private GameLog log;
	private GamePhase phase = GamePhase.MANAGEMENT;
	private GeoMap map;
	private final String scenarioName;
	private WeatherType weather;

	public Game(String id, GameSettings settings, String scenarioName) {
		super();
		assert (!_games.contains(id));
		this.id = id;
		this.settings = settings;
		this.scenarioName = scenarioName;
		positions = new HashMap<Player, Position>();
		log = new GameLog();
		logGameStart();
		_games.add(id);
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

	public Date getDate() {
		long time = this.getSettings().getInitialTime().getTime();
		time += this.getTurn() * this.getSettings().getTurnDuration() * GameSettings.HOUR_MILLIS;
		return new Date(time);
	}

	public int getMaxSpotting() {
		return (int) Math.floor(MAX_SPOTTING / this.getSettings().getHexSide());
	}

	public double getHexDistance() {
		return getSettings().getHexSide() * 1.5d;
	}

	/* adders */

	public void addPosition(Player player, Position position) {
		positions.put(player, position);
		logPosition(player, position);
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

	public String getScenarioName() {
		return scenarioName;
	}

	public WeatherType getWeather() {
		return weather;
	}

	public void setWeather(WeatherType weather) {
		this.weather = weather;
	}

	public double getTurnDuration() {
		return settings.getTurnDuration();
	}

	/* equals and hashcode */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Game other = (Game) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

}

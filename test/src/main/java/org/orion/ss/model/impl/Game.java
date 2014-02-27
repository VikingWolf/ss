package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.orion.ss.model.core.GamePhase;

public class Game extends Observable {

	private Map<Player, Position> attacker;
	private Map<Player, Position> defender;
	private GameSettings settings;
	private String id;
	private int turn = 0;
	private Player currentPlayer;
	private Market market;
	private GameLog log;
	private GamePhase phase = GamePhase.MANAGEMENT;

	public Game(String id, GameSettings settings, Market market) {
		super();
		this.id = id;
		this.settings = settings;
		this.market = market;
		attacker = new HashMap<Player, Position>();
		defender = new HashMap<Player, Position>();
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
		Position result = null;
		if (attacker.containsKey(player)) {
			result = attacker.get(player);
		} else if (defender.containsKey(player)) {
			result = defender.get(player);
		}
		return result;
	}
	
	/* loggers */
	
	public void logGameStart(){
		log.addEntry("Game " + getId() + " created at " + new Date());
		log.addEntry("turn=" + getTurn());
		log.addEntry("date=" + getDate());
		log.addDisplay(settings);
		log.addDisplay(market);
		log.addSeparator();
	}
	
	private void logPositions(Map<Player, Position> positions, int mode){
		for (Player player : positions.keySet()){
			Position position = positions.get(player);
			logPosition(player, position, mode);
		}
	}
	
	private void logPosition(Player player, Position position, int mode){
		String entry = player.getEmail() + " plays with " + position.getName() + "(" + position.getCountry() +") as ";
		if (mode == Position.ATTACKER){
			entry += "attacker.";
		} else {
			entry += "defender.";
		}
		this.getLog().addEntry(entry);		
	}
	
	public void logScore(){
		this.getLog().addSeparator();
		this.getLog().addEntry("Game score");
		for (Player player : this.getPlayers()){
			Position position = getPositionFor(player);
			this.getLog().addEntry(player.getEmail() + "\t\t" + position.getName() + "(" + position.getCountry() + ") =\t\t" + position.getPrestige());
		}
	}

	/* adders */

	public void addAttacker(Player player, Position position) {
		attacker.put(player, position);
		logPosition(player, position, Position.ATTACKER);
	}

	public void addDefender(Player player, Position position) {
		defender.put(player, position);
		logPosition(player, position, Position.DEFENDER);
	}

	public Date getDate() {
		long time = this.getSettings().getInitialTime().getTime();
		time += this.getTurn() * this.getSettings().getTurnDuration() * GameSettings.HOUR_MILLIS;
		return new Date(time);
	}

	/* getters & setters */

	public List<Player> getPlayers() {
		ArrayList<Player> result = new ArrayList<Player>();
		result.addAll(attacker.keySet());
		result.addAll(defender.keySet());
		return result;
	}

	public Map<Player, Position> getAttacker() {
		return attacker;
	}

	public void setAttacker(Map<Player, Position> attacker) {
		this.attacker = attacker;
	}

	public Map<Player, Position> getDefender() {
		return defender;
	}

	public void setDefender(Map<Player, Position> defender) {
		this.defender = defender;
		logPositions(attacker, Position.DEFENDER);
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
	
	public Position getCurrentPlayerPosition(){
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

	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
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

}

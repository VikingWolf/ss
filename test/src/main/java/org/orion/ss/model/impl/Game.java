package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
		
	private Map<Player, Position> attacker;
	private Map<Player, Position> defender;
	private GameSettings settings;
	private String id;
	private int turn = 0;
	private Player currentPlayer;
	private Market market;
	
	public Game(String id, GameSettings settings, Market market){
		super();
		this.id = id;
		this.settings = settings;
		this.market = market;
		attacker = new HashMap<Player, Position>();
		defender = new HashMap<Player, Position>();
	}
	
	public void advanceTurn(){
		turn++;
	}
	
	public Position getPositionFor(Player player){
		Position result = null;
		if (attacker.containsKey(player)) {
			result = attacker.get(player);
		} else if (defender.containsKey(player)){
			result = defender.get(player);
		}
		return result;
	}
	
	/* adders */
	
	public void addAttacker(Player player, Position position){
		attacker.put(player, position);
	}
	
	public void addDefender(Player player, Position position){
		defender.put(player, position);
	}
	
	public Date getDate(){
		long time = this.getSettings().getInitialTime().getTime();
		time += this.getTurn() * this.getSettings().getTurnDuration() * GameSettings.HOUR_MILLIS;
		return new Date(time);
	}
	
	/* getters & setters */
	
	public List<Player> getPlayers(){
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

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}
	
}

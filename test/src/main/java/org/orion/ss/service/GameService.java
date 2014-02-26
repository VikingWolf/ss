package org.orion.ss.service;

import java.util.List;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Player;

public class GameService extends Service {
		
	public GameService(Game game){
		super(game);
	}
	
	public void endTurn(){
		//TODO check victory
		this.getGame().advanceTurn();
		resetPlayers();
	}
	
	protected void resetPlayers(){
		for (Player player : getGame().getPlayers()){
			player.reset();
		}
	}
	
	public Player nextPlayer(){
		double max = Double.MIN_VALUE;
		Player result = null;
		for (Player player : getGame().getPlayers()){
			double initiative = computeInitiative(player);
			if (initiative > max){
				max = initiative;
				result = player;
			}
		}		
		return result;
	}
	
	private double computeInitiative(Player player){
		double result = 0d;
		List<Company> companies = this.getGame().getPositionFor(player).getAllCompanies(); 
		for (Company company : companies){
			result += company.computeInitiative();
		}	
		return result / (double) companies.size();
	}
	

}

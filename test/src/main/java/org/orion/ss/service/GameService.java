package org.orion.ss.service;

import java.util.List;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Player;
import org.orion.ss.utils.Maths;

public class GameService extends Service {
		
	private final static double INITIATIVE_UNITARY_DEVIATION = 0.2d;
	
	public GameService(Game game){
		super(game);
	}
	
	public void managementPhase(List<Player> players){
		//TODO
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
			double initiative = Maths.gaussianRandomize(computeInitiative(player), INITIATIVE_UNITARY_DEVIATION);
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

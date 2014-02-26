package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Player;

public class ManagementService extends Service {

	public ManagementService(Game game) {
		super(game);
	}
	
	public List<Player> getSuitablePlayers(){
		List<Player> result = new ArrayList<Player>();
		for (Player player : getGame().getPlayers()){
			if (cheapestBuy() <= getGame().getPositionFor(player).getPrestige()){
				result.add(player);
			}
		}
		return result;
	}
	
	private int cheapestBuy(){
		return 0;
	}

}

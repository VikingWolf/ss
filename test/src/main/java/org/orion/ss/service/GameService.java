package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Player;
import org.orion.ss.test.Test;
import org.orion.ss.utils.Maths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameService extends Service {

	protected final static Logger logger = LoggerFactory.getLogger(Test.class);

	private final static double INITIATIVE_UNITARY_DEVIATION = 0.2d;

	public GameService(Game game) {
		super(game);
	}

	public void endTurn() {
		// TODO check game end
		if (!this.gameHasEnded()) {
			this.getGame().advanceTurn();
			resetPlayers();
		} else {
			getGame().getLog().addEntry("The game has ended!");
		}
	}

	public void startGame() {
		getGame().getLog().addSeparator();
		getGame().getLog().addEntry("The game has started!");
		resetPlayers();
		nextPlayer();
	}

	public boolean nextPlayer() {
		boolean result = false;
		if (getGame().getCurrentPlayer() != null) {
			getGame().getCurrentPlayer().setPlayed(true);
		}
		Player next = getNextPlayer();
		if (next != null) {
			getGame().setCurrentPlayer(next);
		} else {
			endTurn();
			if (!gameHasEnded()) {
				next = getNextPlayer();
				getGame().setCurrentPlayer(next);
			} else {
				result = true;
			}
		}
		return result;
	}

	public void computeScore() {		
		getGame().logScore();
	}

	private List<Player> getSuitablePlayers() {
		List<Player> result = new ArrayList<Player>();
		for (Player player : getGame().getPlayers()) {
			if (!player.isPlayed()) {
				result.add(player);
			}
		}
		return result;
	}

	private void resetPlayers() {
		for (Player player : getGame().getPlayers()) {
			player.reset();
		}
	}

	private double computeInitiative(Player player) {
		double result = 0d;
		List<Company> companies = this.getGame().getPositionFor(player).getAllCompanies();
		for (Company company : companies) {
			result += company.computeInitiative();
		}
		result = Maths.gaussianRandomize(result, INITIATIVE_UNITARY_DEVIATION);
		return result / companies.size();
	}

	private Player getNextPlayer() {
		Player result = null;
		double max = Double.NEGATIVE_INFINITY;
		for (Player player : getSuitablePlayers()) {
			double initiative = computeInitiative(player);
			logger.debug("current=" + max + ", initiative=" + initiative + ", player=" + player);
			if (initiative > max) {
				max = initiative;
				result = player;
			}
		}
		return result;
	}

	public boolean gameHasEnded() {
		// TODO game endconditions
		boolean result = false;
		if ((getGame().getTurn() >= getGame().getSettings().getTimeLimit() + getGame().getSettings().getTimeMargin())
				&& (this.getSuitablePlayers().isEmpty())) {
			result = true;
		}
		return result;
	}

}

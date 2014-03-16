package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.GamePhase;
import org.orion.ss.model.core.ObjectiveType;
import org.orion.ss.model.core.PositionRole;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.WeatherType;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Objective;
import org.orion.ss.model.impl.Player;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.Scenario;
import org.orion.ss.test.Test;
import org.orion.ss.utils.Maths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameService extends Service {

	protected final static Logger logger = LoggerFactory.getLogger(Test.class);

	private final static double INITIATIVE_UNITARY_DEVIATION = 0.2d;

	private Scenario scenario;

	protected GameService(Game game, Scenario scenario) {
		super(game);
		this.scenario = scenario;
	}

	public void endTurn() {
		if (!this.gameHasEnded()) {
			this.getGame().advanceTurn();
			resetPlayers();
			if (getGame().getPhase().equals(GamePhase.TURN)) {
				setWeather();
			}
		} else {
			getGame().getLog().addEntry("The game has ended!");
		}
	}

	protected void setWeather() {
		double random = new Random().nextDouble();
		WeatherType weather = getScenario().getForecast().get(getGame().getTurn() - 1).resolveWeather(random);
		getGame().setWeather(weather);
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

	public ObjectiveType getObjectiveType(Location location, Position position) {
		ObjectiveType result = null;
		for (Objective objective : this.getScenario().getObjectives()) {
			if (objective.getLocation().equals(location)) {
				if (objective.getType() == ObjectiveType.MAIN) {
					result = objective.getType();
				} else if (objective.getType() == ObjectiveType.DEFENDER) {
					if (position.getRole() == PositionRole.DEFENDER) {
						result = objective.getType();
					}
				} else if (objective.getType() == ObjectiveType.SECONDARY) {
					if (position.getRole() == PositionRole.ATTACKER) {
						result = objective.getType();
					}
				}
				break;
			}
		}
		return result;
	}

	public List<String> canEndDeployment(Position position) {
		List<String> result = new ArrayList<String>();
		if (position.getLocation() == null) {
			result.add(position.getFullLongName() + " must be deployed before ending.");
		}
		for (Company company : position.getAllCompanies()) {
			if (company.getLocation() != null) {
				if (company.getParent().getLocation() == null) {
					String message = company.getParent().getFullLongName() + " must be deployed before ending.";
					if (!result.contains(message))
						result.add(message);
				}
			}
		}
		return result;
	}

	public List<Unit> undeployedUnits(Position position) {
		List<Unit> result = new ArrayList<Unit>();
		for (Company company : position.getAllCompanies()) {
			if (company.getLocation() == null) {
				result.add(company);
			}
		}
		return result;
	}

	/* getters & setters */

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

}

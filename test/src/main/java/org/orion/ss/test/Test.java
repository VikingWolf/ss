package org.orion.ss.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.orion.ss.model.core.Country;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	protected final static Logger logger = LoggerFactory.getLogger(Test.class);

	private Game game;

	public static void main(String[] args) {
		Test test = new Test();
		logger.error("Starting test at " + new Date());
		test.mount();
	}

	protected void mount() {
		this.game = new Game();
		List<Position> attacker = new ArrayList<Position>();
		List<Position> defender = new ArrayList<Position>();
		Position ger = new Position();
		ger.setCode("1st Infanterieregiment");
		ger.setCountry(Country.GER);
		attacker.add(ger);
		Position uk = new Position();
		uk.setCode("3rd Rifle Regiment");
		uk.setCountry(Country.UK);
		defender.add(uk);
		game.setAttacker(attacker);
		game.setDefender(defender);
	}

	/* getters & setters */

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}

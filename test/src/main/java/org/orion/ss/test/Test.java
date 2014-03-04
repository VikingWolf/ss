package org.orion.ss.test;

import java.util.Date;

import org.orion.ss.model.impl.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	protected final static Logger logger = LoggerFactory.getLogger(Test.class);

	private Game game;

	/* services */

	public static void main(String[] args) {
		Test test = new Test();
		logger.info("Starting test at " + new Date());
		test.mountServices();
		test.start();
		/**
		 * secuencia 1. Determinar iniciativa de los jugadores 1.1 El jugador con la iniciativa
		 */
		logger.info("Ending test at " + new Date());
	}

	protected void start() {
	}

	protected void mountServices() {
		game = new GameSample().buildGame();
		logger.info("Mounting services...");
	}

	/* getters & setters */

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}

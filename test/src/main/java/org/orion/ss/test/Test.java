package org.orion.ss.test;

import java.util.Date;

import org.orion.ss.model.impl.Game;
import org.orion.ss.service.GameService;
import org.orion.ss.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	protected final static Logger logger = LoggerFactory.getLogger(Test.class);
	
	private Game game;
	
	/* services */
	private OrderService orderService;
	private GameService gameService;

	public static void main(String[] args) {
		Test test = new Test();
		logger.info("Starting test at " + new Date());
		test.mountServices();
		test.start();
		/** secuencia
		 * 1. Determinar iniciativa de los jugadores
		 * 	1.1 El jugador con la iniciativa 
		 */
		logger.info("Ending test at " + new Date());
	}
	
	protected void start(){
		logger.info(gameService.nextPlayer() + "");
	}
	
	
	protected void mountServices(){
		game = new GameSample().buildGame();
		logger.info("Mounting services...");
		orderService = new OrderService(game);
		gameService = new GameService(game);
	}

	/* getters & setters */

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}

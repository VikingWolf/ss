package org.orion.ss.service;

import org.orion.ss.model.impl.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Service {

	protected final static Logger logger = LoggerFactory.getLogger(Service.class);
	
	private Game game;

	public Service(Game game) {
		this.game = game;
	}
	
	/* getters & setters */

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}

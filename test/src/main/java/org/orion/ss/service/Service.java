package org.orion.ss.service;

import org.orion.ss.model.impl.Game;

public abstract class Service {

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

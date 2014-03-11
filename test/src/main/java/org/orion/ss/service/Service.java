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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Service other = (Service) obj;
		if (game == null) {
			if (other.game != null) return false;
		} else if (!game.equals(other.game)) return false;
		return true;
	}

}

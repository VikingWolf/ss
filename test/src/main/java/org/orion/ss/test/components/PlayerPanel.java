package org.orion.ss.test.components;

import org.orion.ss.model.impl.Game;
import org.orion.ss.service.GameService;
import org.orion.ss.test.GraphicTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PlayerPanel extends FastPanel {

	private static final long serialVersionUID = -590500321758963247L;

	protected final static Logger logger = LoggerFactory.getLogger(ManagementPanel.class);

	protected final GameService gameService;
	protected final GraphicTest parent;

	public PlayerPanel(GraphicTest parent, GameService gameService) {
		super();
		this.gameService = gameService;
		this.parent = parent;
	}

	public abstract void mount();

	public Game getGame() {
		return gameService.getGame();
	}

}

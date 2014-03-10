package org.orion.ss.test.components;

import org.orion.ss.model.impl.Game;
import org.orion.ss.test.GraphicTest;

public class TurnPanel extends PlayerPanel {

	private static final long serialVersionUID = 6444803414199994917L;

	public TurnPanel(GraphicTest parent, Game game) {
		super(parent, game);
	}

	@Override
	public void mount() {
		removeAll();
		// TODO Auto-generated method stub
	}

}

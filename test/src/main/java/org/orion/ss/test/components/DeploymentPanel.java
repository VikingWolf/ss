package org.orion.ss.test.components;

import org.orion.ss.model.impl.Game;
import org.orion.ss.service.GeoService;
import org.orion.ss.test.GraphicTest;

public class DeploymentPanel extends PlayerPanel {

	private static final long serialVersionUID = 8107876468158803580L;

	private final GeoService geoService;

	private final static double HEX_SIDE = 48.0d;

	public DeploymentPanel(GraphicTest parent, Game game) {
		super(parent, game);
		geoService = new GeoService(game);
		setBounds(GraphicTest.TAB_BOUNDS);
		setLayout(null);
		mount();
	}

	@Override
	public void mount() {
		removeAll();
		ScrollableMap mapPanel = new ScrollableMap(game.getMap(), 500, GraphicTest.TOP_MARGIN, 860, 560, HEX_SIDE);
		add(mapPanel);
	}

}

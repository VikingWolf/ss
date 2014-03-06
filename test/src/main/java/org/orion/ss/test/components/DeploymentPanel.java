package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Game;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.GraphService;
import org.orion.ss.test.GraphicTest;

public class DeploymentPanel extends PlayerPanel implements LocationUpdatable {

	private static final long serialVersionUID = 8107876468158803580L;

	private final GeoService geoService;
	private final GraphService graphService;

	private final static double HEX_SIDE = 48.0d;

	private ScrollableMap mapPanel;
	private DeploymentTreePanel treePanel;
	private JButton endTurnB;
	private JLabel infoL;

	private Unit selectedUnit;

	public DeploymentPanel(GraphicTest parent, Game game) {
		super(parent, game);
		geoService = new GeoService(game);
		graphService = new GraphService(game);
		setBounds(GraphicTest.TAB_BOUNDS);
		setLayout(null);
		mount();
	}

	@Override
	public void mount() {
		removeAll();
		addLabel("Deployment, " + game.getCurrentPlayer().getEmail(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		infoL = new JLabel("Select unit and deploy to the map");
		infoL.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		add(infoL);
		mountTreePanel();
		mountEndTurnButton();
		mountMapPanel();
	}

	protected void mountTreePanel() {
		treePanel = new DeploymentTreePanel(this, game.getCurrentPlayerPosition(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT * 2,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				435);
		add(treePanel.getPanel());
	}

	protected void mountEndTurnButton() {
		endTurnB = new JButton("End Deployment");
		endTurnB.setBounds(GraphicTest.LEFT_MARGIN, 540, GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
		add(endTurnB);
		endTurnB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean gameEnded = parent.nextPlayer();
				if (gameEnded) {
					endTurnB.setEnabled(false);
					parent.endGame();
				} else {
					parent.updatePlayerPanel();
				}
			}
		});
	}

	protected void mountMapPanel() {
		mapPanel = new ScrollableMap(game.getMap(), 500, GraphicTest.TOP_MARGIN, 860, 560, HEX_SIDE, geoService.fullMap(), this, graphService);
		mapPanel.setUnits(game.getAllUnitsLocated());
		add(mapPanel);
	}

	public void updateMap(Unit unit) {
		selectedUnit = unit;
		mapPanel.setDeployArea(geoService.getDeployArea(unit));
		mapPanel.repaint();
	}

	@Override
	public void updateLocation(Location location) {
		boolean result = geoService.deploy(selectedUnit, location);
		if (result) {
			mapPanel.setUnits(game.getAllUnitsLocated());
			infoL.setForeground(Color.BLACK);
			infoL.setText(selectedUnit.getFullName() + " placed at (" + location.getX() + "," + location.getY() + ")");
		} else {
			infoL.setForeground(Color.RED);
			infoL.setText("This unit cannot be placed here!");
		}
		mapPanel.repaint();
	}

	@Override
	public void locationInfo(Location location) {
		// TODO Auto-generated method stub

	}
}

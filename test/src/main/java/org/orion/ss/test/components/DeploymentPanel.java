package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Game;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.GraphService;
import org.orion.ss.test.GraphicTest;
import org.springframework.util.StringUtils;

public class DeploymentPanel extends PlayerPanel implements LocationUpdatable {

	private static final long serialVersionUID = 8107876468158803580L;

	private final GeoService geoService;
	private final GraphService graphService;

	private final static double HEX_SIDE = 48.0d;

	private ScrollableMap mapPanel;
	private DeploymentTreePanel treePanel;
	private JButton endTurnB;
	private JLabel infoL;
	private SmallUnitInfoPanel unitInfoPanel;
	private JTextField toDeployTF;

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
		mountUnitInfoPanel();
		mountEndTurnButton();
		mountMapPanel();
	}

	protected void mountUnitInfoPanel() {
		unitInfoPanel = new SmallUnitInfoPanel();
		unitInfoPanel.setLayout(null);
		unitInfoPanel.setBounds(
				treePanel.getPanel().getX() + treePanel.getPanel().getWidth() + GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN,
				260,
				480);
		add(unitInfoPanel);
		unitInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Unit Info"));
	}

	protected void mountTreePanel() {
		treePanel = new DeploymentTreePanel(this, game.getCurrentPlayerPosition(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT * 2,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				415);
		add(treePanel.getPanel());
	}

	protected void mountEndTurnButton() {
		addLabel("Undeployed units", 
				GraphicTest.LEFT_MARGIN,
				treePanel.getPanel().getY() + treePanel.getPanel().getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		toDeployTF = new JTextField();
		toDeployTF.setEditable(false);
		toDeployTF.setText("" + geoService.undeployedUnits(game.getCurrentPlayerPosition()).size());
		toDeployTF.setBounds(
				GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
				treePanel.getPanel().getY() + treePanel.getPanel().getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		add(toDeployTF);
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
		unitInfoPanel.update(unit);
	}

	@Override
	public void updateLocation(Location location) {
		boolean result = geoService.deploy(selectedUnit, location);
		if (result) {
			mapPanel.setUnits(game.getAllUnitsLocated());
			infoL.setForeground(Color.BLACK);
			infoL.setText(selectedUnit.getFullLongName() + " placed at (" + location.getX() + "," + location.getY() + ")");
			toDeployTF.setText("" + geoService.undeployedUnits(game.getCurrentPlayerPosition()).size());
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

class SmallUnitInfoPanel extends FastPanel {

	private static final long serialVersionUID = -1792156856265716386L;

	public SmallUnitInfoPanel() {
		super();
	}

	public void update(Unit unit) {
		removeAll();
		if (unit != null) {
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), unit.toString()));
		} else {
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Unit Info"));
		}
		if (unit instanceof Company){
			buildCompanyInfo((Company)unit);
		}
	}

	protected void buildCompanyInfo(Company unit){
		List<String> labels = new ArrayList<String>();
		labels.add("Type");
		labels.add("Mobility");
		labels.add("Speed");
		labels.add("Initiative");
		labels.add("Strength");
		labels.add("Organization");
		labels.add("Morale");
		labels.add("Experience");
		List<String> values = new ArrayList<String>();
		values.add(StringUtils.capitalize(unit.getTroopType().getDenomination()));
		values.add("" + unit.getMobilities());
		//TODO values
		//TODO defenses and attacks
	}
}

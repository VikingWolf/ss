package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.UnitStack;
import org.orion.ss.service.GameService;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.GraphService;
import org.orion.ss.service.ServiceFactory;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.test.dialogs.BuySupplyDialog;
import org.orion.ss.utils.NumberFormats;

public class TurnPanel extends PlayerPanel implements LocationUpdatable, SupplyDisplayer {

	private static final long serialVersionUID = 6444803414199994917L;

	private final static int MODE_INFO = 1;
	private final static int MODE_DEPLOY_SUPPLIES = 2;

	private int actionMode = MODE_INFO;

	private final GeoService geoService;
	private final GraphService graphService;

	/* GUI components */
	private FastPanel hexInfoP = new FastPanel();
	private SmallUnitInfoPanel unitInfoP;
	private FastPanel unitStackList = new FastPanel();
	private final JList<Unit> list = new JList<Unit>();
	private JScrollPane pane;
	private JTextField instructionsTF;
	private ScrollableMap mapPanel;

	private final static double HEX_SIDE = 48.0d;

	public TurnPanel(GraphicTest parent, GameService gameService) {
		super(parent, gameService);
		geoService = ServiceFactory.getGeoService(getGame());
		graphService = ServiceFactory.getGraphService(getGame());
	}

	@Override
	public void mount() {
		removeAll();
		setLayout(null);
		mountTurnInfo();
		hexInfoP = new FastPanel();
		hexInfoP.setBounds(
				GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_NARROW + GraphicTest.LEFT_MARGIN * 2,
				200);
		hexInfoP.setLayout(null);
		add(hexInfoP);
		unitInfoP = new SmallUnitInfoPanel(getGame());
		unitInfoP.setLayout(null);
		unitInfoP.setBounds(
				hexInfoP.getX(),
				hexInfoP.getY() + hexInfoP.getHeight(),
				GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_NARROW + GraphicTest.LEFT_MARGIN * 2,
				300);
		add(unitInfoP);
		mountStackList();
		mountMapPanel();
		instructionsTF = new JTextField();
		instructionsTF.setEditable(false);
		instructionsTF.setForeground(Color.RED);
		instructionsTF.setBounds(GraphicTest.LEFT_MARGIN, 540, 380, GraphicTest.ROW_HEIGHT);
		add(instructionsTF);
	}

	protected void mountStackList() {
		unitStackList = new FastPanel();
		unitStackList.setLayout(null);
		unitStackList.setBounds(GraphicTest.LEFT_MARGIN, hexInfoP.getY() + hexInfoP.getHeight(), GraphicTest.COLUMN_WIDTH_XLARGE, 200);
		add(unitStackList);
		list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					Unit unit = list.getSelectedValue();
					updateUnitInfo(unit);

				}
			}

		});
		pane = new JScrollPane(list);
		pane.setBounds(GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2, unitStackList.getWidth() - GraphicTest.LEFT_MARGIN * 2, unitStackList.getHeight() - GraphicTest.TOP_MARGIN * 3);
		pane.setVisible(false);
		unitStackList.add(pane);
	}

	protected void updateUnitInfo(Unit unit) {
		if (unit == null) {
			unitInfoP.setVisible(false);
		} else {
			unitInfoP.update(unit);
			unitInfoP.setVisible(true);
		}
	}

	protected void updateStackList(UnitStack stack) {
		if (stack.size() > 0) {
			logger.error("stack size=" + stack.size());
			unitStackList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Units at " + stack.getLocation().toString()));
			list.setListData(stack.getArray());
			list.repaint();
			pane.setVisible(true);
		} else {
			pane.setVisible(false);
		}
	}

	protected void updateHexInfoPanel(Hex hex) {
		hexInfoP.removeAll();
		hexInfoP.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), hex.getCoords().toString()));
		hexInfoP.addLabel("Terrain",
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		hexInfoP.addNotEditableTextField(hex.getTerrain() + "",
				GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
				GraphicTest.TOP_MARGIN * 2,
				GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		hexInfoP.addLabel("Vegetation",
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		hexInfoP.addNotEditableTextField(hex.getVegetation() + "",
				GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		hexInfoP.addLabel("Soil",
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * 2,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		hexInfoP.addNotEditableTextField(hex.getSoilState() + "",
				GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * 2,
				GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
	}

	protected void mountMapPanel() {
		mapPanel = new ScrollableMap(getGame().getMap(), 460, GraphicTest.TOP_MARGIN, 860, 560, HEX_SIDE, geoService.fullMap(), this, graphService);
		mapPanel.setUnits(geoService.getAllUnitsLocatedAt(mapPanel.getBounds()));
		add(mapPanel);
	}

	protected void mountTurnInfo() {
		addLabel("" + getGame().getDate(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		addLabel("Turn",
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		addNotEditableTextField("" + getGame().getTurn(),
				GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
				GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		addLabel("Player",
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * 2,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		addNotEditableTextField("" + getGame().getCurrentPlayer().getEmail(),
				GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
				GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * 2,
				GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		addLabel("Weather",
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * 3,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		addNotEditableTextField("" + getGame().getWeather(),
				GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
				GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * 3,
				GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		addLabel("Prestige",
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * 4,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		JTextField prestigeTF = new JTextField();
		prestigeTF.setText(NumberFormats.PRESTIGE.format(getGame().getCurrentPlayerPosition().getPrestige()));
		prestigeTF.setEditable(false);
		prestigeTF.setBounds(
				GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
				GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * 4,
				GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		add(prestigeTF);
		JButton buySuppliesB = new JButton("Buy Supplies");
		buySuppliesB.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * 5,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		buySuppliesB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				instructionsTF.setText("Select a hex to deploy the supplies...");
				mapPanel.setDeployArea(gameService.getGame().getCurrentPlayerPosition().getDeployArea());
				mapPanel.repaint();
				actionMode = MODE_DEPLOY_SUPPLIES;
				// TODO Auto-generated method stub

			}

		});
		add(buySuppliesB);
		JButton purchaseB = new JButton("Purchase Companies");
		purchaseB.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT * 6,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		add(purchaseB);
	}

	protected void showBuySuppliesDialog(Location location) {
		new BuySupplyDialog(this, this.getGame(), location);
	}

	/* state updates */
	@Override
	public void updateLocation(Location location) {
		if (actionMode == MODE_INFO) {
			this.updateHexInfoPanel(geoService.getHexAt(location));
			this.updateStackList(geoService.getStackAt(location));
			this.updateUnitInfo(null);
		} else if (actionMode == MODE_DEPLOY_SUPPLIES) {
			showBuySuppliesDialog(location);
		}
	}

	@Override
	public void locationInfo(Location location, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSuppliesDisplay() {
		instructionsTF.setText("Supplies placed.");
		mapPanel.setSupplies(geoService.getStocksLocatedAt(mapPanel.getBounds(), getGame().getCurrentPlayerPosition()));
		mapPanel.repaint();
	}
}

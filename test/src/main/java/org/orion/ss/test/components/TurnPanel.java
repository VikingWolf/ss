package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Building;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.Unit;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.ObjectiveType;
import org.orion.ss.model.geo.Airfield;
import org.orion.ss.model.geo.Fortification;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.UrbanCenter;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.model.impl.UnitStack;
import org.orion.ss.service.GameService;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.MovementService;
import org.orion.ss.service.OrderService;
import org.orion.ss.service.ServiceFactory;
import org.orion.ss.service.TextService;
import org.orion.ss.service.UnitService;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.test.dialogs.BuySupplyDialog;
import org.orion.ss.test.dialogs.PurchaseCompanyDialog;
import org.orion.ss.test.dialogs.UnitOrdersDialog;
import org.orion.ss.utils.NumberFormats;

public class TurnPanel extends PlayerPanel implements LocationUpdatable, SupplyDisplayer, UnitDisplayer {

	private static final long serialVersionUID = 6444803414199994917L;

	private final static int MODE_INFO = 1;
	private final static int MODE_DEPLOY_SUPPLIES = 2;
	private final static int MODE_DEPLOY_COMPANY = 3;
	private final static int MODE_MOVE_UNIT = 4;

	private int actionMode = MODE_INFO;

	private final GeoService geoService;
	private final TextService textService;
	private final OrderService orderService;
	private final UnitService unitService;
	private final MovementService movementService;

	/* GUI components */
	private FastPanel hexInfoP = new FastPanel();
	private SmallUnitInfoPanel unitInfoP;
	private FastPanel unitStackList = new FastPanel();
	private final JList<Activable> list = new JList<Activable>();
	private JScrollPane pane;
	private JTextField instructionsTF;
	private ScrollableMap mapPanel;
	private JTextField prestigeTF;
	private MapOptionsPanel mapOptionsPanel;
	private JButton ordersB;
	private JButton endTurnB;
	private UnitOrdersDialog unitOrdersDialog;

	private Location currentLocation = null;

	private final static double HEX_SIDE = 48.0d;

	public TurnPanel(GraphicTest parent, GameService gameService) {
		super(parent, gameService);
		geoService = ServiceFactory.getGeoService(getGame());
		textService = ServiceFactory.getTextService(getGame());
		orderService = ServiceFactory.getOrderService(getGame());
		unitService = ServiceFactory.getUnitService(getGame());
		movementService = ServiceFactory.getMovementService(getGame());
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
				240);
		hexInfoP.setLayout(null);
		add(hexInfoP);
		unitInfoP = new SmallUnitInfoPanel(getGame());
		unitInfoP.setLayout(null);
		unitInfoP.setBounds(
				hexInfoP.getX(),
				hexInfoP.getY() + hexInfoP.getHeight(),
				GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_NARROW + GraphicTest.LEFT_MARGIN * 2,
				340);
		add(unitInfoP);
		mountStackList();
		mountMapPanel();
		instructionsTF = new JTextField();
		instructionsTF.setEditable(false);
		instructionsTF.setForeground(Color.RED);
		instructionsTF.setBounds(GraphicTest.LEFT_MARGIN, 600, 1100, GraphicTest.ROW_HEIGHT);
		add(instructionsTF);
		endTurnB = new JButton("End Turn");
		endTurnB.setBounds(
				instructionsTF.getX() + instructionsTF.getWidth() + GraphicTest.LEFT_MARGIN,
				600,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		endTurnB.setForeground(Color.RED);
		add(endTurnB);
		if (gameService.gameHasEnded())
			endTurnB.setEnabled(false);
		endTurnB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!gameService.gameHasEnded()) {
					gameService.nextPlayer();
					parent.updatePlayerPanel();
				}
			}

		});

		mapOptionsPanel = new MapOptionsPanel(mapPanel);
		mapOptionsPanel.setBounds(
				GraphicTest.LEFT_MARGIN,
				unitStackList.getY() + unitStackList.getHeight(),
				GraphicTest.COLUMN_WIDTH_XLARGE,
				140);
		add(mapOptionsPanel);
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
					Activable activable = list.getSelectedValue();
					if (activable instanceof Unit) {
						Unit unit = (Unit) activable;
						if (orderService.buildOrders(unit).size() > 0) {
							ordersB.setEnabled(true);
							mapPanel.setSelectedUnit(unit);
						} else
						ordersB.setEnabled(false);
					} else {
						ordersB.setEnabled(false);
					}
					updateUnitInfo(activable);
				}
			}

		});
		pane = new JScrollPane(list);
		pane.setBounds(GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2, unitStackList.getWidth() - GraphicTest.LEFT_MARGIN * 2, unitStackList.getHeight() - GraphicTest.TOP_MARGIN * 3);
		pane.setVisible(false);
		unitStackList.add(pane);
	}

	protected void updateUnitInfo(Activable activable) {
		if (activable == null) {
			unitInfoP.setVisible(false);
		} else {
			unitInfoP.update(activable);
			unitInfoP.setVisible(true);
		}
	}

	protected void updateStackList(Building building, UnitStack stack) {
		List<Activable> data = new ArrayList<Activable>();
		for (Activable activable : stack) {
			data.add(activable);
		}
		if (building instanceof Activable) {
			data.add((Activable) building);
		}
		if (data.size() > 0) {
			unitStackList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Units at " + stack.getLocation().toString()));
			list.setListData(data.toArray(new Activable[] {}));
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
		int y = 3;
		String riverSides = textService.getStringSides(geoService.getRiversOf(hex.getCoords()), hex.getCoords());
		if (riverSides.length() > 0) {
			hexInfoP.addLabel("River at " + riverSides,
					GraphicTest.LEFT_MARGIN,
					GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
					GraphicTest.COLUMN_WIDTH_XXLARGE,
					GraphicTest.ROW_HEIGHT);
			y++;
		}
		String roadSides = textService.getStringSides(geoService.getRoadsOf(hex.getCoords()), hex.getCoords());
		if (roadSides.length() > 0) {
			hexInfoP.addLabel("Road at " + roadSides,
					GraphicTest.LEFT_MARGIN,
					GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
					GraphicTest.COLUMN_WIDTH_XXLARGE,
					GraphicTest.ROW_HEIGHT);
			y++;
		}
		String railwaySides = textService.getStringSides(geoService.getRailwaysOf(hex.getCoords()), hex.getCoords());
		if (railwaySides.length() > 0) {
			hexInfoP.addLabel("Railway at " + railwaySides,
					GraphicTest.LEFT_MARGIN,
					GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
					GraphicTest.COLUMN_WIDTH_XXLARGE,
					GraphicTest.ROW_HEIGHT);
			y++;
		}
		String bridgeSides = textService.getStringSides(geoService.getBridgesOf(hex.getCoords()), hex.getCoords());
		if (bridgeSides.length() > 0) {
			hexInfoP.addLabel("Bridge at " + bridgeSides,
					GraphicTest.LEFT_MARGIN,
					GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
					GraphicTest.COLUMN_WIDTH_XXLARGE,
					GraphicTest.ROW_HEIGHT);
			y++;
		}
		Building building = geoService.getBuildingAt(hex.getCoords());
		if (building != null) {
			if (building instanceof UrbanCenter) {
				UrbanCenter center = (UrbanCenter) building;
				hexInfoP.addLabel(center.getName(),
						GraphicTest.LEFT_MARGIN,
						GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
						GraphicTest.COLUMN_WIDTH,
						GraphicTest.ROW_HEIGHT);
				hexInfoP.addNotEditableTextField(NumberFormats.PRESTIGE.format(center.getValue()) + " prestige",
						GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
						GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
						GraphicTest.COLUMN_WIDTH_NARROW,
						GraphicTest.ROW_HEIGHT);
				y++;
			} else if (building instanceof Airfield) {
				Airfield airfield = (Airfield) building;
				hexInfoP.addLabel("Airfield",
						GraphicTest.LEFT_MARGIN,
						GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
						GraphicTest.COLUMN_WIDTH,
						GraphicTest.ROW_HEIGHT);
				if (airfield.getController().equals(getGame().getCurrentPlayerPosition().getCountry())) {
					hexInfoP.addNotEditableTextField("Cap = " + airfield.getCapacity(),
							GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
							GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
							GraphicTest.COLUMN_WIDTH_NARROW,
							GraphicTest.ROW_HEIGHT);
				}
				y++;
			} else if (building instanceof Fortification) {
				Fortification fortification = (Fortification) building;
				hexInfoP.addLabel("Fortifications",
						GraphicTest.LEFT_MARGIN,
						GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
						GraphicTest.COLUMN_WIDTH,
						GraphicTest.ROW_HEIGHT);
				if (fortification.getController().equals(getGame().getCurrentPlayerPosition().getCountry())) {
					hexInfoP.addNotEditableTextField("strength = " + fortification.getStrength(),
							GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
							GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
							GraphicTest.COLUMN_WIDTH_NARROW,
							GraphicTest.ROW_HEIGHT);
				}
				y++;
			}
			ObjectiveType type = gameService.getObjectiveType(building.getLocation(), getGame().getCurrentPlayerPosition());
			if (type != null) {
				hexInfoP.addLabel(type.getDenomination() + " objective",
						GraphicTest.LEFT_MARGIN * 3,
						GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
						GraphicTest.COLUMN_WIDTH,
						GraphicTest.ROW_HEIGHT);
				y++;
			}
		}
	}

	protected void mountMapPanel() {
		mapPanel = new ScrollableMap(460, GraphicTest.TOP_MARGIN, 860, 580, HEX_SIDE, getGame(), getGame().getCurrentPlayerPosition(), this, geoService.fullMap(), MapPanel.MODE_TURN);
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
		prestigeTF = new JTextField();
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
				mapPanel.setDeployArea(getGame().getCurrentPlayerPosition().getDeployArea());
				actionMode = MODE_DEPLOY_SUPPLIES;
			}

		});
		add(buySuppliesB);
		JButton purchaseB = new JButton("Purchase Company");
		purchaseB.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT * 6,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		purchaseB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				instructionsTF.setText("Select a hex to deploy the company...");
				mapPanel.setDeployArea(unitService.getDeployArea(getGame().getCurrentPlayerPosition(), FormationLevel.COMPANY.getSupplyLimit()));
				actionMode = MODE_DEPLOY_COMPANY;
			}

		});
		add(purchaseB);
		ordersB = new JButton("Orders");
		ordersB.setEnabled(false);
		ordersB.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 4 + GraphicTest.ROW_HEIGHT * 7,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		ordersB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showOrdersDialog((Unit) list.getSelectedValue());
			}

		});
		add(ordersB);
	}

	protected void showOrdersDialog(Unit unit) {
		unitOrdersDialog = new UnitOrdersDialog(gameService, unit, this);
	}

	protected void showBuySuppliesDialog(Location location) {
		new BuySupplyDialog(this, gameService, location);
	}

	protected void showBuyCompanyDialog(Location location) {
		new PurchaseCompanyDialog(this, gameService, location);
	}

	/* state updates */
	public void startMoveMode(Mobile mobile) {
		actionMode = MODE_MOVE_UNIT;
		getInstructionsTF().setText("Trace the movement path by clicking adjacent hexes. Right click to end.");
		getMapPanel().setMode(MapPanel.MODE_MOVEMENT);
		movementService.resetMovementPath(mobile);
		mapPanel.repaint();
	}

	@Override
	public void updateLocation(Location location, int... modifiers) {
		setCurrentLocation(location);
		refreshLocation(modifiers);
	}

	@Override
	public void refreshLocation(int... modifiers) {
		if (actionMode == MODE_INFO) {
			this.updateHexInfoPanel(geoService.getHexAt(getCurrentLocation()));
			this.updateStackList(geoService.getBuildingAt(getCurrentLocation()), unitService.getStackAt(getGame().getCurrentPlayerPosition(), getCurrentLocation()));
			this.updateUnitInfo(null);
		} else if (actionMode == MODE_DEPLOY_SUPPLIES) {
			showBuySuppliesDialog(getCurrentLocation());
		} else if (actionMode == MODE_DEPLOY_COMPANY) {
			showBuyCompanyDialog(getCurrentLocation());
		} else if (actionMode == MODE_MOVE_UNIT) {
			if (modifiers[0] == MouseEvent.BUTTON1) {
				//TODO move unit
				unitOrdersDialog.addInfo(getCurrentLocation());
				movementService.addToMovementPath(getCurrentLocation());
			} else if (modifiers[0] == MouseEvent.BUTTON3) {
				mapPanel.setMode(MapPanel.MODE_TURN);
				actionMode = MODE_INFO;
				unitOrdersDialog.setVisible(true);
			}
		}
		if (list.getSelectedValue() != null && orderService.buildOrders(list.getSelectedValue()).size() > 0)
			ordersB.setEnabled(true);
		else ordersB.setEnabled(false);
		mapPanel.repaint();
	}

	@Override
	public void locationInfo(Location location, int x, int y) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateSuppliesDisplay(Stock stock, Location location) {
		instructionsTF.setText("Supplies " + stock.toString() + " placed at " + location.toString());
		prestigeTF.setText(NumberFormats.PRESTIGE.format(getGame().getCurrentPlayerPosition().getPrestige()));
		mapPanel.setDeployArea(null);
		refreshLocation();
		actionMode = MODE_INFO;
	}

	@Override
	public void updateUnitsDisplay(Location location) {
		instructionsTF.setText("Company placed at " + location);
		prestigeTF.setText(NumberFormats.PRESTIGE.format(getGame().getCurrentPlayerPosition().getPrestige()));
		mapPanel.setDeployArea(null);
		actionMode = MODE_INFO;
	}

	@Override
	public void updateUnitDetails(Unit unit) {
		// TODO Auto-generated method stub

	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	/* getters & setters */

	public JTextField getInstructionsTF() {
		return instructionsTF;
	}

	public void setInstructionsTF(JTextField instructionsTF) {
		this.instructionsTF = instructionsTF;
	}

	public ScrollableMap getMapPanel() {
		return mapPanel;
	}

	public void setMapPanel(ScrollableMap mapPanel) {
		this.mapPanel = mapPanel;
	}

}

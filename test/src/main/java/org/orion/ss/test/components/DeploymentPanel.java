package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.UnitStack;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.GraphService;
import org.orion.ss.test.GraphicTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		unitInfoPanel = new SmallUnitInfoPanel(game);
		unitInfoPanel.setLayout(null);
		unitInfoPanel.setBounds(
				treePanel.getPanel().getX() + treePanel.getPanel().getWidth() + GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN,
				265,
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
	public void locationInfo(Location location, int x, int y) {
		int symbolSize = 64;
		int horizCapacity = 6;
		UnitStackDialog unitStackDialog = new UnitStackDialog(geoService.getStackAt(location), symbolSize, horizCapacity);
		Point size = unitStackDialog.computeSize();
		int bX = x + mapPanel.getX() + (int) HEX_SIDE;
		if (bX + (int) size.getX() > GraphicTest.WINDOW_BOUNDS.getWidth()) {
			bX = x + mapPanel.getX() - (int) HEX_SIDE - (int) size.getX();
		}
		unitStackDialog.setBounds(
				bX,
				y + mapPanel.getY(),
				(int) size.getX(),
				(int) size.getY());
		unitStackDialog.mount();
		unitStackDialog.setVisible(true);
	}
}

class UnitStackDialog extends JDialog {

	private static final long serialVersionUID = 5084475594671764480L;

	protected final static Logger logger = LoggerFactory.getLogger(UnitStackDialog.class);

	private final UnitStack stack;
	private final int symbolSize;
	private final int horizCapacity;
	private int vertCapacity = 0;
	private final static int _okButtonHeight = GraphicTest.ROW_HEIGHT;
	private final static int _headerHeight = 80;

	public UnitStackDialog(UnitStack stack, int symbolSize, int horizCapacity) {
		super();
		this.stack = stack;
		this.symbolSize = symbolSize;
		this.horizCapacity = horizCapacity;
		setTitle("Unit Info at (" + stack.getLocation().getX() + "," + stack.getLocation().getY() + ")");
		setModal(true);
		setLayout(null);
	}

	public Point computeSize() {
		vertCapacity = stack.size() / horizCapacity;
		int width = symbolSize * horizCapacity + GraphicTest.LEFT_MARGIN + GraphicTest.RIGHT_MARGIN;
		int height = GraphicTest.TOP_MARGIN + GraphicTest.BOTTOM_MARGIN + _okButtonHeight + vertCapacity * symbolSize + _headerHeight;
		logger.error("height=" + height);
		return new Point(width, height);
	}

	public void mount() {
		UnitStackPanel unitStackPanel = new UnitStackPanel();
		unitStackPanel.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN,
				symbolSize * horizCapacity,
				symbolSize * vertCapacity
				);
		JButton closeB = new JButton("OK");
		closeB.setBounds(
				(this.getWidth() - GraphicTest.LEFT_MARGIN - GraphicTest.RIGHT_MARGIN - GraphicTest.COLUMN_WIDTH) / 2,
				this.getHeight() - _okButtonHeight - GraphicTest.BOTTOM_MARGIN,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT
				);
		logger.error("dialog height=" + getHeight() + ", buttonY=" + closeB.getY());
		closeB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				setVisible(false);
			}
		});
		add(closeB);
	}
}

class UnitStackPanel extends JPanel {

	private static final long serialVersionUID = 4469120112654662176L;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

}

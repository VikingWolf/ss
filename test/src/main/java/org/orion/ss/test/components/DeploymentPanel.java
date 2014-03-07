package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
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
		UnitStack stack = geoService.getStackAt(location);
		if (stack.size()>0){
			UnitStackDialog unitStackDialog = new UnitStackDialog(stack, symbolSize, game);
			Dimension dimension = unitStackDialog.computeSize();
			int bX = x + mapPanel.getWidth() + (int) HEX_SIDE;
			if (bX + (int) dimension.getWidth() > GraphicTest.WINDOW_BOUNDS.getWidth()) {
				bX = x + mapPanel.getX() - (int) HEX_SIDE - (int) dimension.getWidth();
			}
			unitStackDialog.setBounds(
					bX,
					y + mapPanel.getY(),
					(int) dimension.getWidth(),
					(int) dimension.getHeight());
			unitStackDialog.mount();
			unitStackDialog.setVisible(true);
		}
	}
}

class UnitStackDialog extends JDialog {

	private static final long serialVersionUID = 5084475594671764480L;

	protected final static Logger logger = LoggerFactory.getLogger(UnitStackDialog.class);


	private UnitStackPanel unitStackCanvas;
	private final static int _okButtonHeight = GraphicTest.ROW_HEIGHT;
	private final static int _headerHeight = 35;
	private final static int _footerHeight = 40;
	private final static int width = 400;
	private final static int maxHeight = 300;

	public UnitStackDialog(UnitStack stack, int symbolSize, Game game) {
		super();
		unitStackCanvas = new UnitStackPanel(stack, symbolSize, game);
		setTitle("Unit Info at (" + stack.getLocation().getX() + "," + stack.getLocation().getY() + ")");
		setModal(true);
		getContentPane().setLayout(null);
	}

	public Dimension computeSize() {
		int height = unitStackCanvas.getUnitStack().size() * (GraphicTest.TOP_MARGIN + unitStackCanvas.getSymbolSize()) + GraphicTest.TOP_MARGIN * 2 + GraphicTest.BOTTOM_MARGIN + _okButtonHeight +  _footerHeight;
		return new Dimension(width, Math.min(height, maxHeight));
	}

	public void mount() {
		JScrollPane unitStackPanel = new JScrollPane(unitStackCanvas);
		unitStackPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		unitStackPanel.setBounds(
				GraphicTest.LEFT_MARGIN, 
				GraphicTest.TOP_MARGIN,
				this.getWidth() - GraphicTest.LEFT_MARGIN * 2 - GraphicTest.LATERAL_SWING_MARGIN,
				this.getHeight() - _headerHeight - GraphicTest.TOP_MARGIN * 2 - GraphicTest.BOTTOM_MARGIN - _okButtonHeight);
		getContentPane().add(unitStackPanel);			
		JButton closeB = new JButton("OK");
		closeB.setBounds(
				(this.getWidth() - GraphicTest.COLUMN_WIDTH_NARROW) / 2,
				this.getHeight() - _okButtonHeight - GraphicTest.BOTTOM_MARGIN - _footerHeight,
				GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT
				);
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

	private static final long serialVersionUID = -617645392072460358L;

	private GraphService graphService;
	private int symbolSize = 0;
	private UnitStack unitStack;

	public UnitStackPanel(UnitStack unitStack, int symbolSize, Game game){
		super(new GridLayout(0, 1));
		graphService = new GraphService(game);
		graphService.setSymbolSize(symbolSize);
		this.symbolSize = symbolSize;		
		this.unitStack = unitStack;
		for (Unit unit : unitStack){
			UnitInfoRow row = new UnitInfoRow(unit, this.symbolSize, graphService.getUnitSymbol((Company)(unit)));			
			add(row);
		}
	}

	public int getSymbolSize() {
		return symbolSize;
	}

	public void setSymbolSize(int symbolSize) {
		this.symbolSize = symbolSize;
	}

	public UnitStack getUnitStack() {
		return unitStack;
	}

	public void setUnitStack(UnitStack unitStack) {
		this.unitStack = unitStack;
	}

}

class UnitInfoRow extends JPanel {
	
	private static final long serialVersionUID = 7349489354503508589L;

	private Unit unit;
	private int symbolSize;
	private BufferedImage symbol; 
	
	public UnitInfoRow(Unit unit, int symbolSize, BufferedImage symbol){
		super(new GridBagLayout());
		GridBagConstraints gridConstraints = new GridBagConstraints();
		
		JLabel unitSymbol = new JLabel();
		unitSymbol.setIcon(new ImageIcon(symbol));
		
		add(unitSymbol);
		Formation division = unit.getParentFormation(FormationLevel.DIVISION);
		Formation corps = unit.getParentFormation(FormationLevel.CORPS);

	}
}

package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.UnitStack;
import org.orion.ss.service.GameService;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.ServiceFactory;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.test.components.tree.DeploymentTreePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeploymentPanel extends PlayerPanel implements LocationUpdatable, UnitDetailsDisplayer {

	private static final long serialVersionUID = 8107876468158803580L;

	private final GeoService geoService;
	private final GameService gameService;

	private final static double HEX_SIDE = 48.0d;

	private ScrollableMap mapPanel;
	private DeploymentTreePanel treePanel;
	private JButton endTurnB;
	private JLabel infoL;
	private SmallUnitInfoPanel unitInfoPanel;
	private JTextField toDeployTF;

	private Unit selectedUnit;

	public DeploymentPanel(GraphicTest parent, GameService gameService) {
		super(parent, gameService);
		geoService = ServiceFactory.getGeoService(getGame());
		this.gameService = gameService;
		setBounds(GraphicTest.TAB_BOUNDS);
		setLayout(null);
		mount();
	}

	@Override
	public void mount() {
		removeAll();
		addLabel("Deployment, " + getGame().getCurrentPlayer().getEmail(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_XXLARGE,
				GraphicTest.ROW_HEIGHT);
		infoL = new JLabel("Select unit and deploy to the map");
		infoL.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH_XXLARGE,
				GraphicTest.ROW_HEIGHT);
		add(infoL);
		mountTreePanel();
		mountUnitInfoPanel();
		mountEndTurnButton();
		mountMapPanel();
	}

	protected void mountUnitInfoPanel() {
		unitInfoPanel = new SmallUnitInfoPanel(getGame());
		unitInfoPanel.setLayout(null);
		unitInfoPanel.setBounds(
				treePanel.getPanel().getX() + treePanel.getPanel().getWidth() + GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN,
				225,
				480);
		add(unitInfoPanel);
		unitInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Unit Info"));
	}

	protected void mountTreePanel() {
		treePanel = new DeploymentTreePanel(this, getGame().getCurrentPlayerPosition(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT * 2,
				GraphicTest.COLUMN_WIDTH_XXLARGE,
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
		toDeployTF.setText("" + gameService.undeployedUnits(getGame().getCurrentPlayerPosition()).size());
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
				List<String> deploymentErrors = gameService.canEndDeployment(getGame().getCurrentPlayerPosition());
				if (deploymentErrors.size() > 0) {
					showCannotEndDialog(deploymentErrors);
				} else if (gameService.undeployedUnits(getGame().getCurrentPlayerPosition()).size() > 0) {
					showConfirmationDialog();
				} else {
					gameService.nextPlayer();
					parent.updatePlayerPanel();
				}
			}
		});
	}

	protected void showCannotEndDialog(List<String> errors) {
		final JDialog dialog = new JDialog();
		dialog.setTitle("Undeployment incomplete");
		dialog.setBounds((int) GraphicTest.DEFAULT_DIALOG_LOCATION.getX(), (int) GraphicTest.DEFAULT_DIALOG_LOCATION.getY(), 400, 200);
		dialog.setLayout(null);
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				logger.error("window closing");
				super.windowClosing(arg0);
			}

		});
		int y = 0;
		for (String error : errors) {
			JLabel label = new JLabel();
			label.setText(error);
			label.setBounds(
					GraphicTest.LEFT_MARGIN,
					GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
					GraphicTest.COLUMN_WIDTH_XXXXLARGE,
					GraphicTest.ROW_HEIGHT
					);
			dialog.add(label);
			y++;
		}
		JButton okB = new JButton("OK");
		okB.setBounds((dialog.getWidth() - GraphicTest.COLUMN_WIDTH_LARGE) / 2, 120, GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		dialog.add(okB);
		okB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
				dialog.setVisible(false);
			}

		});
		dialog.setModal(true);
		dialog.setVisible(true);

	}

	protected void showConfirmationDialog() {
		final JDialog dialog = new JDialog();
		dialog.setTitle("There are undeployed units");
		dialog.setBounds(100, 100, 400, 200);
		dialog.setLayout(null);
		JLabel label1 = new JLabel("There are " + gameService.undeployedUnits(getGame().getCurrentPlayerPosition()).size() + " undeployed units.");
		label1.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 3,
				400,
				GraphicTest.ROW_HEIGHT
				);
		dialog.add(label1);
		JLabel label2 = new JLabel("Do you really wish to end your deployment?");
		label2.setBounds(
				GraphicTest.LEFT_MARGIN,
				label1.getY() + label1.getHeight() + GraphicTest.TOP_MARGIN,
				400,
				GraphicTest.ROW_HEIGHT
				);
		dialog.add(label2);
		JButton endDeploymentB = new JButton("Yes, end deployment and play");
		endDeploymentB.setBounds(
				(dialog.getWidth() - GraphicTest.COLUMN_WIDTH_XXLARGE) / 2,
				label2.getY() + label1.getHeight() + GraphicTest.TOP_MARGIN * 3,
				GraphicTest.COLUMN_WIDTH_XXLARGE,
				GraphicTest.ROW_HEIGHT
				);
		endDeploymentB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameService.nextPlayer();
				parent.updatePlayerPanel();
				dialog.dispose();
				dialog.setVisible(false);
			}

		});
		dialog.add(endDeploymentB);
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				logger.error("window closing");
				super.windowClosing(arg0);
			}

		});
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	protected void mountMapPanel() {
		mapPanel = new ScrollableMap(500, GraphicTest.TOP_MARGIN, 860, 620, HEX_SIDE, getGame(), getGame().getCurrentPlayerPosition(), this, geoService.fullMap());
		mapPanel.setDrawSupplyArea(false);
		add(mapPanel);
	}

	@Override
	public void updateUnitDetails(Unit unit) {
		selectedUnit = unit;
		mapPanel.setDrawDeployArea(true);
		mapPanel.setSelectedUnit(unit);
		mapPanel.repaint();
		unitInfoPanel.update(unit);
		mapPanel.setSelectedUnit(selectedUnit);
		mapPanel.repaint();
	}

	@Override
	public void updateLocation(Location location) {
		boolean result = geoService.deploy(selectedUnit, location);
		if (result) {
			infoL.setForeground(Color.BLACK);
			infoL.setText(selectedUnit.getFullLongName() + " placed at (" + location.getX() + "," + location.getY() + ")");
			toDeployTF.setText("" + gameService.undeployedUnits(getGame().getCurrentPlayerPosition()).size());
		} else {
			infoL.setForeground(Color.RED);
			infoL.setText("This unit cannot be placed here!");
		}
		mapPanel.repaint();
	}

	@Override
	public void refreshLocation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void locationInfo(Location location, int x, int y) {
		int symbolSize = 64;
		UnitStack stack = geoService.getStackAt(getGame().getCurrentPlayerPosition(), location);
		if (stack.size() > 0) {
			UnitStackDialog unitStackDialog = new UnitStackDialog(stack, symbolSize, getGame());
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

	private final UnitStackPanel unitStackCanvas;
	private final static int _okButtonHeight = GraphicTest.ROW_HEIGHT;
	private final static int _headerHeight = 35;
	private final static int _footerHeight = 40;
	private final static int width = 450;
	private final static int maxHeight = 350;

	public UnitStackDialog(UnitStack stack, int symbolSize, Game game) {
		super();
		unitStackCanvas = new UnitStackPanel(stack, symbolSize, game);
		setTitle("Unit Info at (" + stack.getLocation().getX() + "," + stack.getLocation().getY() + ")");
		setModal(true);
		getContentPane().setLayout(null);
	}

	public Dimension computeSize() {
		int height = unitStackCanvas.getUnitStack().size() * (GraphicTest.TOP_MARGIN + unitStackCanvas.getSymbolSize()) + GraphicTest.TOP_MARGIN * 2 + GraphicTest.BOTTOM_MARGIN + _okButtonHeight + _footerHeight;
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

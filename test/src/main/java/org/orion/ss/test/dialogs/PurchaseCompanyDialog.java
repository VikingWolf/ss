package org.orion.ss.test.dialogs;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;

import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.service.GameService;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.ManagementService;
import org.orion.ss.service.ServiceFactory;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.test.components.FastPanel;
import org.orion.ss.test.components.UnitDetailsDisplayer;
import org.orion.ss.test.components.UnitDisplayer;
import org.orion.ss.test.components.tree.PurchaseCompanyTreePanel;
import org.orion.ss.utils.NumberFormats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PurchaseCompanyDialog extends JDialog implements UnitDetailsDisplayer {

	private static final long serialVersionUID = -3709849519497626262L;
	protected final static Logger logger = LoggerFactory.getLogger(PurchaseCompanyDialog.class);

	private final GameService gameService;
	private final ManagementService managementService;
	private final GeoService geoService;

	private final UnitDisplayer parent;

	private JButton buyB;
	private JComboBox<CompanyModel> modelsCB;
	private JTextField supplyLimitTF;
	private JTextField costTF;
	private JTextField infoTF;

	private Formation selectedFormation;

	public PurchaseCompanyDialog(UnitDisplayer parent, GameService gameService, Location location) {
		super();
		this.parent = parent;
		this.gameService = gameService;
		managementService = ServiceFactory.getManagementService(gameService.getGame());
		geoService = ServiceFactory.getGeoService(gameService.getGame());
		setTitle("Buy company to place at " + location);
		setBounds((int) GraphicTest.DEFAULT_DIALOG_LOCATION.getX(), (int) GraphicTest.DEFAULT_DIALOG_LOCATION.getY(), 540, 340);
		setModal(true);
		setLayout(null);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				setVisible(false);
				super.windowClosing(e);
			}

		});
		mount(location);
		setVisible(true);
	}

	protected void mount(final Location location) {
		FastPanel panel = new FastPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, getWidth(), getHeight());
		panel.addLabel("Select a formation to assign the company", GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN, GraphicTest.COLUMN_WIDTH_XXXLARGE, GraphicTest.ROW_HEIGHT);
		PurchaseCompanyTreePanel formationTree = new PurchaseCompanyTreePanel(this, gameService.getGame().getCurrentPlayerPosition(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH_XXLARGE,
				200);

		panel.add(formationTree.getPanel());
		modelsCB = new JComboBox<CompanyModel>();
		modelsCB.setBounds(
				GraphicTest.LEFT_MARGIN + formationTree.getPanel().getX() + formationTree.getPanel().getWidth(),
				formationTree.getPanel().getY(),
				GraphicTest.COLUMN_WIDTH_XXLARGE,
				GraphicTest.ROW_HEIGHT
				);
		modelsCB.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("unchecked")
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					JComboBox<CompanyModel> source = (JComboBox<CompanyModel>) arg0.getSource();
					int cost = managementService.purchaseCost(selectedFormation, (CompanyModel) source.getSelectedItem());
					costTF.setText(NumberFormats.PRESTIGE.format(cost));
					if (cost > gameService.getGame().getCurrentPlayerPosition().getPrestige()) {
						buyB.setEnabled(false);
						infoTF.setText("You do not have enough prestige to purchase that company");
					} else if (!selectedFormation.isExpandable()) {
						infoTF.setText("That formation is at supply limit, you cannot add more companies");
					} else {
						infoTF.setText("");
						buyB.setEnabled(true);
					}
				}
			}

		});
		panel.add(modelsCB);
		panel.addLabel("Supply Limit",
				GraphicTest.LEFT_MARGIN + formationTree.getPanel().getX() + formationTree.getPanel().getWidth(),
				modelsCB.getY() + modelsCB.getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		supplyLimitTF = new JTextField();
		supplyLimitTF.setEditable(false);
		supplyLimitTF.setBounds(
				GraphicTest.LEFT_MARGIN + formationTree.getPanel().getX() + formationTree.getPanel().getWidth() + GraphicTest.COLUMN_WIDTH,
				modelsCB.getY() + modelsCB.getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		panel.add(supplyLimitTF);
		panel.addLabel("Prestige Cost",
				GraphicTest.LEFT_MARGIN + formationTree.getPanel().getX() + formationTree.getPanel().getWidth(),
				supplyLimitTF.getY() + supplyLimitTF.getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		costTF = new JTextField();
		costTF.setEditable(false);
		costTF.setBounds(
				GraphicTest.LEFT_MARGIN + formationTree.getPanel().getX() + formationTree.getPanel().getWidth() + GraphicTest.COLUMN_WIDTH,
				supplyLimitTF.getY() + supplyLimitTF.getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		panel.add(costTF);
		infoTF = new JTextField();
		infoTF.setEditable(false);
		infoTF.setForeground(Color.RED);
		infoTF.setBounds(
				GraphicTest.LEFT_MARGIN + formationTree.getPanel().getX() + formationTree.getPanel().getWidth(),
				costTF.getY() + costTF.getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_XXLARGE,
				GraphicTest.ROW_HEIGHT);
		panel.add(infoTF);
		buyB = new JButton("Buy and place");
		buyB.setBounds(
				(panel.getWidth() - GraphicTest.COLUMN_WIDTH_LARGE) / 2,
				formationTree.getPanel().getY() + formationTree.getPanel().getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_LARGE,
				GraphicTest.ROW_HEIGHT);
		buyB.setEnabled(false);
		buyB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Company company = managementService.purchaseCompany(selectedFormation, (CompanyModel) modelsCB.getSelectedItem());
				geoService.deploy(company, location);
				parent.updateUnitsDisplay(location);
				dispose();
				setVisible(false);
			}
		});
		panel.add(buyB);
		add(panel);
	}

	@Override
	public void updateUnitDetails(Unit unit) {
		Formation formation = (Formation) unit;
		selectedFormation = formation;
		modelsCB.removeAllItems();
		for (CompanyModel model : managementService.getCompanyModelsFor(formation)) {
			modelsCB.addItem(model);
		}
		supplyLimitTF.setText(formation.getAllCompanies().size() + " / " + formation.getFormationLevel().getSupplyLimit());
	}

}

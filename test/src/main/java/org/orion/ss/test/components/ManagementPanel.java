package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.service.CombatService;
import org.orion.ss.service.ManagementService;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.utils.NumberFormats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagementPanel extends PlayerPanel {

	protected final static Logger logger = LoggerFactory.getLogger(ManagementPanel.class);

	private static final long serialVersionUID = -6731886729299310601L;

	private final ManagementService managementService;
	private final CombatService combatService;

	/* GUI components */
	private JPanel detailsPanel;
	private JTextField prestigeTF;
	private JButton button;
	private CompanyDetailPanel unitDetailPanel;
	private CompanyUpgradePanel unitUpgradeInfoPanel;
	private FastPanel reinforceInfoPanel;
	private FastPanel supplyCostPanel;
	private FormationDetailPanel formationDetailPanel;
	private FastPanel purchasePanel;
	private JComboBox<CompanyModel> purchaseCB;
	private JComboBox<FormationLevel> createCB;
	private JComboBox<TroopType> troopTypeCB;

	private PositionTreePanel treePanel;

	public ManagementPanel(GraphicTest parent, Game game) {
		super(parent, game);
		setBounds(GraphicTest.TAB_BOUNDS);
		setLayout(null);
		managementService = new ManagementService(game);
		combatService = new CombatService(game);
		mount();
	}

	public void updateTree() {
		this.remove(treePanel.getPanel());
		Rectangle bounds = treePanel.getPanel().getBounds();
		treePanel = new PositionTreePanel(this, game.getCurrentPlayerPosition(), (int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
		add(treePanel.getPanel());
		this.repaint();
	}

	@Override
	public void mount() {
		this.removeAll();
		addLabel("Administration, " + game.getCurrentPlayer().getEmail(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
		button = new JButton("End Turn");
		button.setBounds(GraphicTest.LEFT_MARGIN, 540, GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
		add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean gameEnded = parent.nextPlayer();
				if (gameEnded) {
					button.setEnabled(false);
					parent.endGame();
				} else {
					parent.updatePlayerPanel();
				}
			}

		});
		addLabel("Prestige", GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT, GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		prestigeTF = new JTextField();
		prestigeTF.setBounds(GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		prestigeTF.setEditable(false);
		prestigeTF.setText(NumberFormats.PRESTIGE.format(game.getCurrentPlayerPosition().getPrestige()));
		add(prestigeTF);
		treePanel = new PositionTreePanel(this, game.getCurrentPlayerPosition(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN
				* 3 + GraphicTest.ROW_HEIGHT * 2, GraphicTest.COLUMN_WIDTH_XLARGE, 435);
		add(treePanel.getPanel());
		/* variable */
		detailsPanel = new JPanel();
		detailsPanel.setLayout(null);
		add(detailsPanel);
		detailsPanel.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.TOP_MARGIN, 1200, 560);
	}

	private void mountFormationPanel() {
		detailsPanel.removeAll();
		formationDetailPanel = new FormationDetailPanel(managementService, this);
		formationDetailPanel.setLayout(null);
		formationDetailPanel.setBounds(0, 0, GraphicTest.COLUMN_WIDTH * 9 + GraphicTest.LEFT_MARGIN * 2, 360);
		detailsPanel.add(formationDetailPanel);
		reinforceInfoPanel = new FastPanel();
		reinforceInfoPanel.setBounds(0,
				formationDetailPanel.getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.LEFT_MARGIN * 4 + GraphicTest.COLUMN_WIDTH_NARROW * 2 + GraphicTest.COLUMN_WIDTH_XNARROW * 2,
				GraphicTest.TOP_MARGIN * 4 + GraphicTest.ROW_HEIGHT * 5);
		reinforceInfoPanel.setLayout(null);
		reinforceInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Reinforce Availability"));
		detailsPanel.add(reinforceInfoPanel);
		supplyCostPanel = new FastPanel();
		supplyCostPanel.setLayout(null);
		supplyCostPanel.setBounds(
				reinforceInfoPanel.getX() + reinforceInfoPanel.getWidth(),
				formationDetailPanel.getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_NARROW + GraphicTest.COLUMN_WIDTH_XNARROW,
				GraphicTest.TOP_MARGIN * 4 + GraphicTest.ROW_HEIGHT * 5);
		supplyCostPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Supply Costs"));
		detailsPanel.add(supplyCostPanel);
		purchasePanel = new FastPanel();
		purchasePanel.setLayout(null);
		purchasePanel.setBounds(
				supplyCostPanel.getX() + supplyCostPanel.getWidth(),
				formationDetailPanel.getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.LEFT_MARGIN + GraphicTest.RIGHT_MARGIN + GraphicTest.COLUMN_WIDTH * 2,
				GraphicTest.TOP_MARGIN * 4 + GraphicTest.ROW_HEIGHT * 5);
		purchasePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Purchase"));
		detailsPanel.add(purchasePanel);
		repaint();
	}

	private void updatePurchasePanel(final Formation formation) {
		purchaseCB = new JComboBox<CompanyModel>(formation.getCountry().getCompanyModels().toArray(new CompanyModel[] {}));
		purchaseCB.setBounds(
				GraphicTest.LEFT_MARGIN, 
				GraphicTest.TOP_MARGIN * 2, 
				GraphicTest.COLUMN_WIDTH * 2, 
				GraphicTest.ROW_HEIGHT);
		purchasePanel.add(purchaseCB);
		int purchaseCost = managementService.purchaseCost(formation, (CompanyModel)purchaseCB.getSelectedItem());
		JTextField purchaseCostTF = new JTextField(NumberFormats.PRESTIGE.format(purchaseCost));
		purchaseCostTF.setBounds(
				GraphicTest.LEFT_MARGIN, 
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT, 
				GraphicTest.COLUMN_WIDTH_NARROW, 
				GraphicTest.ROW_HEIGHT);
		purchasePanel.add(purchaseCostTF);
		JButton purchaseCompanyB = new JButton("Purchase");
		purchaseCompanyB.setBounds(
				purchasePanel.getWidth() - GraphicTest.COLUMN_WIDTH - GraphicTest.RIGHT_MARGIN,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		purchaseCompanyB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				managementService.purchaseCompany(formation, (CompanyModel)purchaseCB.getSelectedItem());
				updateDetails(formation);
				updatePrestigeTF();
				updateTree();
			}			
		});
		purchasePanel.add(purchaseCompanyB);
		purchaseCompanyB.setEnabled(formation.isExpandable() && (purchaseCB.getSelectedItem()!=null) && (game.getCurrentPlayerPosition().getPrestige()>=purchaseCost));
		createCB = new JComboBox<FormationLevel>(managementService.getSubordinables(formation).toArray(new FormationLevel[]{}));
		createCB.setBounds(				
				GraphicTest.LEFT_MARGIN, 
				GraphicTest.TOP_MARGIN * 4 + GraphicTest.ROW_HEIGHT * 2, 
				GraphicTest.COLUMN_WIDTH * 2, 
				GraphicTest.ROW_HEIGHT);
		purchasePanel.add(createCB);
		JButton createFormationB = new JButton("Create");
		createFormationB.setBounds(
				purchasePanel.getWidth() - GraphicTest.COLUMN_WIDTH - GraphicTest.RIGHT_MARGIN,
				GraphicTest.TOP_MARGIN * 5 + GraphicTest.ROW_HEIGHT * 3,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		purchasePanel.add(createFormationB);
		createFormationB.setEnabled(formation.isExpandable() && (createCB.getSelectedItem()!=null));
		troopTypeCB = new JComboBox<TroopType>(managementService.getSubordinateTroopTypes(formation).toArray(new TroopType[]{}));
		troopTypeCB.setBounds(
				GraphicTest.LEFT_MARGIN, 
				GraphicTest.TOP_MARGIN * 5 + GraphicTest.ROW_HEIGHT * 3,
				GraphicTest.COLUMN_WIDTH, 
				GraphicTest.ROW_HEIGHT);
		purchaseCostTF.setEditable(formation.isExpandable() && (createCB.getSelectedItem()!=null) && (troopTypeCB.getSelectedItem()!=null));
		purchasePanel.add(troopTypeCB);
		createFormationB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				managementService.createFormation(formation, (FormationLevel)createCB.getSelectedItem(), (TroopType)troopTypeCB.getSelectedItem());
				updateDetails(formation);
				updatePrestigeTF();
				updateTree();				
			}
			
		});

	}

	private void mountUnitDetailsPanel() {
		detailsPanel.removeAll();
		unitDetailPanel = new CompanyDetailPanel(managementService, combatService, this);
		unitDetailPanel.setLayout(null);
		unitDetailPanel.setBounds(0, 0, GraphicTest.COLUMN_WIDTH * 2 + GraphicTest.COLUMN_WIDTH_LARGE * 2 + GraphicTest.LEFT_MARGIN, 560);
		detailsPanel.add(unitDetailPanel);

		reinforceInfoPanel = new FastPanel();
		reinforceInfoPanel.setBounds(
				unitDetailPanel.getWidth() + unitDetailPanel.getX(),
				0,
				GraphicTest.LEFT_MARGIN * 4 + GraphicTest.COLUMN_WIDTH_NARROW * 2 + GraphicTest.COLUMN_WIDTH_XNARROW * 2,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT * 5);
		reinforceInfoPanel.setLayout(null);
		detailsPanel.add(reinforceInfoPanel);

		supplyCostPanel = new FastPanel();
		supplyCostPanel.setLayout(null);
		supplyCostPanel.setBounds(
				reinforceInfoPanel.getX() + reinforceInfoPanel.getWidth(),
				0,
				GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_NARROW + GraphicTest.COLUMN_WIDTH_XNARROW,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT * 5);
		supplyCostPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Supply Costs"));
		detailsPanel.add(supplyCostPanel);

		unitUpgradeInfoPanel = new CompanyUpgradePanel(managementService, combatService);
		unitUpgradeInfoPanel.setLayout(null);
		unitUpgradeInfoPanel.setBounds(GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH * 2
				+ GraphicTest.COLUMN_WIDTH_LARGE * 2, GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * 6,
				GraphicTest.COLUMN_WIDTH * 2 + GraphicTest.COLUMN_WIDTH_LARGE * 2, 400);
		detailsPanel.add(unitUpgradeInfoPanel);
		repaint();
	}

	public void updateSupplyCostPanel() {
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Reinforce Availability");
		reinforceInfoPanel.setBorder(title);
		int i = 0;
		for (SupplyType type : game.getCurrentPlayerPosition().getCountry().getMarket().keySet()) {
			supplyCostPanel.addLabel(type.getDenomination(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
			supplyCostPanel.addNotEditableTextField("" + game.getCurrentPlayerPosition().getCountry().getMarket().get(type), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.ROW_HEIGHT);
			i++;
		}

	}

	public void updateReinforceInfoPanel() {
		double[] firstColumnValues = { 1.0d, 2.0d, 2.5d, 3.0d, 3.5d };
		double[] secondColumnValues = { 4.0d, 4.25d, 4.5d, 4.75d, 5.0d };
		for (int i = 0; i < 5; i++) {
			reinforceInfoPanel.addLabel(NumberFormats.XP.format(firstColumnValues[i]), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
			reinforceInfoPanel.addNotEditableTextField("" + managementService.getReinforceAvailability(firstColumnValues[i], game.getCurrentPlayerPosition()), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
					GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.ROW_HEIGHT);
			reinforceInfoPanel.addLabel(NumberFormats.XP.format(secondColumnValues[i]), GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_NARROW + GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
			reinforceInfoPanel.addNotEditableTextField("" + managementService.getReinforceAvailability(secondColumnValues[i], game.getCurrentPlayerPosition()), GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_NARROW * 2 + GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.TOP_MARGIN * 2
					+ GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.ROW_HEIGHT);
		}

	}

	public void updateDetails(final Company company) {
		mountUnitDetailsPanel();
		unitDetailPanel.updateUnitPanel(company);
		updateReinforceInfoPanel();
		updateSupplyCostPanel();
	}

	public void updateDetails(final Formation formation) {
		mountFormationPanel();
		formationDetailPanel.update(formation);
		updateReinforceInfoPanel();
		updateSupplyCostPanel();
		updatePurchasePanel(formation);
	}

	public void updateUpgradeUnitPanel(final Company company, final CompanyModel upgradeModel) {
		unitUpgradeInfoPanel.update(company, upgradeModel);
	}

	protected void updatePrestigeTF() {
		prestigeTF.setText(NumberFormats.PRESTIGE.format(game.getCurrentPlayerPosition().getPrestige()));
	}

}

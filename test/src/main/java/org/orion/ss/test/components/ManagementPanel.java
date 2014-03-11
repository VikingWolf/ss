package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
import org.orion.ss.service.CombatService;
import org.orion.ss.service.GameService;
import org.orion.ss.service.ManagementService;
import org.orion.ss.service.ServiceFactory;
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
	private JButton endTurnB;
	private CompanyDetailPanel unitDetailPanel;
	private CompanyUpgradePanel unitUpgradeInfoPanel;
	private FastPanel reinforceInfoPanel;
	private FastPanel supplyCostPanel;
	private FormationDetailPanel formationDetailPanel;
	private FastPanel purchasePanel;
	private FastPanel createPanel;
	private JComboBox<CompanyModel> purchaseUnitModelCB;
	private JComboBox<CompanyModel> formationUnitModelCB;
	private JComboBox<FormationLevel> createFormationLevelCB;
	private JComboBox<TroopType> createTroopTypeCB;
	private JTextField purchaseCostTF;
	private JTextField createCostTF;

	private ManagementTreePanel treePanel;

	public ManagementPanel(GraphicTest parent, GameService gameService) {
		super(parent, gameService);
		setBounds(GraphicTest.TAB_BOUNDS);
		setLayout(null);
		managementService = ServiceFactory.getManagementService(getGame());
		combatService = ServiceFactory.getCombatService(getGame());
		mount();
	}

	public void updateTree() {
		this.remove(treePanel.getPanel());
		Rectangle bounds = treePanel.getPanel().getBounds();
		treePanel = new ManagementTreePanel(this, getGame().getCurrentPlayerPosition(), (int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
		add(treePanel.getPanel());
		this.repaint();
	}

	@Override
	public void mount() {
		this.removeAll();
		addLabel("Administration, " + getGame().getCurrentPlayer().getEmail(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
		endTurnB = new JButton("End Management");
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
		addLabel("Prestige", GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT, GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		prestigeTF = new JTextField();
		prestigeTF.setBounds(GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		prestigeTF.setEditable(false);
		prestigeTF.setText(NumberFormats.PRESTIGE.format(getGame().getCurrentPlayerPosition().getPrestige()));
		add(prestigeTF);
		treePanel = new ManagementTreePanel(this, getGame().getCurrentPlayerPosition(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN
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
				GraphicTest.TOP_MARGIN * 4 + GraphicTest.ROW_HEIGHT * 6);
		reinforceInfoPanel.setLayout(null);
		reinforceInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Reinforce Availability"));
		detailsPanel.add(reinforceInfoPanel);
		supplyCostPanel = new FastPanel();
		supplyCostPanel.setLayout(null);
		supplyCostPanel.setBounds(
				reinforceInfoPanel.getX() + reinforceInfoPanel.getWidth(),
				formationDetailPanel.getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_NARROW + GraphicTest.COLUMN_WIDTH_XNARROW,
				GraphicTest.TOP_MARGIN * 4 + GraphicTest.ROW_HEIGHT * 6);
		supplyCostPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Supply Costs"));
		detailsPanel.add(supplyCostPanel);
		purchasePanel = new FastPanel();
		purchasePanel.setLayout(null);
		purchasePanel.setBounds(
				supplyCostPanel.getX() + supplyCostPanel.getWidth(),
				formationDetailPanel.getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.LEFT_MARGIN + GraphicTest.RIGHT_MARGIN + GraphicTest.COLUMN_WIDTH * 2,
				GraphicTest.TOP_MARGIN * 4 + GraphicTest.ROW_HEIGHT * 6);
		purchasePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Purchase"));
		detailsPanel.add(purchasePanel);
		createPanel = new FastPanel();
		createPanel.setLayout(null);
		createPanel.setBounds(
				purchasePanel.getX() + purchasePanel.getWidth(),
				formationDetailPanel.getHeight() + GraphicTest.TOP_MARGIN,
				GraphicTest.LEFT_MARGIN + GraphicTest.RIGHT_MARGIN + GraphicTest.COLUMN_WIDTH * 2,
				GraphicTest.TOP_MARGIN * 4 + GraphicTest.ROW_HEIGHT * 6);
		createPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "New Formation"));
		detailsPanel.add(createPanel);
		repaint();
	}

	private void updatePurchasePanel(final Formation formation) {
		purchasePanel.addLabel("Prestige cost", GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		purchaseUnitModelCB = new JComboBox<CompanyModel>(managementService.getCompanyModelsFor(formation).toArray(new CompanyModel[] {}));
		purchaseUnitModelCB.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2,
				GraphicTest.COLUMN_WIDTH * 2,
				GraphicTest.ROW_HEIGHT);
		purchasePanel.add(purchaseUnitModelCB);
		purchaseCostTF = new JTextField(NumberFormats.PRESTIGE.format(managementService.purchaseCost(formation, (CompanyModel) purchaseUnitModelCB.getSelectedItem())));
		purchaseCostTF.setBounds(
				purchasePanel.getWidth() - GraphicTest.COLUMN_WIDTH - GraphicTest.RIGHT_MARGIN,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		purchaseCostTF.setEditable(false);
		purchasePanel.add(purchaseCostTF);
		JButton purchaseCompanyB = new JButton("Purchase");
		purchaseCompanyB.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 6 + GraphicTest.ROW_HEIGHT * 4,
				purchasePanel.getWidth() - GraphicTest.LEFT_MARGIN - GraphicTest.RIGHT_MARGIN,
				GraphicTest.ROW_HEIGHT);
		purchaseCompanyB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				managementService.purchaseCompany(formation, (CompanyModel) purchaseUnitModelCB.getSelectedItem());
				updateDetails(formation);
				updatePrestigeTF();
				updateTree();
			}
		});
		purchaseUnitModelCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					purchaseCostTF.setText("" + managementService.purchaseCost(formation, (CompanyModel) purchaseUnitModelCB.getSelectedItem()));
				}
			}
		});
		purchasePanel.add(purchaseCompanyB);
		purchaseCompanyB.setEnabled(formation.isExpandable() && (purchaseUnitModelCB.getSelectedItem() != null) && (getGame().getCurrentPlayerPosition().getPrestige() >= Integer.parseInt(purchaseCostTF.getText())));
	}

	protected void updateCreatePanel(final Formation formation) {
		logger.error("updating create panel");
		createPanel.addLabel("Prestige cost", GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 4 + GraphicTest.ROW_HEIGHT * 2, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		createPanel.addLabel("Troop Type", GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 5 + GraphicTest.ROW_HEIGHT * 3, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		createFormationLevelCB = new JComboBox<FormationLevel>(managementService.getSubordinables(formation).toArray(new FormationLevel[] {}));
		createFormationLevelCB.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2,
				GraphicTest.COLUMN_WIDTH * 2,
				GraphicTest.ROW_HEIGHT);
		createPanel.add(createFormationLevelCB);
		formationUnitModelCB = new JComboBox<CompanyModel>(managementService.getCompanyModelsFor(formation).toArray(new CompanyModel[] {}));
		formationUnitModelCB.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT * 1,
				GraphicTest.COLUMN_WIDTH * 2,
				GraphicTest.ROW_HEIGHT);
		createPanel.add(formationUnitModelCB);
		int createCost = managementService.createFormationCost(formation, (CompanyModel) purchaseUnitModelCB.getSelectedItem(), (FormationLevel) createFormationLevelCB.getSelectedItem());
		createCostTF = new JTextField(NumberFormats.PRESTIGE.format(createCost));
		createCostTF.setBounds(
				purchasePanel.getWidth() - GraphicTest.COLUMN_WIDTH - GraphicTest.RIGHT_MARGIN,
				GraphicTest.TOP_MARGIN * 4 + GraphicTest.ROW_HEIGHT * 2,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		createCostTF.setEditable(false);
		createPanel.add(createCostTF);
		createTroopTypeCB = new JComboBox<TroopType>(managementService.getSubordinateTroopTypes(formation).toArray(new TroopType[] {}));
		createTroopTypeCB.setBounds(
				purchasePanel.getWidth() - GraphicTest.COLUMN_WIDTH - GraphicTest.RIGHT_MARGIN,
				GraphicTest.TOP_MARGIN * 5 + GraphicTest.ROW_HEIGHT * 3,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		createPanel.add(createTroopTypeCB);
		JButton createFormationB = new JButton("Create");
		createFormationB.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 6 + GraphicTest.ROW_HEIGHT * 4,
				createPanel.getWidth() - GraphicTest.LEFT_MARGIN - GraphicTest.RIGHT_MARGIN,
				GraphicTest.ROW_HEIGHT);
		createPanel.add(createFormationB);
		createFormationB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				managementService.createFormation(formation, (CompanyModel) formationUnitModelCB.getSelectedItem(), (FormationLevel) createFormationLevelCB.getSelectedItem(), (TroopType) createTroopTypeCB.getSelectedItem());
				updateDetails(formation);
				updatePrestigeTF();
				updateTree();
			}
		});
		class CreateCostListener implements ItemListener {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					createCostTF.setText("" + managementService.createFormationCost(formation, (CompanyModel) formationUnitModelCB.getSelectedItem(), (FormationLevel) createFormationLevelCB.getSelectedItem()));
				}
			}
		}
		CreateCostListener createCostListener = new CreateCostListener();
		createFormationLevelCB.addItemListener(createCostListener);
		formationUnitModelCB.addItemListener(createCostListener);
		createFormationB.setEnabled(formation.isExpandable()
				&& (createFormationLevelCB.getSelectedItem() != null)
				&& managementService.createFormationCost(formation, (CompanyModel) formationUnitModelCB.getSelectedItem(), (FormationLevel) createFormationLevelCB.getSelectedItem()) <= formation.getPosition().getPrestige());
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
		for (SupplyType type : getGame().getCurrentPlayerPosition().getCountry().getMarket().keySet()) {
			supplyCostPanel.addLabel(type.getDenomination(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
			supplyCostPanel.addNotEditableTextField("" + getGame().getCurrentPlayerPosition().getCountry().getMarket().get(type), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.ROW_HEIGHT);
			i++;
		}

	}

	public void updateReinforceInfoPanel() {
		double[] firstColumnValues = { 1.0d, 2.0d, 2.5d, 3.0d, 3.5d };
		double[] secondColumnValues = { 4.0d, 4.25d, 4.5d, 4.75d, 5.0d };
		for (int i = 0; i < 5; i++) {
			reinforceInfoPanel.addLabel(NumberFormats.XP.format(firstColumnValues[i]), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
			reinforceInfoPanel.addNotEditableTextField("" + managementService.getReinforceAvailability(firstColumnValues[i], getGame().getCurrentPlayerPosition()), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
					GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.ROW_HEIGHT);
			reinforceInfoPanel.addLabel(NumberFormats.XP.format(secondColumnValues[i]), GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_NARROW + GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
			reinforceInfoPanel.addNotEditableTextField("" + managementService.getReinforceAvailability(secondColumnValues[i], getGame().getCurrentPlayerPosition()), GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_NARROW * 2 + GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.TOP_MARGIN * 2
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
		updateCreatePanel(formation);
	}

	public void updateUpgradeUnitPanel(final Company company, final CompanyModel upgradeModel) {
		unitUpgradeInfoPanel.update(company, upgradeModel);
	}

	protected void updatePrestigeTF() {
		prestigeTF.setText(NumberFormats.PRESTIGE.format(getGame().getCurrentPlayerPosition().getPrestige()));
	}

}

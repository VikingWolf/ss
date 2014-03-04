package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.AttackSet;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Defense;
import org.orion.ss.model.impl.WeaponModel;
import org.orion.ss.service.CombatService;
import org.orion.ss.service.ManagementService;
import org.orion.ss.service.ReinforceCost;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.utils.NumberFormats;

public class CompanyDetailPanel extends FastPanel {

	private static final long serialVersionUID = 2165976278365302905L;

	private JButton upgradeB;
	private JTextField costTF;
	private JComboBox<CompanyModel> upgradesCB;
	private JButton eliteReinforceB;
	private JButton regularReinforceB;
	private JTextField costEliteReinforceTF;
	private JTextField costRegularReinforceTF;
	private JButton resupplyB;
	private JTextField resupplyTF;

	private final ManagementService managementService;
	private final CombatService combatService;

	private final ManagementPanel parent;

	public CompanyDetailPanel(ManagementService managementService, CombatService combatService, ManagementPanel parent) {
		super();
		this.managementService = managementService;
		this.combatService = combatService;
		this.parent = parent;
	}

	protected void displayUnitInfo(final Company company) {
		/* First column */
		List<String> labels1 = new ArrayList<String>();
		labels1.add("Type");
		labels1.add("Model");
		labels1.add("Mobility");
		labels1.add("Speed");
		labels1.add("Initiative");
		labels1.add("Strength");
		labels1.add("Organization");
		labels1.add("Morale");
		labels1.add("Experience");
		List<String> textfields1 = new ArrayList<String>();
		textfields1.add(company.getModel().getType().getDenomination());
		textfields1.add(company.getModel().getCode());
		textfields1.add(company.getModel().getMobility().getDenomination());
		textfields1.add(NumberFormats.DF_2.format(company.getModel().getSpeed()) + " km/h");
		textfields1.add(NumberFormats.DF_2.format(company.computeInitiative()));
		textfields1.add((int) (company.getStrength() * company.getModel().getMaxStrength()) + " / " + company.getModel().getMaxStrength());
		textfields1.add(NumberFormats.PERCENT.format(company.getOrganization()));
		textfields1.add(NumberFormats.MORALE.format(company.getMorale()));
		textfields1.add(NumberFormats.XP.format(company.getExperience()));
		for (Defense defense : combatService.computeDefenses(company)) {
			labels1.add(defense.getType().getDenomination());
			textfields1.add("" + NumberFormats.DF_2.format(defense.getStrength()));
		}
		for (SupplyType stock : company.getMaxSupplies().keySet()) {
			labels1.add(stock.getDenomination()+" Supply");
			textfields1.add(NumberFormats.DF_3.format(company.getSupplies().get(stock)) + " / " + NumberFormats.DF_3.format(company.getMaxSupplies().get(stock)));
		}
		for (int i = 0; i < labels1.size(); i++) {
			addLabel(labels1.get(i), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
					GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
			addNotEditableTextField(textfields1.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN
					* 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		}
		/* Second Column */
		addLabel("Equipment Prestige Value", GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE,
				GraphicTest.TOP_MARGIN * 2, GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		addNotEditableTextField(NumberFormats.PRESTIGE.format(managementService.getValue(company.getModel())),
				GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE * 2, GraphicTest.TOP_MARGIN * 2,
				GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
		List<String> labels2 = new ArrayList<String>();
		labels2.add("Equipment");
		for (WeaponModel weaponModel : company.getWeaponry().keySet()) {
			labels2.add(company.getWeaponry().get(weaponModel) + " x " + weaponModel.getDenomination());
		}
		for (int i = 0; i < labels2.size(); i++) {
			addLabel(labels2.get(i), GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE,
					GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (i + 1), GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		}
		/* Attacks */
		addLabel("Consumption", GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (labels1.size() - 1), GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		AttackSet attacks = combatService.computeAttacks(company);
		for (int i = 0; i < attacks.size(); i++) {
			Attack attack = attacks.get(i);
			String supplyConsumption = "";
			for (SupplyType supplyType : attack.getConsumption().keySet()) {
				supplyConsumption += supplyType.getDenomination() + "=" + NumberFormats.DF_3.format(attack.getConsumption().get(supplyType)) + ", ";
			}
			if (supplyConsumption.length() > 0) {
				supplyConsumption = supplyConsumption.substring(0, supplyConsumption.length() - 2);
			}
			addLabel(attack.getType().getDenomination(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT
					* (labels1.size() + i), GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
			addNotEditableTextField(NumberFormats.DF_2.format(attack.getStrength()) + " at " + attack.getRange() + " km",
					GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (labels1.size() + i),
					GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
			addNotEditableTextField(supplyConsumption, GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH
					+ GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (labels1.size() + i),
					GraphicTest.COLUMN_WIDTH_XXLARGE, GraphicTest.ROW_HEIGHT);
		}
	}

	protected void updateUnitPanel(final Company company) {
		parent.updatePrestigeTF();
		removeAll();
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), company.getId());
		setBorder(title);
		displayUnitInfo(company);
		/* Actions */
		JLabel upgradesL = new JLabel("Upgrades");
		upgradesL.setBounds(GraphicTest.LEFT_MARGIN, 445, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
		add(upgradesL);
		CompanyModel[] models = new CompanyModel[0];
		upgradesCB = new JComboBox<CompanyModel>(company.getModel().getUpgrades().toArray(models));
		costTF = new JTextField();
		costTF.setBounds(GraphicTest.LEFT_MARGIN * 3 + GraphicTest.COLUMN_WIDTH_XLARGE + GraphicTest.COLUMN_WIDTH_NARROW, 445,
				GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
		add(costTF);
		upgradeB = new JButton("Commit Upgrade");
		upgradeB.setBounds(GraphicTest.LEFT_MARGIN * 4 + GraphicTest.COLUMN_WIDTH_XLARGE + GraphicTest.COLUMN_WIDTH_NARROW * 2, 445,
				GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		add(upgradeB);
		this.updateUpgradeArea(company, (CompanyModel) upgradesCB.getSelectedItem());
		upgradeB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				CompanyModel model = (CompanyModel) upgradesCB.getSelectedItem();
				managementService.upgrade(company, model);
				updateUnitPanel(company);
			}

		});
		upgradesCB.addItemListener(new ItemListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void itemStateChanged(ItemEvent e) {
				System.out.println("selected");
				if (e.getStateChange() == ItemEvent.SELECTED) {
					JComboBox<CompanyModel> source = (JComboBox<CompanyModel>) e.getSource();
					updateUpgradeArea(company, (CompanyModel) source.getSelectedItem());
				}
			}
		});
		upgradesCB.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_NARROW, 445, GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		add(upgradesCB);
		final ReinforceCost eliteCost = managementService.eliteReinforceCost(company);
		final ReinforceCost regularCost = managementService.regularReinforceCost(company);
		eliteReinforceB = new JButton("Elite Reinforce");
		eliteReinforceB.setBounds(GraphicTest.LEFT_MARGIN, 445 + GraphicTest.ROW_HEIGHT + (int) (GraphicTest.TOP_MARGIN * 1.5d),
				GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		add(eliteReinforceB);
		regularReinforceB = new JButton("Regular Reinforce");
		regularReinforceB.setBounds(GraphicTest.LEFT_MARGIN, 480 + GraphicTest.ROW_HEIGHT + (int) (GraphicTest.TOP_MARGIN * 1.5d),
				GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		add(regularReinforceB);
		costEliteReinforceTF = new JTextField();
		costEliteReinforceTF.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_LARGE, 445 + GraphicTest.ROW_HEIGHT
				+ (int) (GraphicTest.TOP_MARGIN * 1.5d), GraphicTest.COLUMN_WIDTH_XLARGE + 5, GraphicTest.ROW_HEIGHT);
		costEliteReinforceTF.setEditable(false);
		add(costEliteReinforceTF);
		costRegularReinforceTF = new JTextField();
		costRegularReinforceTF.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_LARGE, 480 + GraphicTest.ROW_HEIGHT
				+ (int) (GraphicTest.TOP_MARGIN * 1.5d), GraphicTest.COLUMN_WIDTH_XLARGE + 5, GraphicTest.ROW_HEIGHT);
		costRegularReinforceTF.setEditable(false);
		add(costRegularReinforceTF);
		eliteReinforceB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				managementService.eliteReinforce(company, eliteCost);
				updateUnitPanel(company);
			}

		});
		regularReinforceB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				managementService.regularReinforce(company, regularCost);
				updateUnitPanel(company);
			}

		});
		resupplyB = new JButton("Re-supply");
		resupplyB.setBounds(GraphicTest.LEFT_MARGIN * 3 + GraphicTest.COLUMN_WIDTH_LARGE + GraphicTest.COLUMN_WIDTH_XLARGE + 10, 445 + GraphicTest.ROW_HEIGHT + (int) (GraphicTest.TOP_MARGIN * 1.5d),
				GraphicTest.COLUMN_WIDTH_LARGE , GraphicTest.ROW_HEIGHT);
		add(resupplyB);
		resupplyTF = new JTextField();
		resupplyTF.setBounds(GraphicTest.LEFT_MARGIN * 3 + GraphicTest.COLUMN_WIDTH_LARGE + GraphicTest.COLUMN_WIDTH_XLARGE + 10, 480 + GraphicTest.ROW_HEIGHT
				+ (int) (GraphicTest.TOP_MARGIN * 1.5d), GraphicTest.COLUMN_WIDTH_LARGE , GraphicTest.ROW_HEIGHT);
		int resupplyCost = managementService.resupplyCost(company);
		resupplyTF.setText("" + resupplyCost);
		add(resupplyTF);
		resupplyB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				managementService.resupply(company);
				updateUnitPanel(company);				
			}
			
		});
		
		updateReinforcementArea(company, regularCost, eliteCost, resupplyCost);
	}

	public void updateUpgradeArea(Company target, CompanyModel upgrade) {
		upgradeB.setEnabled(upgrade != null && target.getStrength() >= 1.0d);
		if (upgrade != null) {
			int upgradeCost = managementService.upgradeCost(target, upgrade);
			costTF.setText("" + upgradeCost);
			upgradeB.setEnabled(target.getPosition().getPrestige() >= upgradeCost && upgradeB.isEnabled());
		}
		parent.updateUpgradeUnitPanel(target, upgrade);
	}

	protected void updateReinforcementArea(Company company, ReinforceCost regularCost, ReinforceCost eliteCost, int resupplyCost) {
		if (company.getStrength() < 1.0d) {
			if (company.getExperience() > 1.0d) {
				costEliteReinforceTF.setText("costs " + eliteCost.getCost() + " for " + NumberFormats.PERCENT.format(eliteCost.getStrength()) + " strength");
			}
			costRegularReinforceTF.setText("costs " + regularCost.getCost() + " for " + NumberFormats.PERCENT.format(regularCost.getStrength()) + " strength");
		}
		eliteReinforceB.setEnabled(company.getStrength() < 1.0d && company.getExperience() > 1.0d && company.getPosition().getPrestige() >= eliteCost.getCost());
		regularReinforceB.setEnabled(company.getStrength() < 1.0d && company.getPosition().getPrestige() >= regularCost.getCost());
		resupplyB.setEnabled((managementService.resupplyCost(company)>0 && (company.getPosition().getPrestige()>=managementService.resupplyCost(company))));
		resupplyTF.setText("" + managementService.resupplyCost(company));
	}

}

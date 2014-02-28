package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.impl.AirSquadron;
import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Defense;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.WeaponModel;
import org.orion.ss.service.GameService;
import org.orion.ss.service.ManagementService;
import org.orion.ss.service.ReinforceCost;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.utils.NumberFormats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerPanel extends FastPanel implements Observer {

	protected final static Logger logger = LoggerFactory.getLogger(PlayerPanel.class);
	
	private static final long serialVersionUID = -6731886729299310601L;

	private final Game game;

	private final GraphicTest parent;

	private final ManagementService managementService;

	/* GUI components */
	private JTextField prestigeTF;
	private JButton button;
	private FastPanel unitDetailPanel;
	private JTextField costTF;
	private JButton upgradeB;
	private JButton eliteReinforceB;
	private JButton regularReinforceB;
	private JTextField costEliteReinforceTF;
	private JTextField costRegularReinforceTF;

	public PlayerPanel(GraphicTest parent, Game game) {
		super();
		this.game = game;
		this.parent = parent;
		setBounds(GraphicTest.TAB_BOUNDS);
		setLayout(null);
		managementService = new ManagementService(game);
	}

	protected void mount() {
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
				}
			}

		});
		addLabel("Prestige", GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
		prestigeTF = new JTextField();
		prestigeTF.setBounds(GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		prestigeTF.setEditable(false);
		prestigeTF.setText(NumberFormats.PRESTIGE.format(game.getCurrentPlayerPosition().getPrestige()));
		add(prestigeTF);
		PositionTreePanel treePanel = new PositionTreePanel(this, game.getCurrentPlayerPosition(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN
				* 3 + GraphicTest.ROW_HEIGHT * 2, GraphicTest.COLUMN_WIDTH_XLARGE, 435);
		add(treePanel.getPanel());
		unitDetailPanel = new FastPanel();
		unitDetailPanel.setLayout(null);
		unitDetailPanel.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.TOP_MARGIN, GraphicTest.COLUMN_WIDTH * 2
				+ GraphicTest.COLUMN_WIDTH_LARGE * 2 + GraphicTest.LEFT_MARGIN, 560);
		add(unitDetailPanel);
		repaint();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		mount();
	}

	public void updateUnitPanel(final Company company) {
		prestigeTF.setText(NumberFormats.PRESTIGE.format(game.getCurrentPlayerPosition().getPrestige()));
		unitDetailPanel.removeAll();
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), company.getId());
		unitDetailPanel.setBorder(title);
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
		textfields1.add((int) (company.getStrength() * company.getModel().getMaxStrength()) + "/ " + company.getModel().getMaxStrength());
		textfields1.add(NumberFormats.PERCENT.format(company.getOrganization()));
		textfields1.add(NumberFormats.MORALE.format(company.getMorale()));
		textfields1.add(NumberFormats.XP.format(company.getExperience()));
		for (Defense defense : company.computeDefenses()) {
			labels1.add(defense.getType().getDenomination());
			textfields1.add("" + NumberFormats.DF_2.format(defense.getStrength()));
		}
		for (SupplyType stock : company.getMaxSupplies().keySet()) {
			labels1.add("Max " + stock.getDenomination());
			textfields1.add(NumberFormats.DF_4.format(company.getMaxSupplies().get(stock)));
		}
		for (int i = 0; i < labels1.size(); i++) {
			unitDetailPanel.addLabel(labels1.get(i), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
			unitDetailPanel.addNotEditableTextField(textfields1.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		}
		/* Second Column */
		unitDetailPanel.addLabel("Equipment Prestige Value", GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.TOP_MARGIN * 2, GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		unitDetailPanel.addNotEditableTextField(NumberFormats.PRESTIGE.format(managementService.getValue(company.getModel())), GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE * 2, GraphicTest.TOP_MARGIN * 2, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
		List<String> labels2 = new ArrayList<String>();
		labels2.add("Equipment");
		for (WeaponModel weaponModel : company.getWeaponry().keySet()) {
			labels2.add(company.getWeaponry().get(weaponModel) + " x " + weaponModel.getDenomination());
		}
		for (int i = 0; i < labels2.size(); i++) {
			unitDetailPanel.addLabel(labels2.get(i), GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (i + 1), GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		}
		/* Attacks */
		unitDetailPanel.addLabel("Consumption", GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (labels1.size() - 1), GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		for (int i = 0; i < company.computeAttacks().size(); i++) {
			Attack attack = company.computeAttacks().get(i);
			String supplyConsumption = "";
			for (SupplyType supplyType: attack.getConsumption().keySet()){
				supplyConsumption += supplyType.getDenomination()+"="+NumberFormats.DF_4.format(attack.getConsumption().get(supplyType)) +", ";
			}
			if (supplyConsumption.length()>0){
				supplyConsumption = supplyConsumption.substring(0, supplyConsumption.length()-2);
			}
			unitDetailPanel.addLabel(attack.getType().getDenomination(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (labels1.size() + i), GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
			unitDetailPanel.addNotEditableTextField(NumberFormats.DF_2.format(attack.getStrength()) + " at " + attack.getRange() + " km", GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (labels1.size() + i), GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
			unitDetailPanel.addNotEditableTextField(supplyConsumption, GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (labels1.size() + i), GraphicTest.COLUMN_WIDTH_XXLARGE, GraphicTest.ROW_HEIGHT);

		}
		/* Actions */
		JLabel upgradesL = new JLabel("Upgrades");
		upgradesL.setBounds(GraphicTest.LEFT_MARGIN, 445, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
		unitDetailPanel.add(upgradesL);
		JComboBox upgradesCB = new JComboBox(company.getModel().getUpgrades().toArray());
		costTF = new JTextField();
		costTF.setBounds(GraphicTest.LEFT_MARGIN * 3 + GraphicTest.COLUMN_WIDTH_XLARGE + GraphicTest.COLUMN_WIDTH_NARROW, 445, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
		unitDetailPanel.add(costTF);
		upgradeB = new JButton("Commit Upgrade");
		upgradeB.setBounds(GraphicTest.LEFT_MARGIN * 4 + GraphicTest.COLUMN_WIDTH_XLARGE + GraphicTest.COLUMN_WIDTH_NARROW * 2, 445, GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		unitDetailPanel.add(upgradeB);
		this.updateUpgradeArea(company, (CompanyModel) upgradesCB.getSelectedItem());
		upgradesCB.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("selected");
				if (e.getStateChange()==ItemEvent.SELECTED){
					JComboBox source = (JComboBox) e.getSource();
					System.out.println("sel=" + source.getSelectedItem());
					updateUpgradeArea(company, (CompanyModel) source.getSelectedItem());
				}
			}
		});		
		upgradesCB.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_NARROW, 445, GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
		unitDetailPanel.add(upgradesCB);
		
		eliteReinforceB = new JButton("Elite Reinforce");
		eliteReinforceB.setBounds(GraphicTest.LEFT_MARGIN, 445 + GraphicTest.ROW_HEIGHT + (int)(GraphicTest.TOP_MARGIN * 1.5d), GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		unitDetailPanel.add(eliteReinforceB);
		regularReinforceB = new JButton("Regular Reinforce");
		regularReinforceB.setBounds(GraphicTest.LEFT_MARGIN, 480 + GraphicTest.ROW_HEIGHT + (int)(GraphicTest.TOP_MARGIN * 1.5d), GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		unitDetailPanel.add(regularReinforceB);		
		costEliteReinforceTF = new JTextField();
		costEliteReinforceTF.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_LARGE, 445 + GraphicTest.ROW_HEIGHT + (int)(GraphicTest.TOP_MARGIN * 1.5d), GraphicTest.COLUMN_WIDTH_XLARGE + 5, GraphicTest.ROW_HEIGHT);
		costEliteReinforceTF.setEditable(false);
		unitDetailPanel.add(costEliteReinforceTF);
		costRegularReinforceTF = new JTextField();
		costRegularReinforceTF.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_LARGE, 480 + GraphicTest.ROW_HEIGHT + (int)(GraphicTest.TOP_MARGIN * 1.5d), GraphicTest.COLUMN_WIDTH_XLARGE + 5, GraphicTest.ROW_HEIGHT);
		costRegularReinforceTF.setEditable(false);
		unitDetailPanel.add(costRegularReinforceTF);
		final ReinforceCost eliteCost = managementService.eliteReinforceCost(company);
		final ReinforceCost regularCost = managementService.regularReinforceCost(company);
		updateReinforcementArea(company, regularCost, eliteCost);
		eliteReinforceB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				managementService.eliteReinforce(company, eliteCost);
				updateReinforcementArea(company, regularCost, eliteCost);
				//TODO here
			}
			
		});
		regularReinforceB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				managementService.regularReinforce(company, regularCost);
				updateUnitPanel(company);				
			}
			
		});


		JButton reassignB = new JButton("Reassign Unit");
		reassignB.setBounds(GraphicTest.LEFT_MARGIN * 8 + GraphicTest.COLUMN_WIDTH_LARGE * 2, 445 + GraphicTest.ROW_HEIGHT + (int)(GraphicTest.TOP_MARGIN * 1.5d), GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		unitDetailPanel.add(reassignB);
	}

	public void updateUnitPanel(AirSquadron airSquadron) {
		// TODO montar panel
	}

	public void updateUpgradeArea(Company target, CompanyModel upgrade){
		logger.error("upgrade="+upgrade);
		upgradeB.setEnabled(upgrade!=null&&target.getStrength()>=1.0d);
		if (upgrade != null){
			int upgradeCost = managementService.upgradeCost(target, upgrade);
			costTF.setText("" + upgradeCost);
			upgradeB.setEnabled(target.getPosition().getPrestige()<upgradeCost&&upgradeB.isEnabled());			
		}
	}
	
	public void updateReinforcementArea(Company company, ReinforceCost regularCost, ReinforceCost eliteCost){
		if (company.getStrength()<1.0d){
			if (company.getExperience()>1.0d){
				costEliteReinforceTF.setText("costs " + eliteCost.getCost() + " for " + NumberFormats.PERCENT.format(eliteCost.getStrength()) + " strength"  );
			}			
			costRegularReinforceTF.setText("costs " + regularCost.getCost() + " for " + NumberFormats.PERCENT.format(regularCost.getStrength()) + " strength"  );
		} 
		eliteReinforceB.setEnabled(company.getStrength()<1.0d&&company.getExperience()>1.0d&&company.getPosition().getPrestige()>eliteCost.getCost());							
		regularReinforceB.setEnabled(company.getStrength()<1.0d&&company.getPosition().getPrestige()>regularCost.getCost());
		
	}
}

class ReinforceListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
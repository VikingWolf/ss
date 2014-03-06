package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.service.ManagementService;
import org.orion.ss.service.ReinforceCost;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.utils.NumberFormats;

public class FormationDetailPanel extends FastPanel {

	private static final long serialVersionUID = 4809452906948077694L;

	private final ManagementService managementService;

	private Formation formation;

	private final ManagementPanel playerPanel;

	public FormationDetailPanel(ManagementService managementService, ManagementPanel parent) {
		super();
		this.managementService = managementService;
		playerPanel = parent;
	}

	public ManagementPanel getPlayerPanel() {
		return playerPanel;
	}

	public Formation getFormation() {
		return formation;
	}

	public void setFormation(Formation formation) {
		this.formation = formation;
	}

	public void update(Formation target) {
		formation = target;
		removeAll();
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), formation.getName()));
		List<String> labels1 = new ArrayList<String>();
		labels1.add("Name");
		labels1.add("Size");
		labels1.add("Type");
		labels1.add("Strength");
		labels1.add("Reinforce Cost");
		labels1.add("");
		labels1.add("Re-supply cost");
		labels1.add("");
		for (int i = 0; i < labels1.size(); i++) {
			addLabel(labels1.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH * i, GraphicTest.TOP_MARGIN, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		}
		ReinforceCompanyListener reinforceCompanyListener = new ReinforceCompanyListener();
		ResupplyCompanyListener resupplyCompanyListener = new ResupplyCompanyListener();
		DismissCompanyListener dismissCompanyListener = new DismissCompanyListener();
		int j = 1;
		for (Company company : formation.getCompanies()) {
			List<Object> values1 = new ArrayList<Object>();
			values1.add(company.getName());
			values1.add(company.getModel().getFormationLevel() + "(" + company.getModel().getFormationLevel().getCode() + ")");
			values1.add(company.getModel().getType().getDenomination());
			values1.add("" + company.getAbsoluteStrength() + "/" + company.getModel().getMaxStrength());
			ReinforceCost regularCost = managementService.regularReinforceCost(company);
			values1.add(NumberFormats.PERCENT.format(regularCost.getStrength()) + " for " + NumberFormats.PRESTIGE.format(regularCost.getCost()));
			JButton reinforceB = new CompanyButton("Reinforce", company);
			ReinforceCost reinforceCost = managementService.regularReinforceCost(company);
			reinforceB.setEnabled(reinforceCost.getCost() > 0 && company.getPosition().getPrestige() >= reinforceCost.getCost());
			reinforceB.addActionListener(reinforceCompanyListener);
			values1.add(reinforceB);
			values1.add(NumberFormats.DF_3.format(managementService.resupplyCost(company)));
			JButton resupplyB = new CompanyButton("Re-supply", company);
			int resupplyCost = managementService.resupplyCost(company);
			resupplyB.setEnabled(resupplyCost > 0 && company.getPosition().getPrestige() >= resupplyCost);
			resupplyB.addActionListener(resupplyCompanyListener);
			values1.add(resupplyB);
			JButton dismissB = new CompanyButton("Dissmis", company);
			dismissB.addActionListener(dismissCompanyListener);
			values1.add(dismissB);
			for (int i = 0; i < values1.size(); i++) {
				if (values1.get(i) instanceof JButton) {
					JButton button = (JButton) values1.get(i);
					button.setBounds(
							GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH * i,
							GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * j,
							GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
					add(button);
				} else {
					addNotEditableTextField((String) values1.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH * i, GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * j, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
				}
			}
			j++;
		}
		ReinforceFormationListener reinforceFormationListener = new ReinforceFormationListener();
		ResupplyFormationListener resupplyFormationListener = new ResupplyFormationListener();
		DismissFormationListener dismissFormationListener = new DismissFormationListener();
		for (Formation subordinate : formation.getSubordinates()) {
			List<Object> values1 = new ArrayList<Object>();
			values1.add(subordinate.getName());
			values1.add("" + subordinate.getFormationLevel() + "(" + subordinate.getFormationLevel().getCode() + ")");
			values1.add(formation.getType().getDenomination());
			values1.add("" + subordinate.getAbsoluteStrength() + "/" + subordinate.getMaxStrength());
			values1.add(NumberFormats.PRESTIGE.format(managementService.regularReinforceCost(subordinate)));
			JButton reinforceB = new FormationButton("Reinforce", subordinate);
			values1.add(reinforceB);
			int reinforceCost = managementService.regularReinforceCost(subordinate);
			reinforceB.setEnabled(reinforceCost > 0 && subordinate.getPosition().getPrestige() >= reinforceCost);
			reinforceB.addActionListener(reinforceFormationListener);
			values1.add(NumberFormats.PRESTIGE.format(managementService.resupplyCost(subordinate)));
			JButton resupplyB = new FormationButton("Re-supply", subordinate);
			int resupplyCost = managementService.resupplyCost(subordinate);
			resupplyB.setEnabled(resupplyCost > 0 && formation.getPosition().getPrestige() >= resupplyCost);
			resupplyB.addActionListener(resupplyFormationListener);
			values1.add(resupplyB);
			JButton dismissB = new FormationButton("Dissmis", subordinate);
			dismissB.addActionListener(dismissFormationListener);
			values1.add(dismissB);
			for (int i = 0; i < values1.size(); i++) {
				if (values1.get(i) instanceof JButton) {
					JButton button = (JButton) values1.get(i);
					button.setBounds(
							GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH * i,
							GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * j,
							GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
					add(button);
				} else {
					addNotEditableTextField((String) values1.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH * i, GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * j, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
				}

			}
			j++;
		}
	}

	class CompanyButton extends JButton {

		private static final long serialVersionUID = -715559750005823160L;
		private final Company company;

		public CompanyButton(String text, Company company) {
			super(text);
			this.company = company;
		}

		public Company getCompany() {
			return company;
		}

	}

	class FormationButton extends JButton {

		private static final long serialVersionUID = -1836587057849149527L;
		private final Formation formation;

		public FormationButton(String text, Formation formation) {
			super(text);
			this.formation = formation;
		}

		public Formation getFormation() {
			return formation;
		}

	}

	class ReinforceCompanyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			CompanyButton source = (CompanyButton) e.getSource();
			managementService.regularReinforce(source.getCompany());
			update(getFormation());
			getPlayerPanel().updatePrestigeTF();
		}

	}

	class ResupplyCompanyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			CompanyButton source = (CompanyButton) e.getSource();
			managementService.resupply(source.getCompany());
			update(getFormation());
			getPlayerPanel().updatePrestigeTF();
		}

	}

	class DismissCompanyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			CompanyButton source = (CompanyButton) e.getSource();
			managementService.dismiss(source.getCompany());
			update(getFormation());
			getPlayerPanel().updateTree();
		}

	}

	class ReinforceFormationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			FormationButton source = (FormationButton) e.getSource();
			managementService.regularReinforce(source.getFormation());
			update(getFormation());
			getPlayerPanel().updatePrestigeTF();
		}

	}

	class ResupplyFormationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			FormationButton source = (FormationButton) e.getSource();
			managementService.resupply(source.getFormation());
			update(getFormation());
			getPlayerPanel().updatePrestigeTF();
		}

	}

	class DismissFormationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			FormationButton source = (FormationButton) e.getSource();
			managementService.dismiss(source.getFormation());
			update(getFormation());
			getPlayerPanel().updateTree();
		}
	}

}

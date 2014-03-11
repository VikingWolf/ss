package org.orion.ss.test.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.UnitStack;
import org.orion.ss.service.GraphService;
import org.orion.ss.service.ServiceFactory;
import org.orion.ss.utils.NumberFormats;

class UnitStackPanel extends JPanel {

	private static final long serialVersionUID = -617645392072460358L;

	private final GraphService graphService;
	private int symbolSize = 0;
	private UnitStack unitStack;

	public UnitStackPanel(UnitStack unitStack, int symbolSize, Game game) {
		super(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		graphService = ServiceFactory.getGraphService(game);
		graphService.setSymbolSize(symbolSize);
		this.symbolSize = symbolSize;
		this.unitStack = unitStack;
		c.ipady = 10;
		c.ipadx = 10;
		for (Unit unit : unitStack) {
			JLabel unitSymbol = new JLabel();
			unitSymbol.setIcon(new ImageIcon(graphService.getUnitSymbol(unit)));
			c.weightx = 0.20;
			c.gridx = 0;
			add(unitSymbol, c);
			Formation division = unit.getParentFormation(FormationLevel.DIVISION);
			JPanel formationInfoP = new JPanel(new GridLayout(0, 1));
			if (division != null) {
				formationInfoP.add(new JLabel("   " + division.getFullLongName()));
			}
			Formation corps = unit.getParentFormation(FormationLevel.CORPS);
			if (corps != null) {
				formationInfoP.add(new JLabel("   " + corps.getFullLongName()));
			}
			c.weightx = 0.30;
			c.gridx = 1;
			add(formationInfoP, c);
			JPanel labelsStateP = new JPanel(new GridLayout(0, 1));
			fillStateLabelsPanel(labelsStateP, unit);
			c.weightx = 0.30;
			c.gridx = 2;
			add(labelsStateP, c);
			JPanel textFieldsStateP = new JPanel(new GridLayout(0, 1));
			fillStateTextfieldsPanel(textFieldsStateP, unit);
			c.weightx = 0.20;
			c.gridx = 3;
			add(textFieldsStateP, c);
		}
	}

	protected void fillStateLabelsPanel(JPanel panel, Unit unit) {
		if (unit instanceof Company) {
			panel.add(new JLabel("Strength"));
			panel.add(new JLabel("Organization"));
			panel.add(new JLabel("Morale"));
		} else if (unit instanceof Formation) {
			panel.add(new JLabel("Supply Limit"));
			panel.add(new JLabel("Strength"));
		}
	}

	protected void fillStateTextfieldsPanel(JPanel panel, Unit unit) {
		if (unit instanceof Company) {
			Company company = (Company) unit;
			JTextField strengthTF = new JTextField();
			strengthTF.setText(NumberFormats.PERCENT.format(company.getStrength()));
			strengthTF.setEditable(false);
			panel.add(strengthTF);
			JTextField organizationTF = new JTextField();
			organizationTF.setText(NumberFormats.PERCENT.format(company.getOrganization()));
			organizationTF.setEditable(false);
			panel.add(organizationTF);
			JTextField moraleTF = new JTextField();
			moraleTF.setText(NumberFormats.MORALE.format(company.getMorale()));
			moraleTF.setEditable(false);
			panel.add(moraleTF);
		} else if (unit instanceof Formation) {
			Formation formation = (Formation) unit;
			JTextField supplyLimitTF = new JTextField();
			supplyLimitTF.setText(formation.getAllCompanies().size() + " / " + formation.getFormationLevel().getSupplyLimit());
			supplyLimitTF.setEditable(false);
			panel.add(supplyLimitTF);
			JTextField strengthTF = new JTextField();
			strengthTF.setText(formation.getTogetherCompanies().size() + " / " + formation.getAllCompanies().size());
			strengthTF.setEditable(false);
			panel.add(strengthTF);
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

package org.orion.ss.test.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;

import org.orion.ss.model.Unit;
import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Defense;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.service.CombatService;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.utils.NumberFormats;
import org.springframework.util.StringUtils;

public class SmallUnitInfoPanel extends FastPanel {

	private static final long serialVersionUID = -1792156856265716386L;

	private final CombatService combatService;

	public SmallUnitInfoPanel(Game game) {
		super();
		combatService = new CombatService(game);
	}

	public void update(Unit unit) {
		removeAll();
		if (unit != null) {
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), unit.toString()));
		} else {
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Unit Info"));
		}
		if (unit instanceof Company) {
			buildCompanyInfo((Company) unit);
		} else if (unit instanceof Formation) {
			buildFormationInfo((Formation) unit);
		}
	}

	protected void buildFormationInfo(Formation unit) {
		List<String> labels = new ArrayList<String>();
		labels.add("Type");
		labels.add("Supply Limit");
		labels.add("Strength");
		/*
		 * for (SupplyType supply : SupplyType.values()) { labels.add(supply.getDenomination()); }
		 */
		List<String> values = new ArrayList<String>();
		values.add(unit.getTroopType().getDenomination());
		values.add(unit.getAllCompanies().size() + " / " + unit.getFormationLevel().getSupplyLimit());
		values.add(unit.getAbsoluteStrength() + " / " + unit.getMaxStrength());
		for (int i = 0; i < labels.size(); i++) {
			addLabel(labels.get(i), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
					GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
			addNotEditableTextField(values.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN
					* 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
		}

	}

	protected void buildCompanyInfo(Company unit) {
		List<String> labels = new ArrayList<String>();
		labels.add("Type");
		labels.add("Mobility");
		labels.add("Speed");
		labels.add("Initiative");
		labels.add("Strength");
		labels.add("Organization");
		labels.add("Morale");
		labels.add("Experience");
		List<String> values = new ArrayList<String>();
		values.add(StringUtils.capitalize(unit.getTroopType().getDenomination()));
		values.add(unit.getModel().getMobility().getDenomination());
		values.add(NumberFormats.DF_2.format(unit.getModel().getSpeed()) + " km/h");
		values.add(NumberFormats.DF_2.format(unit.computeInitiative()));
		values.add((int) (unit.getStrength() * unit.getModel().getMaxStrength()) + " / " + unit.getModel().getMaxStrength());
		values.add(NumberFormats.PERCENT.format(unit.getOrganization()));
		values.add(NumberFormats.MORALE.format(unit.getMorale()));
		values.add(NumberFormats.XP.format(unit.getExperience()));
		for (Defense defense : combatService.computeDefenses(unit)) {
			labels.add(defense.getType().getDenomination());
			values.add("" + NumberFormats.DF_2.format(defense.getStrength()));
		}
		for (Attack attack : combatService.computeAttacks(unit)) {
			labels.add(attack.getType().getDenomination() + " attack ");
			values.add("" + NumberFormats.DF_2.format(attack.getStrength()));
		}
		for (int i = 0; i < values.size(); i++) {
			addLabel(labels.get(i), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
					GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
			addNotEditableTextField(values.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN
					* 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
		}
	}
}
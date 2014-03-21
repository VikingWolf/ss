package org.orion.ss.test.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Fortification;
import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Defense;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.service.CombatService;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.ServiceFactory;
import org.orion.ss.service.UnitService;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.utils.NumberFormats;
import org.springframework.util.StringUtils;

public class SmallUnitInfoPanel extends FastPanel {

	private static final long serialVersionUID = -1792156856265716386L;

	private final CombatService combatService;
	private final GeoService geoService;
	private final UnitService unitService;

	public SmallUnitInfoPanel(Game game) {
		super();
		combatService = ServiceFactory.getCombatService(game);
		geoService = ServiceFactory.getGeoService(game);
		unitService = ServiceFactory.getUnitService(game);
	}

	public void update(Activable unit) {
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
		} else if (unit instanceof Fortification)
			buildFortificationInfo((Fortification) unit);
	}

	protected void buildFortificationInfo(Fortification fortification) {
		List<String> labels = new ArrayList<String>();
		labels.add("Size");
		labels.add("Strength");
		labels.add("Status");
		List<String> values = new ArrayList<String>();
		values.add("" + fortification.getSize());
		values.add("" + fortification.getStrength());
		values.add(NumberFormats.PERCENT.format(fortification.getStatus()));
		int i = 0;
		for (i = 0; i < labels.size(); i++) {
			addLabel(labels.get(i),
					GraphicTest.LEFT_MARGIN,
					GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
					GraphicTest.COLUMN_WIDTH,
					GraphicTest.ROW_HEIGHT);
			addNotEditableTextField(values.get(i),
					GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH,
					GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
					GraphicTest.COLUMN_WIDTH_NARROW,
					GraphicTest.ROW_HEIGHT);
		}
		labels.add("Garrison");
		addLabel(labels.get(i),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
				GraphicTest.COLUMN_WIDTH,
				GraphicTest.ROW_HEIGHT);
		/* garrison */
		i++;
		for (Unit unit : unitService.getStackAt(geoService.getGame().getCurrentPlayerPosition(), fortification.getLocation())) {
			if (unit.isGarrison()) {
				addNotEditableTextField(unit.getFullShortName(),
						GraphicTest.LEFT_MARGIN,
						GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
						GraphicTest.COLUMN_WIDTH_XLARGE,
						GraphicTest.ROW_HEIGHT);
				i++;
			}
		}

	}

	protected void buildFormationInfo(Formation unit) {
		List<String> labels = new ArrayList<String>();
		labels.add("Type");
		labels.add("Supply Limit");
		labels.add("Strength");
		labels.add("Auto-Supply");
		List<String> values = new ArrayList<String>();
		values.add(unit.getTroopType().getDenomination());
		values.add(unit.getAllCompanies().size() + " / " + unit.getFormationLevel().getSupplyLimit());
		values.add(unit.getAbsoluteStrength() + " / " + unit.getMaxStrength());
		values.add("" + unit.isAutoSupply());
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
		labels.add("Auto-Supply");
		labels.add("Speed");
		labels.add("Initiative");
		labels.add("Strength");
		labels.add("Organization");
		labels.add("Morale");
		labels.add("Experience");
		List<String> values = new ArrayList<String>();
		values.add(StringUtils.capitalize(unit.getTroopType().getDenomination()));
		values.add("" + unit.isAutoSupply());
		values.add(unit.getModel().getMobility().getDenomination() + " " + NumberFormats.DF_2.format(unit.getModel().getSpeed()) + " km/h");
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
			labels.add(attack.getType().getDenomination());
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
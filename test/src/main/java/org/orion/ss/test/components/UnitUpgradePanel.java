package org.orion.ss.test.components;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Defense;
import org.orion.ss.model.impl.WeaponModel;
import org.orion.ss.service.ManagementService;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.utils.NumberFormats;

public class UnitUpgradePanel extends FastPanel {

	private static final long serialVersionUID = 7856777506136459114L;

	private final ManagementService managementService;

	public UnitUpgradePanel(ManagementService managementService) {
		super();
		this.managementService = managementService;
	}

	protected void displayUnitInfo(final Company company, FastPanel panel) {
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
			panel.addLabel(labels1.get(i), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
					GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
			panel.addNotEditableTextField(textfields1.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN
					* 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		}
		/* Second Column */
		panel.addLabel("Equipment Prestige Value", GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE,
				GraphicTest.TOP_MARGIN * 2, GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		panel.addNotEditableTextField(NumberFormats.PRESTIGE.format(managementService.getValue(company.getModel())),
				GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE * 2, GraphicTest.TOP_MARGIN * 2,
				GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
		List<String> labels2 = new ArrayList<String>();
		labels2.add("Equipment");
		for (WeaponModel weaponModel : company.getWeaponry().keySet()) {
			labels2.add(company.getWeaponry().get(weaponModel) + " x " + weaponModel.getDenomination());
		}
		for (int i = 0; i < labels2.size(); i++) {
			panel.addLabel(labels2.get(i), GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE,
					GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (i + 1), GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		}
		/* Attacks */
		panel.addLabel("Consumption", GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH + GraphicTest.COLUMN_WIDTH_LARGE,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (labels1.size() - 1), GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		for (int i = 0; i < company.computeAttacks().size(); i++) {
			Attack attack = company.computeAttacks().get(i);
			String supplyConsumption = "";
			for (SupplyType supplyType : attack.getConsumption().keySet()) {
				supplyConsumption += supplyType.getDenomination() + "=" + NumberFormats.DF_4.format(attack.getConsumption().get(supplyType)) + ", ";
			}
			if (supplyConsumption.length() > 0) {
				supplyConsumption = supplyConsumption.substring(0, supplyConsumption.length() - 2);
			}
			panel.addLabel(attack.getType().getDenomination(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT
					* (labels1.size() + i), GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
			panel.addNotEditableTextField(NumberFormats.DF_2.format(attack.getStrength()) + " at " + attack.getRange() + " km",
					GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (labels1.size() + i),
					GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
			panel.addNotEditableTextField(supplyConsumption, GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH
					+ GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * (labels1.size() + i),
					GraphicTest.COLUMN_WIDTH_XXLARGE, GraphicTest.ROW_HEIGHT);
		}
	}

}

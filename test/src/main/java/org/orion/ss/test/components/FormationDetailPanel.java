package org.orion.ss.test.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.service.ManagementService;
import org.orion.ss.service.ReinforceCost;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.utils.NumberFormats;

public class FormationDetailPanel extends FastPanel {

	private static final long serialVersionUID = 4809452906948077694L;

	private ManagementService managementService;
	
	public FormationDetailPanel(ManagementService managementService){
		super();
		this.managementService = managementService;
	}
	
	public void update(Formation formation){
		removeAll();
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), formation.getId());
		setBorder(title);

		List<String> labels1 = new ArrayList<String>();
		labels1.add("Name");
		labels1.add("Type");
		labels1.add("Strength");
		labels1.add("Reinforce");
		labels1.add("Elite Reinforce");
		for (int i = 0; i < labels1.size(); i++){
			addLabel(labels1.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH * i, GraphicTest.TOP_MARGIN, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		}		

		int j = 1;
		for (Company company : formation.getCompanies()){
			List<String> values1 = new ArrayList<String>();
			values1.add(company.getCode());
			values1.add(company.getModel().getLevel() + "(" + company.getModel().getLevel().getCode() + ")");
			values1.add("" + company.getAbsoluteStrength() + "/" + company.getModel().getMaxStrength());
			ReinforceCost regularCost = managementService.regularReinforceCost(company);
			values1.add(NumberFormats.PERCENT.format(regularCost.getStrength()) + " for " + NumberFormats.PRESTIGE.format(regularCost.getCost()));
			ReinforceCost eliteCost = managementService.eliteReinforceCost(company);
			values1.add(NumberFormats.PERCENT.format(eliteCost.getStrength()) + " for " + NumberFormats.PRESTIGE.format(eliteCost.getCost()));
			for (int i = 0; i < values1.size(); i++){
				addNotEditableTextField(values1.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH * i, GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * j, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);			
				
			}			
			j++;
		}
		for (Formation subordinate : formation.getSubordinates()){
			List<String> values1 = new ArrayList<String>();
			values1.add(subordinate.getName());
			values1.add("" + subordinate.getLevel() + "(" + formation.getLevel().getCode() + ")");
			values1.add(""  + subordinate.getAbsoluteStrength() + "/" + subordinate.getMaxStrength());
			for (int i = 0; i < values1.size(); i++){
				addNotEditableTextField(values1.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH * i, GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * j, GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);			
				
			}			
			j++;			
		}
		

	}
}

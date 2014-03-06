package org.orion.ss.model.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.FormationLevel;

public class Country {

	private String name;
	private double manpowerModifier;
	private Market market;
	private List<CompanyModel> companyModels;
	private final Map<FormationLevel, Integer> lastIds;
	private Color color;

	public Country(String name, double manpowerModifier, Color color) {
		this.name = name;
		this.manpowerModifier = manpowerModifier;
		this.color = color;
		market = new Market();
		companyModels = new ArrayList<CompanyModel>();
		lastIds = new HashMap<FormationLevel, Integer>();
	}

	public int getLastIdFor(FormationLevel level) {
		return lastIds.get(level);
	}

	public void updateLastId(Unit unit) {
		if (unit.getFormationLevel().isUniqueId()) {
			Integer actualId = lastIds.get(unit.getFormationLevel());
			if (actualId == null) {
				lastIds.put(unit.getFormationLevel(), unit.getId());
			} else if (actualId < unit.getId()) {
				lastIds.put(unit.getFormationLevel(), unit.getId());
			}
		}
	}

	/* adders */
	public void addCompanyModel(CompanyModel model) {
		if (!companyModels.contains(model)) {
			companyModels.add(model);
		}
	}

	/* getters & setters */

	public String getName() {
		return name;
	}

	public double getManpowerModifier() {
		return manpowerModifier;
	}

	public Market getMarket() {
		return market;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setManpowerModifier(double manpowerModifier) {
		this.manpowerModifier = manpowerModifier;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	public List<CompanyModel> getCompanyModels() {
		return companyModels;
	}

	public void setCompanyModels(List<CompanyModel> companyModels) {
		this.companyModels = companyModels;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}

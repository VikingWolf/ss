package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

public class Country {

	private String name;
	private double manpowerModifier;
	private Market market;
	private List<CompanyModel> companyModels;

	public Country(String name, double manpowerModifier) {
		this.name = name;
		this.manpowerModifier = manpowerModifier;
		market = new Market();
		companyModels = new ArrayList<CompanyModel>();
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

}

package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.ActivableImpl;
import org.orion.ss.model.core.Country;

public class Position extends ActivableImpl {

	private String code;
	private Country country;
	private List<Formation> battleOrder;
	private List<Fortification> defenses;

	public Position(){
		super();
		battleOrder = new ArrayList<Formation>();
		defenses = new ArrayList<Fortification>();
	}
	
	@Override
	public boolean isActivable() {
		return true;
	}
	
	public List<Company> getAllCompanies(){
		List<Company> result = new ArrayList<Company>();
		for (Formation formation : this.getBattleOrder()){
			result.addAll(formation.getAllCompanies());
		}
		return result;
	}

	/* adders */
	public void addFormation(Formation formation){
		battleOrder.add(formation);
	}

	/* getters & setters */

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<Formation> getBattleOrder() {
		return battleOrder;
	}

	public void setBattleOrder(List<Formation> battleOrder) {
		this.battleOrder = battleOrder;
	}

	public List<Fortification> getDefenses() {
		return defenses;
	}

	public void setDefenses(List<Fortification> defenses) {
		this.defenses = defenses;
	}

}

package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.core.FormationLevel;

public class Position extends Formation {

	public final static int ATTACKER = 1;
	public final static int DEFENDER = 2;
	
	private List<Fortification> defenses;
	private int prestige;

	public Position(FormationLevel type, String name){
		super(type, name);
		defenses = new ArrayList<Fortification>();
	}
	
	@Override
	public boolean isActivable() {
		return true;
	}
	

	/* getters & setters */


	public List<Fortification> getDefenses() {
		return defenses;
	}

	public void setDefenses(List<Fortification> defenses) {
		this.defenses = defenses;
	}

	public int getPrestige() {
		return prestige;
	}

	public void setPrestige(int prestige) {
		this.prestige = prestige;
	}
	
}

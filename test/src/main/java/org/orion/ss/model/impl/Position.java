package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.geo.Fortification;

public class Position extends Formation {

	public final static int ATTACKER = 1;
	public final static int DEFENDER = 2;
	
	private List<Fortification> defenses;
	private int prestige;

	public Position(FormationLevel level, TroopType type, int id){
		super(level, type, id);
		defenses = new ArrayList<Fortification>();
	}
	
	@Override
	public boolean isActivable() {
		return true;
	}
	
	public void decreasePrestige(int prestige){
		this.prestige -= prestige;
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

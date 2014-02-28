package org.orion.ss.model.impl;

import java.util.ArrayList;

public class AttackSet extends ArrayList<Attack> {

	private static final long serialVersionUID = 8846388830837927467L;

	@Override
	public boolean add(Attack attack) {
		if (this.contains(attack)) {
			int index = this.indexOf(attack);
			Attack adjusted = this.get(index);
			adjusted.increaseConsumption(attack.getConsumption());
			adjusted.increaseStrength(attack.getStrength());
			this.set(index, adjusted);
			return true;
		} else {
			return super.add(attack);
		}
	}

}

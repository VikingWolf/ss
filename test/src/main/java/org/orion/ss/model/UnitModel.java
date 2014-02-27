package org.orion.ss.model;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.Mobility;
import org.orion.ss.model.impl.Stock;

public abstract class UnitModel {

	private Stock consumption;
	private Stock maxSupplies;

	public abstract Mobility getMobility();
	
	public abstract FormationLevel getFormationLevel();

	/* getters & setters */

	public Stock getConsumption() {
		return consumption;
	}

	public void setConsumption(Stock consumption) {
		this.consumption = consumption;
	}

	public Stock getMaxSupplies() {
		return maxSupplies;
	}

	public void setMaxSupplies(Stock maxSupplies) {
		this.maxSupplies = maxSupplies;
	}
	
}

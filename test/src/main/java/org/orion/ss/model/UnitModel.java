package org.orion.ss.model;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.Mobility;
import org.orion.ss.model.impl.Stock;

public abstract class UnitModel implements DefenseCapable {

	private Stock consumption;
	private Stock maxSupplies;

	public abstract Mobility getMobility();

	public abstract FormationLevel getFormationLevel();

	public UnitModel() {
		super();
		this.consumption = new Stock();
		this.maxSupplies = new Stock();
	}

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

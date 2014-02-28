package org.orion.ss.model;

import org.orion.ss.model.core.Country;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.Mobility;
import org.orion.ss.model.impl.Stock;

public abstract class UnitModel implements DefenseCapable {

	private Stock consumption;
	private Stock maxSupplies;
	private Country country;

	public abstract Mobility getMobility();

	public abstract FormationLevel getFormationLevel();

	public UnitModel(Country country) {
		super();
		this.consumption = new Stock();
		this.maxSupplies = new Stock();
		this.country = country;
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

}

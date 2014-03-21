package org.orion.ss.model;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.impl.Country;
import org.orion.ss.model.impl.Mobility;
import org.orion.ss.model.impl.Stock;

public abstract class UnitModel implements Leveleable {

	private Stock consumption;
	private Stock maxSupplies;
	private Country country;

	public abstract Mobility getMobility();

	@Override
	public abstract FormationLevel getFormationLevel();

	public UnitModel(Country country) {
		super();
		consumption = new Stock();
		maxSupplies = new Stock();
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

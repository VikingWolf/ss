package org.orion.ss.model.impl;

import org.orion.ss.model.core.Country;
import org.orion.ss.model.core.SupplyType;

public class Supply {

	private Country country;
	private SupplyType type;
	
	public Supply(SupplyType type, Country country){
		super();
		this.country = country;
		this.type = type;		
	}

	/* getters & setters */
	
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public SupplyType getType() {
		return type;
	}

	public void setType(SupplyType type) {
		this.type = type;
	}

}

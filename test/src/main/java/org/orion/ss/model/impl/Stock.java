package org.orion.ss.model.impl;

import java.util.HashMap;

import org.orion.ss.model.core.SupplyType;

/* in metric tons */
public class Stock extends HashMap<SupplyType, Double> {
	
	private static final long serialVersionUID = -7291769965563781844L;

	public void put(SupplyType type, double amount){
		if (this.containsKey(type)){
			amount += this.get(type);
		} 
		super.put(type, amount);
	}

}

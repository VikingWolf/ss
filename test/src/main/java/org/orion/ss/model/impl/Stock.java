package org.orion.ss.model.impl;

import java.util.HashMap;

import org.orion.ss.model.core.SupplyType;

/* in metric tons */
public class Stock extends HashMap<SupplyType, Double> {

	private static final long serialVersionUID = -7291769965563781844L;

	public void add(Stock stock) {
		for (SupplyType supplyType : stock.keySet()) {
			this.put(supplyType, stock.get(supplyType));
		}
	}

	@Override
	public Double put(SupplyType type, Double amount) {
		if (this.containsKey(type)) {
			amount += this.get(type);
		}
		super.put(type, amount);
		return amount;
	}

}

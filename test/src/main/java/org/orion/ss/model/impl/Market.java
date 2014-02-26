package org.orion.ss.model.impl;

import java.util.HashMap;

public class Market extends HashMap<Supply, Integer> {

	private static final long serialVersionUID = -410240914606057259L;
	
	@Override
	public String toString(){
		String result ="";
		for (Supply supply : this.keySet()){
			result += supply.getType() + " for " + supply.getCountry() + "=" + this.get(supply) + "\n";
		}
		return result;
	}
	
}

package org.orion.ss.model.impl;

import java.util.HashMap;
import java.util.Map;

import org.orion.ss.model.core.Mobility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobilitySet extends HashMap<Mobility, Double> {

	protected final static Logger logger = LoggerFactory.getLogger(MobilitySet.class);

	private static final long serialVersionUID = -4903018601543307454L;

	/* always put the slowest speed movement */
	@Override
	public Double put(Mobility key, Double value) {
		double result = 0;
		if (this.containsKey(key)) {
			if (this.get(key) > value) {
				super.put(key, value);
				result = value;
			} else {
				result = this.get(key);
			}
		} else {
			super.put(key, value);
			result = value;
		}
		return result;
	}

	@Override
	public void putAll(Map<? extends Mobility, ? extends Double> map) {
		for (Mobility mobility : map.keySet()) {
			this.put(mobility, map.get(mobility));
		}
	}

}

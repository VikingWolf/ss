package org.orion.ss.model.impl;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobilitySet extends LinkedHashSet<Mobility> {

	protected final static Logger logger = LoggerFactory.getLogger(MobilitySet.class);

	private static final long serialVersionUID = -4903018601543307454L;

	@Override
	public boolean add(Mobility arg0) {
		boolean result = false;
		Mobility candidate = get(arg0);
		if (candidate != null) {
			if (candidate.getSpeed() > arg0.getSpeed()) {
				this.remove(candidate);
				result = super.add(arg0);
			}
		} else {
			result = super.add(arg0);
		}
		return result;
	}

	@Override
	public boolean addAll(Collection<? extends Mobility> arg0) {
		boolean result = false;
		for (Mobility mobility : arg0) {
			result |= this.add(mobility);
		}
		return result;
	}

	private Mobility get(Mobility arg0) {
		Mobility result = null;
		for (Mobility mobility : this) {
			if (mobility.equals(arg0)) {
				result = mobility;
				break;
			}
		}
		return result;
	}

}

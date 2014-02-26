package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.orion.ss.model.core.Mobility;

public class MobilitySet extends ArrayList<Mobility> {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(Mobility e) {
		boolean result = false;
		if (!this.contains(e)) {
			super.add(e);
			result = true;
		}
		return result;
	}

	@Override
	public boolean addAll(Collection<? extends Mobility> c) {
		boolean result = false;
		for (Mobility mobility : c) {
			result |= this.add(mobility);
		}
		return result;
	}

	@Override
	public void add(int index, Mobility element) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean addAll(int index, Collection<? extends Mobility> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Mobility set(int index, Mobility element) {
		// TODO Auto-generated method stub
		return null;
	}

}

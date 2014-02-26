package org.orion.ss.model;

public abstract class ActivableImpl implements Activable {

	private transient boolean hasBeenActivated = false;

	@Override
	public boolean isActivable() {
		return !hasBeenActivated;
	}

}

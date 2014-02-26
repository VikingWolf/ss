package org.orion.ss.model;

public abstract class ActivableImpl implements Activable {

	private transient boolean isActive = false;
	private transient boolean hasBeenActivated = false;

	@Override
	public boolean isActivable() {
		return !hasBeenActivated;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

}

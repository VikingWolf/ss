package org.orion.ss.model.impl;

import org.orion.ss.model.core.ObjectiveType;

public class Objective {

	private ObjectiveType type;
	private Location location;
	
	public Objective(ObjectiveType type, Location location){
		super();
		this.type = type;
		this.location = location;
	}

	/* getters & setters */

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ObjectiveType getType() {
		return type;
	}

	public void setType(ObjectiveType type) {
		this.type = type;
	}
}

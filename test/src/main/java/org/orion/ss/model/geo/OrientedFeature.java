package org.orion.ss.model.geo;

public abstract class OrientedFeature extends TerrainFeature {

	private OrientedLocation location;

	public OrientedFeature(OrientedLocation location) {
		super();
		this.location = location;
	}

	/* getters & setters */

	public OrientedLocation getLocation() {
		return location;
	}

	public void setLocaction(OrientedLocation location) {
		this.location = location;
	}

}
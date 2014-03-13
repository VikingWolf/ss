package org.orion.ss.model.geo;

public class Bridge extends OrientedFeature {

	private double state;

	public Bridge(OrientedLocation location) {
		super(location);
		state = 1.0d;
	}

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.BRIDGE;
	}

	/* getters & setters */

	public double getState() {
		return state;
	}

	public void setState(double state) {
		this.state = state;
	}

}

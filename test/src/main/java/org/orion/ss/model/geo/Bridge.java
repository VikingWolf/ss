package org.orion.ss.model.geo;


public class Bridge extends OrientedFeature {

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.BRIDGE;
	}

	private double state;

	/* getters & setters */
	
	public double getState() {
		return state;
	}

	public void setState(double state) {
		this.state = state;
	}

}

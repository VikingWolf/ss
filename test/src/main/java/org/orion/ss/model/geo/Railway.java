package org.orion.ss.model.geo;

public class Railway extends OrientedFeature {

	private int gauge;
	private double state;

	public Railway(OrientedLocation location, int gauge) {
		super(location);
		this.gauge = gauge;
		state = 1.0d;
	}

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.RAILWAY;
	}

	/* getters & setters */

	public int getGauge() {
		return gauge;
	}

	public void setGauge(int gauge) {
		this.gauge = gauge;
	}

	public double getState() {
		return state;
	}

	public void setState(double state) {
		this.state = state;
	}

}

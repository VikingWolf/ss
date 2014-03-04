package org.orion.ss.model.geo;

public class Railway extends OrientedFeature {

	private short gauge;
	private double state;
	
	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.RAILWAY;
	}

	/* getters & setters */

	public short getGauge() {
		return gauge;
	}

	public void setGauge(short gauge) {
		this.gauge = gauge;
	}

	public double getState() {
		return state;
	}

	public void setState(double state) {
		this.state = state;
	}
		
}

package org.orion.ss.model.geo;

import org.orion.ss.model.RadiusSupplier;

public class Railway extends MultiLocatedFeature implements RadiusSupplier {

	private int gauge;

	public Railway(int gauge) {
		super();
		this.gauge = gauge;
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

	@Override
	public int getSpupplyRadius() {
		return 1;
	}

}

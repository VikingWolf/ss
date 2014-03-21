package org.orion.ss.model.geo;

import org.orion.ss.model.SelfSupplier;

public class Railway extends MultiLocatedFeature implements SelfSupplier {

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

}

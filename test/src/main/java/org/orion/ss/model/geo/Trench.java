package org.orion.ss.model.geo;

public class Trench extends TerrainFeature {

	private double quality;

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.TRENCH;
	}

	/* getters & setters */

	public double getQuality() {
		return quality;
	}

	public void setQuality(double quality) {
		this.quality = quality;
	}

}

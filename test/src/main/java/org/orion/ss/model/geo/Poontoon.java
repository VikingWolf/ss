package org.orion.ss.model.geo;

public class Poontoon extends OrientedFeature {

	public Poontoon(OrientedLocation location) {
		super(location);
	}

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.PONTOON;
	}

}

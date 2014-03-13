package org.orion.ss.model.geo;

public class Road extends OrientedFeature {

	public Road(OrientedLocation location) {
		super(location);
	}

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.ROAD;
	}

}

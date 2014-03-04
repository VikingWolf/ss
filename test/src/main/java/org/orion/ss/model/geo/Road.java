package org.orion.ss.model.geo;

public class Road extends OrientedFeature {

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.ROAD;
	}

}

package org.orion.ss.model.geo;

public class Road extends MultiLocatedFeature {

	public Road() {
		super();
	}

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.ROAD;
	}

}

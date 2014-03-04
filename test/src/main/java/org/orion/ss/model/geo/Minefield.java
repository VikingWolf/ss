package org.orion.ss.model.geo;

public class Minefield extends TerrainFeature {

	private byte strength;

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.MINEFIELD;
	}

	/* getters & setters */
	
	public byte getStrength() {
		return strength;
	}

	public void setStrength(byte strength) {
		this.strength = strength;
	}

}

package org.orion.ss.model.geo;

import org.orion.ss.model.core.BridgeType;

public class Bridge extends MultiLocatedFeature {

	private double state;
	private final BridgeType bridgeType;

	public Bridge(BridgeType bridgeType) {
		super();
		state = 1.0d;
		this.bridgeType = bridgeType;
	}

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.BRIDGE;
	}

	/* getters & setters */

	public double getState() {
		return state;
	}

	public void setState(double state) {
		this.state = state;
	}

	public BridgeType getBridgeType() {
		return bridgeType;
	}

}

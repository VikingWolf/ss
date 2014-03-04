package org.orion.ss.model.geo;


import java.util.ArrayList;
import java.util.List;


public abstract class OrientedFeature extends TerrainFeature {

	private HexSidedPoint loc;
	
	public HexSidedPoint getLoc() {
		return loc;
	}

	public void setLoc(HexSidedPoint loc) {
		this.loc = loc;
	}
	
	public List<HexSidedPoint> getLocs(){
		List<HexSidedPoint> locs = new ArrayList<HexSidedPoint>();
		locs.add(loc);
		locs.add(loc.getComplementary());
		return locs;		
	}

}
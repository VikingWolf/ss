package org.orion.ss.model.geo;


public class SideLocation extends Location {

	private HexSide orientation;
	
	public SideLocation(int x, int y, HexSide orientation){
		super(x, y);
		this.orientation = orientation;
	}


	/* getters & setters */

	public HexSide getOrientation() {
		return orientation;
	}

	public void setOrientation(HexSide orientation) {
		this.orientation = orientation;
	}


}

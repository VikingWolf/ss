package org.orion.ss.model.impl;

import org.orion.ss.model.core.Orientation;

public class SideLocation extends Location {

	private Orientation orientation;
	
	public SideLocation(int x, int y, Orientation orientation){
		super(x, y);
		this.orientation = orientation;
	}


	/* getters & setters */

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}


}

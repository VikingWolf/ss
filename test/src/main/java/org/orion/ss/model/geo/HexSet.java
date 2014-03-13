package org.orion.ss.model.geo;

import java.util.ArrayList;

public class HexSet extends ArrayList<Hex> {

	private static final long serialVersionUID = -9177460073813774087L;

	public void add(HexSet hexset) {
		for (Hex hex : hexset) {
			if (!this.contains(hex)) {
				this.add(hex);
			}
		}
	}

}

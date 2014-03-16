package org.orion.ss.model.geo;

import java.util.ArrayList;
import java.util.List;

public class HexSet extends ArrayList<Hex> {

	private static final long serialVersionUID = -9177460073813774087L;

	public void add(HexSet hexset) {
		for (Hex hex : hexset) {
			if (!this.contains(hex)) {
				this.add(hex);
			}
		}
	}

	public List<Location> getLocations() {
		List<Location> result = new ArrayList<Location>();
		for (Hex hex : this) {
			result.add(hex.getCoords());
		}
		return result;
	}

}

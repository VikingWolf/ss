package org.orion.ss.model.geo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HexSet extends ArrayList<Hex> {

	private static final long serialVersionUID = -9177460073813774087L;

	@Override
	public boolean add(Hex hex) {
		boolean result = false;
		if (!this.contains(hex)) {
			super.add(hex);
			result = true;
		}
		return result;
	}

	@Override
	public boolean addAll(Collection<? extends Hex> hexSet) {
		boolean result = false;
		for (Hex hex : hexSet) {
			result = this.add(hex);
		}
		return result;
	}

	public List<Location> getLocations() {
		List<Location> result = new ArrayList<Location>();
		for (Hex hex : this) {
			result.add(hex.getCoords());
		}
		return result;
	}

	public static HexSet intersect(HexSet[] hexSets) {
		HexSet result = HexSet.merge(hexSets);
		if (hexSets.length > 1) {
			for (HexSet hexSet : hexSets) {
				result.addAll(hexSet);
			}
		}
		return result;
	}

	public static HexSet merge(HexSet[] hexSets) {
		HexSet result = new HexSet();
		for (HexSet hexSet : hexSets) {
			result.addAll(hexSet);
		}
		return result;
	}

}

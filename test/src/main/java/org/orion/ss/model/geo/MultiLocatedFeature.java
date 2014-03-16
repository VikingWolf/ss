package org.orion.ss.model.geo;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.MultiLocated;

public abstract class MultiLocatedFeature extends TerrainFeature implements MultiLocated {

	private List<OrientedLocation> locations;

	public MultiLocatedFeature() {
		super();
		locations = new ArrayList<OrientedLocation>();
	}

	public boolean passesBy(Rectangle bounds) {
		boolean result = false;
		for (OrientedLocation candidate : getLocations()) {
			if (candidate.getX() >= bounds.getX()
					&& candidate.getX() <= bounds.getX() + bounds.getWidth()
					&& candidate.getY() >= bounds.getY()
					&& candidate.getY() <= bounds.getY() + bounds.getHeight()) {
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean passesBy(Location location) {
		boolean result = false;
		for (OrientedLocation candidate : getLocations()) {
			if (candidate.getX() == location.getX() && candidate.getY() == location.getY()) {
				result = true;
				break;
			}
		}
		return result;
	}

	/* adders */
	public void addLocation(OrientedLocation location) {
		locations.add(location);
	}

	/* getters & setters */

	public void setLocations(List<OrientedLocation> locations) {
		this.locations = locations;
	}

	@Override
	public List<OrientedLocation> getLocations() {
		return locations;
	}

}

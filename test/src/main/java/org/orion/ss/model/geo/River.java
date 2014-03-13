package org.orion.ss.model.geo;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class River extends TerrainFeature {

	private int deep;
	private String name;
	List<OrientedLocation> locations;

	public River(String name, int deep) {
		super();
		this.name = name;
		this.deep = deep;
		locations = new ArrayList<OrientedLocation>();
	}

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.RIVER;
	}

	public List<OrientedLocation> getLocations() {
		List<OrientedLocation> result = new ArrayList<OrientedLocation>();
		for (OrientedLocation location : locations) {
			result.add(location);
			result.add(location.getComplementary());
		}
		return result;
	}

	public boolean flowsBy(Rectangle bounds) {
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

	public List<OrientedLocation> flowsBy(Location location) {
		List<OrientedLocation> result = new ArrayList<OrientedLocation>();
		for (OrientedLocation candidate : getLocations()) {
			if (candidate.getX() == location.getX() && candidate.getY() == location.getY()) {
				result.add(candidate);
			}
		}
		return result;
	}

	/* adders */
	public void addLocation(OrientedLocation location) {
		locations.add(location);
	}

	/* getters & setters */
	public int getDeep() {
		return deep;
	}

	public void setDeep(int deep) {
		this.deep = deep;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocations(List<OrientedLocation> locations) {
		this.locations = locations;
	}

}

package org.orion.ss.model.geo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class River extends MultiLocatedFeature {

	protected final static Logger logger = LoggerFactory.getLogger(River.class);

	private int deep;
	private String name;

	public River(String name, int deep) {
		super();
		this.name = name;
		this.deep = deep;
	}

	@Override
	public TerrainFeatureType getType() {
		return TerrainFeatureType.RIVER;
	}

	@Override
	public List<OrientedLocation> getLocations() {
		List<OrientedLocation> result = new ArrayList<OrientedLocation>();
		for (OrientedLocation location : super.getLocations()) {
			result.add(location);
			result.add(location.getComplementary());
		}
		return result;
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

}

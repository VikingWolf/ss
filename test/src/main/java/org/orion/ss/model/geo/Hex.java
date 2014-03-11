package org.orion.ss.model.geo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hex {

	protected Logger logger = LoggerFactory.getLogger(Hex.class);

	private Terrain terrain;
	private Vegetation vegetation;
	private Location coords;
	private SoilState soilState = SoilState.DRY;

	public Hex() {
		super();
	}

	public Hex(Terrain terrain, Vegetation vegetation) {
		super();
		this.terrain = terrain;
		this.vegetation = vegetation;
	}

	public Hex(Location coords) {
		super();
		this.setCoords(coords);
	}

	public Location getCoords() {
		return coords;
	}

	public void setCoords(Location coords) {
		this.coords = coords;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public Hex setTerrain(Terrain terrain) {
		this.terrain = terrain;
		return this;
	}

	public Vegetation getVegetation() {
		return vegetation;
	}

	public Hex setVegetation(Vegetation vegetation) {
		this.vegetation = vegetation;
		return this;
	}

	public SoilState getSoilState() {
		return soilState;
	}

	public void setSoilState(SoilState soilState) {
		this.soilState = soilState;
	}

	@Override
	public String toString() {
		return "Hex(" + coords.getX() + "," + coords.getY() + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coords == null) ? 0 : coords.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hex other = (Hex) obj;
		if (coords == null) {
			if (other.coords != null)
				return false;
		} else if (!coords.equals(other.coords))
			return false;
		return true;
	}

}

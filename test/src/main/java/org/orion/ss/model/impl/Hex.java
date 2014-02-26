package org.orion.ss.model.impl;

import org.orion.ss.model.core.SoilState;
import org.orion.ss.model.core.Terrain;
import org.orion.ss.model.core.Vegetation;

public class Hex {

	private Terrain terrain;
	private Vegetation vegetation;
	private short elevation;
	private SoilState soilState;
	
	public Hex(Terrain terrain, Vegetation vegetation){
		super();
		this.terrain = terrain;
		this.vegetation = vegetation;
		this.elevation = 0;
		this.soilState = SoilState.DRY;
	}
	
	/* getters & setters */
	
	public Terrain getTerrain() {
		return terrain;
	}
	
	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
	
	public Vegetation getVegetation() {
		return vegetation;
	}
	
	public void setVegetation(Vegetation vegetation) {
		this.vegetation = vegetation;
	}
	
	public short getElevation() {
		return elevation;
	}
	
	public void setElevation(short elevation) {
		this.elevation = elevation;
	}
	
	public SoilState getSoilState() {
		return soilState;
	}
	
	public void setSoilState(SoilState soilState) {
		this.soilState = soilState;
	}

}

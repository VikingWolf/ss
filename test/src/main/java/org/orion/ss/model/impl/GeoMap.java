package org.orion.ss.model.impl;

import java.util.Map;

import org.orion.ss.model.core.Terrain;
import org.orion.ss.model.core.Vegetation;

public class GeoMap {

	private Hex[][] body;
	private Map<SideLocation, River> rivers;

	public GeoMap(int rows, int columns, Terrain defaultTerrain, Vegetation defaultVegetation){
		super();
		body = new Hex[rows][columns];
		this.initMap(defaultTerrain, defaultVegetation);
	}
	
	private void initMap(Terrain defaultTerrain, Vegetation defaultVegetation){
		for (int i = 0; i < body.length; i++){
			for (int j = 0; j < body[i].length; j++){
				body[i][j] = new Hex(defaultTerrain, defaultVegetation);
			}
		}
		
	}
	
	/* getters & setters */
	
	public Hex[][] getBody() {
		return body;
	}
	
	public void setBody(Hex[][] body) {
		this.body = body;
	}
	
	public Map<SideLocation, River> getRivers() {
		return rivers;
	}
	
	public void setRivers(Map<SideLocation, River> rivers) {
		this.rivers = rivers;
	}
	
}

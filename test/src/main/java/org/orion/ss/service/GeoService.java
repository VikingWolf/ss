package org.orion.ss.service;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Game;

public class GeoService extends Service {

	public GeoService(Game game) {
		super(game);
	}
	
	public HexSet fullMap(){
		HexSet result = new HexSet();
		for (Hex hex : getGame().getMap()){
			result.add(hex);
		}
		return result;
	}
	
	public List<Point> getDeployArea(Unit unit){
		List<Point> result = new ArrayList<Point>();
		for (Point candidate : unit.getPosition().getDeployArea()){
			int stackSize = getStackSizeAt(candidate);
			if (getGame().getSettings().getStackLimit()>=stackSize + unit.stackSize()){
				result.add(candidate);
			}
		}
		return result;
		
	}
	
	protected int getStackSizeAt(Point point){
		int result = 0;
		for (Company company : getGame().getAllCompanies()){
			if (company.getLocation() != null){
				if (company.getLocation().equals(point)){
					result++;
				}
			}
		}
		return result;
	}

}

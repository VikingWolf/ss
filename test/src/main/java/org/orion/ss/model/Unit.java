package org.orion.ss.model;

import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Position;

public interface Unit extends Leveleable, Localizable {

	public int getId();
	
	public Unit getParent();
	
	public int stackSize();
	
	public Position getPosition();
	
	public Location getLocation();
	
	public void setLocation(Location location);
	
}

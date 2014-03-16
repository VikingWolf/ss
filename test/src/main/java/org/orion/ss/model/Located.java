package org.orion.ss.model;

import org.orion.ss.model.geo.Location;

public interface Located {

	public Location getLocation();

	public void setLocation(Location location);

}

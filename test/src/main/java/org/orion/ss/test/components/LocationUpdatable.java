package org.orion.ss.test.components;

import org.orion.ss.model.geo.Location;

public interface LocationUpdatable {

	public void updateLocation(Location location, int... modifiers);

	public void refreshLocation(int... modifiers);

	public void locationInfo(Location location, int x, int y);

}

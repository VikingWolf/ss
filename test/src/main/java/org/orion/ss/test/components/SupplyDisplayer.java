package org.orion.ss.test.components;

import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Stock;

public interface SupplyDisplayer {

	public void updateSuppliesDisplay(Stock stock, Location location);

}

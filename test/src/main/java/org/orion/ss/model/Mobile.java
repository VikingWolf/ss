package org.orion.ss.model;

import org.orion.ss.model.impl.MobilitySet;

public interface Mobile extends Activable, Located {

	public MobilitySet getMobilities();

}

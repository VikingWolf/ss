package org.orion.ss.model;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.impl.Country;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Position;

public interface Unit extends Leveleable, Mobile, Activable {

	public int getId();

	public Unit getParent();

	public int stackSize();

	public Position getPosition();

	public String getShortName();

	public String getFullShortName();

	public String getLongName();

	public String getFullLongName();

	public Country getCountry();

	public Formation getParentFormation(FormationLevel level);

}

package org.orion.ss.model;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Country;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Position;
import org.orion.ss.utils.FormationFormats;

public abstract class Unit extends ActivableImpl implements Leveleable, Mobile, Activable {

	private int id;

	private Location location;
	
	private Formation parent;

	/* abstract methods */
	public abstract int stackSize();
	
	public abstract Formation getParentFormation(FormationLevel level);
	
	public abstract Country getCountry();
	
	public abstract Position getPosition();
	
	public abstract TroopType getTroopType();

	public String getShortName(){
		return FormationFormats.shortFormat(this);
	}

	public String getFullShortName() {
		return FormationFormats.fullShortFormat(this);
	}

	public String getLongName() {
		return FormationFormats.longFormat(this);
	}
	
	public String getFullLongName(){
		return FormationFormats.fullLongFormat(this);
	}

	/* getters & setters */
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Formation getParent() {
		return parent;
	}

	public void setParent(Formation parent) {
		this.parent = parent;
	}

}

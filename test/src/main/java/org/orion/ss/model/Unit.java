package org.orion.ss.model;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Country;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Position;
import org.orion.ss.utils.FormationFormats;

public abstract class Unit extends ActivableImpl implements Leveleable, Mobile, Activable, SpotCapable {

	private int id;

	private Formation parent;

	private boolean detached = false;

	/* abstract methods */
	public abstract int stackSize();

	public abstract Country getCountry();

	public abstract Position getPosition();

	public abstract TroopType getTroopType();

	public abstract boolean isDetachable();

	@Override
	public abstract Location getLocation();

	@Override
	public abstract void setLocation(Location location);

	public String getShortName() {
		return FormationFormats.shortFormat(this);
	}

	public String getFullShortName() {
		return FormationFormats.fullShortFormat(this);
	}

	public String getLongName() {
		return FormationFormats.longFormat(this);
	}

	public String getFullLongName() {
		return FormationFormats.fullLongFormat(this);
	}

	public Formation getParentFormation(FormationLevel level) {
		Formation result = null;
		if (this.getFormationLevel().getOrdinal() < level.getOrdinal()) {
			if (this.getParent() != null) {
				if (this.getParent().getFormationLevel() == level)
					result = this.getParent();
				else result = this.getParent().getParentFormation(level);
			}
		}
		return result;
	}

	public boolean isAnyParentOf(Unit unit) {
		boolean result = false;
		if (unit.getParent() != null) {
			if (unit.getParent().equals(this)) {
				result = true;
			} else {
				result = isAnyParentOf(unit.getParent());
			}
		}
		return result;
	}

	/* getters & setters */

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

	public boolean isDetached() {
		return detached;
	}

	public void setDetached(boolean detached) {
		this.detached = detached;
	}

}

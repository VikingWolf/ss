package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.ActivableImpl;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.Unit;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.geo.Location;
import org.orion.ss.utils.FormationFormats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Formation extends ActivableImpl implements Mobile, Unit {

	protected final static Logger logger = LoggerFactory.getLogger(Formation.class);

	private final int id;
	private Country country;
	private List<Formation> subordinates;
	private List<Company> companies; /* company at 0 is hq company */
	private List<AirSquadron> airSquadrons;
	private List<Ship> ships;
	private FormationLevel level;
	private Formation parent;
	private TroopType type;

	public Formation(FormationLevel level, TroopType type, int id) {
		super();
		this.id = id;
		subordinates = new ArrayList<Formation>();
		companies = new ArrayList<Company>();
		airSquadrons = new ArrayList<AirSquadron>();
		ships = new ArrayList<Ship>();
		this.level = level;
		this.type = type;
	}

	@Override
	public MobilitySet getMobilities() {
		MobilitySet result = new MobilitySet();
		for (Unit unit : getUnitStackAtLocation(true)) {
			result.addAll(unit.getMobilities());
		}
		return result;
	}

	public List<Unit> getAllUnits() {
		List<Unit> result = new ArrayList<Unit>();
		result.addAll(this.getCompanies());
		for (Formation formation : this.getSubordinates()) {
			result.addAll(formation.getAllUnits());
		}
		return result;
	}

	protected UnitStack getUnitStackAtLocation(boolean onlyActivables) {
		return getUnitStackAtLocation(this.getLocation(), false);
	}

	protected UnitStack getUnitStackAtLocation(Location location, boolean onlyActivables) {
		UnitStack result = new UnitStack(location);
		for (Unit unit : getCompanies()) {
			if (unit.getLocation().equals(location)) {
				if (onlyActivables && unit.isActivable()) {
					result.add(unit);
				} else {
					result.add(unit);
				}
			}
		}
		for (Formation formation : getSubordinates()) {
			result.addAll(formation.getUnitStackAtLocation(location, onlyActivables));
		}
		return result;
	}

	public List<Company> getAllCompanies() {
		List<Company> result = new ArrayList<Company>();
		for (Unit unit : this.getAllUnits()) {
			if (unit instanceof Company) {
				result.add((Company) unit);
			}
		}
		return result;
	}

	public List<Company> getCompanyStackAtLocation(boolean onlyActivables) {
		List<Company> result = new ArrayList<Company>();
		for (Unit unit : getUnitStackAtLocation(onlyActivables)) {
			if (unit instanceof Company) {
				result.add((Company) unit);
			}
		}
		return result;
	}

	public int getAbsoluteStrength() {
		int strength = 0;
		for (Company company : this.getAllCompanies()) {
			strength += company.getAbsoluteStrength();
		}
		return strength;
	}

	public int getMaxStrength() {
		int strength = 0;
		for (Company company : this.getAllCompanies()) {
			strength += company.getModel().getMaxStrength();
		}
		return strength;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Position getPosition() {
		if (this instanceof Position) {
			return (Position) this;
		} else {
			return this.getParent().getPosition();
		}
	}

	public boolean isExpandable() {
		return this.getAllCompanies().size() <= this.getFormationLevel().getSupplyLimit();
	}

	@Override
	public int stackSize() {
		return this.getCompanyStackAtLocation(false).size();
	}

	/* adders */

	public void addCompany(Company company) {
		companies.add(company);
		company.setParent(this);
	}

	public void addSubordinate(Formation formation) {
		formation.setParent(this);
		formation.setCountry(this.getCountry());
		subordinates.add(formation);
	}

	public void addAirSquadron(AirSquadron airSquadron) {
		airSquadrons.add(airSquadron);
	}

	public void addShip(Ship ship) {
		ships.add(ship);
	}

	@Override
	public String getFullName() {
		return FormationFormats.fullFormat(this);
	}

	public String getName() {
		return FormationFormats.longFormat(this);
	}

	@Override
	public Formation getParentFormation(FormationLevel level) {
		if (this.getFormationLevel().getOrdinal() < level.getOrdinal()) {
			if (this.getParent() != null) {
				return this.getParent().getParentFormation(level);
			} else return null;
		} else if (this.getFormationLevel().getOrdinal() == level.getOrdinal()) {
			return this;
		} else return null;
	}

	/* getters & setters */

	public List<Formation> getSubordinates() {
		return subordinates;
	}

	public TroopType getType() {
		return type;
	}

	public void setType(TroopType type) {
		this.type = type;
	}

	public void setSubordinates(List<Formation> subordinates) {
		this.subordinates = subordinates;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	@Override
	public Location getLocation() {
		return companies.get(0).getLocation();
	}

	@Override
	public void setLocation(Location location) {
		companies.get(0).setLocation(location);
	}

	public List<AirSquadron> getAirSquadrons() {
		return airSquadrons;
	}

	public void setAirSquadrons(List<AirSquadron> airSquadrons) {
		this.airSquadrons = airSquadrons;
	}

	public List<Ship> getShip() {
		return ships;
	}

	public void setShip(List<Ship> ships) {
		this.ships = ships;
	}

	@Override
	public FormationLevel getFormationLevel() {
		return level;
	}

	public void setType(FormationLevel level) {
		this.level = level;
	}

	@Override
	public Formation getParent() {
		return parent;
	}

	public void setParent(Formation parent) {
		this.parent = parent;
	}

	@Override
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
		country.updateLastId(this);
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

}

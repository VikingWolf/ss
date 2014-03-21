package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.MovementSupplier;
import org.orion.ss.model.Unit;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.geo.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Formation extends Unit implements MovementSupplier {

	protected final static Logger logger = LoggerFactory.getLogger(Formation.class);

	private Country country;
	private List<Formation> subordinates;
	private List<Company> companies; /* company at 0 is hq company */
	private List<AirSquadron> airSquadrons;
	private List<Ship> ships;
	private FormationLevel level;
	private TroopType troopType;

	public Formation(FormationLevel level, TroopType type, int id) {
		super();
		setId(id);
		subordinates = new ArrayList<Formation>();
		companies = new ArrayList<Company>();
		airSquadrons = new ArrayList<AirSquadron>();
		ships = new ArrayList<Ship>();
		this.level = level;
		troopType = type;
	}

	@Override
	public MobilitySet getMobilities() {
		MobilitySet result = new MobilitySet();
		for (Company company : this.getCompanyStackAtLocation(false)) {
			result.addAll(company.getMobilities());
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
			if ((unit.getLocation()) != null && (unit.getLocation().equals(location))) {
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
		return getLongName();
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

	public Company getHQCompany() {
		return companies.get(0);
	}

	public boolean isAllTogether() {
		for (Company company : this.getCompanies()) {
			if (company.getLocation() == null) {
				return false;
			} else {
				if (!company.getLocation().equals(this.getHQCompany().getLocation())) { return false; }
			}
		}
		for (Formation subordinate : this.getSubordinates()) {
			if (!subordinate.isAllTogether()) { return false; }
		}
		return true;
	}

	public boolean canBeSplit() {
		return getCompanyStackAtLocation(false).size() > 1;
	}

	/* adders */

	@Override
	public boolean isDetachable() {
		return getParent() != null && !isDetached();
	}

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

	public List<Company> getTogetherCompanies() {
		List<Company> result = new ArrayList<Company>();
		for (Company company : this.getAllCompanies()) {
			if (company.getLocation() != null && company.getLocation().equals(this.getLocation())) {
				result.add(company);
			}
		}
		return result;
	}

	@Override
	public double getSpotCapacity() {
		double max = Double.NEGATIVE_INFINITY;
		for (Company company : getCompanyStackAtLocation(false)) {
			if (company.getSpotCapacity() > max) {
				max = company.getSpotCapacity();
			}
		}
		return max;
	}

	@Override
	public Mobility getSupplyMobility() {
		return this.getHQCompany().getModel().getMobility();
	}

	/* getters & setters */

	@Override
	public boolean isDetached() {
		return super.isDetached() || getParent() == null;
	}

	public List<Formation> getSubordinates() {
		return subordinates;
	}

	@Override
	public TroopType getTroopType() {
		return troopType;
	}

	public void setType(TroopType type) {
		troopType = type;
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
		for (Company company : companies) {
			if (company.getLocation() == null || !company.isDetached()) {
				company.setLocation(location);
			}
		}
		for (Formation subordinate : subordinates) {
			if (subordinate.getLocation() == null || !subordinate.isDetached()) {
				subordinate.setLocation(location);
			}
		}
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
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
		if (this.getFormationLevel().isUniqueId()) country.updateLastId(this);
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

}

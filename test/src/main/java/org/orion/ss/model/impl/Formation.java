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

public class Formation extends ActivableImpl implements Mobile, Unit {

	private int id;
	private Country country;
	private Location location; /* location of the hq */
	private List<Formation> subordinates;
	private List<Company> companies;
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
		for (Company company : getCompanyStackAtLocation(true)) {
			result.addAll(company.getMobilities());
		}
		return result;
	}

	public List<Company> getAllCompanies() {
		List<Company> result = new ArrayList<Company>();
		result.addAll(this.getCompanies());
		for (Formation formation : this.getSubordinates()) {
			result.addAll(formation.getAllCompanies());
		}
		return result;
	}

	protected CompanyStack getCompanyStackAtLocation(boolean onlyActivables) {
		return getCompanyStackAtLocation(this.getLocation(), false);
	}

	protected CompanyStack getCompanyStackAtLocation(Location location, boolean onlyActivables) {
		CompanyStack result = new CompanyStack(location);
		for (Company company : getCompanies()) {
			if (company.getLocation().equals(location)) {
				if (onlyActivables && company.isActivable()) {
					result.add(company);
				} else {
					result.add(company);
				}
			}
		}
		for (Formation formation : getSubordinates()) {
			result.addAll(formation.getCompanyStackAtLocation(location, onlyActivables));
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
		return this.id;
	}

	public Position getPosition() {
		if (this instanceof Position) {
			return (Position) this;
		} else {
			return this.getParent().getPosition();
		}
	}
	
	public boolean isExpandable(){
		return this.getAllCompanies().size() <= this.getFormationLevel().getSupplyLimit();
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
	
	public String getFullName(){
		return FormationFormats.fullFormat(this);
	}

	public String getName() {
		return FormationFormats.format(this);
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
		return location;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
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

	public FormationLevel getFormationLevel() {
		return level;
	}

	public void setType(FormationLevel level) {
		this.level = level;
	}

	public Formation getParent() {
		return parent;
	}

	public void setParent(Formation parent) {
		this.parent = parent;
	}

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

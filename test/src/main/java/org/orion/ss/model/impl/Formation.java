package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.ActivableImpl;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.core.Country;
import org.orion.ss.model.core.FormationLevel;

public class Formation extends ActivableImpl implements Mobile {

	private String name;
	private Country country;
	private Location location; /* location of the hq */
	private List<Formation> subordinates;
	private List<Company> companies;
	private List<AirSquadron> airSquadrons;
	private List<Ship> ships;
	private FormationLevel type;
	private Formation parent;

	public Formation(FormationLevel type, String name){
		super();
		this.name = name;
		subordinates = new ArrayList<Formation>();
		companies = new ArrayList<Company>();
		airSquadrons = new ArrayList<AirSquadron>();
		ships = new ArrayList<Ship>();
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

	public List<Company> getAllCompanies(){
		List<Company> result = new ArrayList<Company>();
		result.addAll(this.getCompanies());
		for (Formation formation : this.getSubordinates()){
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
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getId(){
		String result = "";
		if (parent != null){
			result += parent.getId() + ", ";
		}
		result += this.getName();
		return result;
	}
	
	/* adders */
	
	public void addCompany(Company company){
		this.companies.add(company);
		company.setParent(this);
	}
	
	public void addSubordinate(Formation formation){
		this.subordinates.add(formation);
		formation.setParent(this);
		formation.setCountry(this.getCountry());
	}
	
	public void addAirSquadron(AirSquadron airSquadron){
		this.airSquadrons.add(airSquadron);
	}
	
	public void addShip(Ship ship){
		this.ships.add(ship);
	}
	
	/* getters & setters */
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Formation> getSubordinates() {
		return subordinates;
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

	public FormationLevel getType() {
		return type;
	}

	public void setType(FormationLevel type) {
		this.type = type;
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
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

}

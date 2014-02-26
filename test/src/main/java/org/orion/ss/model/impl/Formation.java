package org.orion.ss.model.impl;

import java.util.List;

import org.orion.ss.model.ActivableImpl;
import org.orion.ss.model.Mobile;

public class Formation extends ActivableImpl implements Mobile {

	private String name;
	private Location location; /* location of the hq */
	private List<Formation> subordinates;
	private List<Company> companies;
	private List<AirSquadron> airSquadrons;
	private List<Ship> ships;

	@Override
	public MobilitySet getMobilities() {
		MobilitySet result = new MobilitySet();
		for (Company company : getCompanyStackAtLocation(true)) {
			result.addAll(company.getMobilities());
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

}

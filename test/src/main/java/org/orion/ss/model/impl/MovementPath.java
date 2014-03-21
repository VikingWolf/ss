package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.orion.ss.model.Mobile;
import org.orion.ss.model.geo.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovementPath extends Stack<LocationNode> {

	private static final long serialVersionUID = -6550613850708896833L;

	protected final static Logger logger = LoggerFactory.getLogger(LocationNode.class);

	private Mobile mobile;

	public MovementPath(Mobile mobile) {
		super();
		this.mobile = mobile;
		this.push(mobile.getLocation(), 0);
	}

	public LocationNode push(Location location, double cost) {
		return super.push(new LocationNode(location, cost));
	}

	public Mobile getMobile() {
		return mobile;
	}

	public void setMobile(Mobile mobile) {
		this.mobile = mobile;
	}

	public Location getLastLocation() {
		return this.peek().getLocation();
	}

	public double getTotalCost() {
		double cost = 0.0d;
		for (LocationNode node : this) {
			logger.error("location=" + node.getLocation() + ", cost=" + cost);
			cost += node.getCost();
		}
		return cost;
	}

	public List<Location> getLocations() {
		List<Location> result = new ArrayList<Location>();
		for (LocationNode node : this) {
			result.add(node.getLocation());
		}
		return result;
	}

}

class LocationNode {
	private Location location;
	private double cost;

	protected LocationNode(Location location, double cost) {
		super();
		this.location = location;
		this.cost = cost;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

}

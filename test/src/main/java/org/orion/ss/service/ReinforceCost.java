package org.orion.ss.service;

public class ReinforceCost {

	double strength;
	int cost;

	public ReinforceCost() {
		super();
		strength = 0;
		cost = 0;
	}

	public ReinforceCost(double strength, int cost) {
		super();
		this.strength = strength;
		this.cost = cost;
	}

	public void add(ReinforceCost cost) {
		this.cost += cost.getCost();
	}

	/* getters & setters */
	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

}

package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;

public class Weapon {

	private String denomination;
	private int maxROF; /* shots per minutes */
	private double maxRange;
	private double shellWeight;
		
	public Weapon(String denomination, int maxROF, double maxRange, double shellWeight){
		super();
		this.denomination = denomination;
		this.maxROF = maxROF;
		this.maxRange = maxRange;
		this.shellWeight = shellWeight;		
	}
	
	public List<Attack> computeAttacks(){
		List<Attack> result = new ArrayList<Attack>();
		return result;
	}

	/* getters & setters */
	
	public int getMaxROF() {
		return maxROF;
	}

	public void setMaxROF(int maxROF) {
		this.maxROF = maxROF;
	}

	public double getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(double maxRange) {
		this.maxRange = maxRange;
	}

	public double getShellWeight() {
		return shellWeight;
	}

	public void setShellWeight(double shellWeight) {
		this.shellWeight = shellWeight;
	}
	
}

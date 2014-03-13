package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameSettings {

	public final static long HOUR_MILLIS = 1000L * 60L * 60L;

	private int turnDuration; /* in hours */
	private Date initialTime;
	private int timeLimit; /* in turns */
	private int timeMargin; /* in turns */
	private double hexSide; /* in km */
	private double stackLimitModifier; /* modifier, companies */
	private List<Objective> objectives;

	public GameSettings() {
		super();
		objectives = new ArrayList<Objective>();
	}

	/* adders */
	public void addObjective(Objective objective) {
		objectives.add(objective);
	}

	/* getters & setters */

	public int getTurnDuration() {
		return turnDuration;
	}

	public void setTurnDuration(int turnDuration) {
		this.turnDuration = turnDuration;
	}

	public Date getInitialTime() {
		return initialTime;
	}

	public void setInitialTime(Date initialTime) {
		this.initialTime = initialTime;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getTimeMargin() {
		return timeMargin;
	}

	public void setTimeMargin(int timeMargin) {
		this.timeMargin = timeMargin;
	}

	public double getHexSide() {
		return hexSide;
	}

	public void setHexSide(double hexSide) {
		this.hexSide = hexSide;
	}

	public int getStackLimit() {
		return (int) (stackLimitModifier * hexSide + hexSide * 6);
	}

	public double getStackLimitModifier() {
		return stackLimitModifier;
	}

	public void setStackLimitModifier(double stackLimitModifier) {
		this.stackLimitModifier = stackLimitModifier;
	}

	public List<Objective> getObjectives() {
		return objectives;
	}

	public void setObjectives(List<Objective> objectives) {
		this.objectives = objectives;
	}

}

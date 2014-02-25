package org.orion.ss.model.impl;

import java.util.Date;
import java.util.List;

public class Scenario {

	private String name;
	private List<Position> attacker;
	private List<Position> defender;
	private byte turnDuration; /* in hours */
	private Date initialTime;
	private short timeLimit; /* in turns */
	private short timeMargin; /* in turns */
	private double hexSide; /* in km */
	private short stackLimit; /* in companies */

}

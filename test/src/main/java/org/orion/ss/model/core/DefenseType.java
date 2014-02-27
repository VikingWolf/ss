package org.orion.ss.model.core;

public enum DefenseType {

	/*			denomination			xpExponent	orgExponent */
	GROUND(		"Ground Defense",		0.3d,		0.6d), 
	CLOSE(		"Close Defense",		0.3d,		0.6d),
	AIR(		"Air Defense",			0.3d,		0.6d),
	NAVAL(		"Naval Defense",		0.3d,		0.6d);
	
	private String denomination;
	private double experienceExponent;
	private double organizationExponent;
	
	private DefenseType(String denomination, double experienceExponent, double organizationExponent){
		this.denomination = denomination;
		this.experienceExponent = experienceExponent;
		this.organizationExponent = organizationExponent;
	}

	public String getDenomination() {
		return denomination;
	}
	
	public double getExperienceExponent() {
		return experienceExponent;
	}

	public double getOrganizationExponent() {
		return organizationExponent;
	}


}

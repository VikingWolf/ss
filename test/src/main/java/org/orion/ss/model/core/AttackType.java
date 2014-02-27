package org.orion.ss.model.core;

public enum AttackType {

	/*			denomination			xpExponent	orgExponent*/
	SOFT(		"Soft Attack",			0.3d,		0.6d),
	HARD(		"Hard Attack",			0.3d,		0.6d),
	AIR(		"Air Attack",			0.3d,		0.6d),
	NAVAL(		"Naval Attack",			0.3d,		0.6d),
	BARRAGE(	"Barrage Attack",		0.3d,		0.6d),
	TORPEDOES(	"Torpedoes Attack",		0.3d,		0.6d);
	
	private String denomination;
	private double experienceExponent;
	private double organizationExponent;
	
	private AttackType(String denomination, double experienceExponent, double organizationExponent){
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

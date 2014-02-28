package org.orion.ss.model.core;

import java.util.Arrays;
import java.util.List;

import org.orion.ss.model.impl.Defense;

public enum CompanyType {

	/* denomination defenses */
	INFANTRY("infantry", TargetType.SOFT, new Defense[] { new Defense(DefenseType.GROUND, 1.0d), new Defense(DefenseType.CLOSE, 1.0d) }),
	CAVALRY("infantry", TargetType.SOFT, new Defense[] { new Defense(DefenseType.GROUND, 1.0d), new Defense(DefenseType.CLOSE, 1.0d) }),
	MOTORIZED("infantry", TargetType.SOFT, new Defense[] { new Defense(DefenseType.GROUND, 1.0d), new Defense(DefenseType.CLOSE, 1.0d) }),
	ARMOURED("infantry", TargetType.HARD, new Defense[] { new Defense(DefenseType.GROUND, 1.0d), new Defense(DefenseType.CLOSE, 1.0d) });

	private String denomination;
	private List<Defense> defenses;
	private TargetType targetType;

	private CompanyType(String denomination, TargetType targetType, Defense[] defenses) {
		this.denomination = denomination;
		this.targetType = targetType;
		this.defenses = Arrays.asList(defenses);
	}

	public List<Defense> getDefenses() {
		return defenses;
	}

	public String getDenomination() {
		return denomination;
	}

	public TargetType getTargetType() {
		return targetType;
	}

	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
	}

}

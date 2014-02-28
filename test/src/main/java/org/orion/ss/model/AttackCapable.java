package org.orion.ss.model;

import java.util.List;

import org.orion.ss.model.impl.Attack;

public interface AttackCapable {

	public List<Attack> computeAttacks();

}

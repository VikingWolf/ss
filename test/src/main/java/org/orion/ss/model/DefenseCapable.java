package org.orion.ss.model;

import java.util.List;

import org.orion.ss.model.impl.Defense;

public interface DefenseCapable {

	public List<Defense> computeDefenses();

}

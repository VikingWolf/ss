package org.orion.ss.orders;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.OrderTime;
import org.orion.ss.model.impl.Game;

public class Ungarrison extends Order<Unit> {

	public final static Class<Unit> EXECUTOR_CLASS = Unit.class;

	public Ungarrison(Unit executor, Game game) {
		super(executor, game);
	}

	public static String getDescription() {
		return "Un-garrison a unit will make it leave its current stationing at the fortifications. ";
	}

	@Override
	public String getDenomination() {
		return "Leave Garrison";
	}

	@Override
	public OrderTime getOrderTime() {
		return OrderTime.NONE;
	}

	@Override
	public boolean checkRequirements() {
		return getExecutor().isGarrison();
	}

	@Override
	public String execute() {
		getExecutor().setGarrison(false);
		return getExecutor().getFullLongName() + " leaves now garrison at " + getExecutor().getLocation() + " fortifications.";
	}

}

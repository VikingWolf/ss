package org.orion.ss.orders;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.OrderTime;
import org.orion.ss.model.impl.Game;

public class Attach extends Order<Unit> {

	public final static Class<Unit> EXECUTOR_CLASS = Unit.class;

	public Attach(Unit executor, Game game) {
		super(executor, game);
	}

	public static String getDescription() {
		return "Attaching the unit will return the command to his parent unit, and no longer will receive separate orders.";
	}

	@Override
	public String getDenomination() {
		return "Attach";
	}

	@Override
	public OrderTime getOrderTime() {
		return OrderTime.NONE;
	}

	@Override
	public boolean checkRequirements() {
		return getExecutor().isDetached() && getExecutor().getParent() != null && getExecutor().getParent().getLocation().equals(getExecutor().getLocation());
	}

	@Override
	public String execute() {
		getExecutor().setDetached(false);
		return getExecutor().getFullLongName() + " has been attached to " + getExecutor().getParent().getFullLongName();
	}

}

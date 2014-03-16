package org.orion.ss.orders;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.OrderTime;

public class Attach extends Order {

	private Unit toAttach;

	public Attach(Unit toAttach) {
		super();
		this.toAttach = toAttach;
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

	public Unit getToAttach() {
		return toAttach;
	}

	public void setToAttach(Unit toAttach) {
		this.toAttach = toAttach;
	}

}

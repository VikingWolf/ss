package org.orion.ss.orders;

import org.orion.ss.model.core.OrderTime;

public class Move extends Order {

	@Override
	public String getDenomination() {
		return "Move";
	}

	@Override
	public OrderTime getOrderTime() {
		return OrderTime.VARIABLE;
	}

}

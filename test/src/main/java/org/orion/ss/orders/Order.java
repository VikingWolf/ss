package org.orion.ss.orders;

import org.orion.ss.model.core.OrderTime;

public abstract class Order {

	public abstract String getDenomination();

	public abstract OrderTime getOrderTime();

	@Override
	public String toString() {
		return getDenomination();
	}

}

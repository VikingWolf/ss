package org.orion.ss.orders;

import org.orion.ss.model.Mobile;
import org.orion.ss.model.core.OrderTime;
import org.orion.ss.model.impl.Game;

public class Move extends Order<Mobile> {

	public final static Class<Mobile> EXECUTOR_CLASS = Mobile.class;

	public Move(Mobile executor, Game game) {
		super(executor, game);
	}

	@Override
	public String getDenomination() {
		return "Move";
	}

	@Override
	public OrderTime getOrderTime() {
		return OrderTime.VARIABLE;
	}

	@Override
	public boolean checkRequirements() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return null;
	}

}

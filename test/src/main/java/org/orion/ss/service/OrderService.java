package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.Unit;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.orders.Move;
import org.orion.ss.orders.Order;
import org.orion.ss.orders.Split;

public class OrderService extends Service {

	protected OrderService(Game game) {
		super(game);
	}

	public List<Order> buildOrders(Activable target) {
		List<Order> result = new ArrayList<Order>();
		if (target instanceof Formation) {
			Formation formation = (Formation) target;
			if (formation.canBeSplit()) {
				result.add(new Split());
			}
		}
		if (target instanceof Mobile) {
			result.add(new Move());
		}
		return result;
	}

	public String execute(Order order) {
		if (order instanceof Split)
			return execute((Split) order);
		if (order instanceof Move)
			return execute((Move) order);
		else return "";
	}

	protected String execute(Split order) {
		String result = "";
		for (Unit unit : order.getToDetach()) {
			unit.setDetached(true);
			result += unit.getFullLongName() + ", ";
		}
		if (result.length() > 2)
			result = result.substring(0, result.length() - 2);
		result += " have been detached. ";
		getGame().getLog().addEntry(result);
		return result;
		//TODO implement
	}

	protected String execute(Move order) {
		String result = "";
		return result;
		//TODO implement		
	}

}

package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.Unit;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.orders.Attach;
import org.orion.ss.orders.Detach;
import org.orion.ss.orders.Move;
import org.orion.ss.orders.Order;

public class OrderService extends Service {

	protected OrderService(Game game) {
		super(game);
	}

	public List<Order> buildOrders(Activable target) {
		List<Order> result = new ArrayList<Order>();
		if (target instanceof Formation) {
			Formation formation = (Formation) target;
			/* detach */
			if (formation.canBeSplit()) {
				result.add(new Detach());
			}
		}
		if (target instanceof Unit) {
			/* attach */
			Unit unit = (Unit) target;
			if (unit.isDetached() && unit.getParent() != null && unit.getParent().getLocation().equals(unit.getLocation())) {
				result.add(new Attach(unit));
			}
		}
		if (target instanceof Mobile) {
			result.add(new Move());
		}
		return result;
	}

	public String execute(Order order) {
		String result = "";
		if (order instanceof Detach)
			result = execute((Detach) order);
		if (order instanceof Attach)
			result = execute((Attach) order);
		if (order instanceof Move)
			result = execute((Move) order);
		if (result.length() > 0) {
			getGame().getLog().addEntry(result);
		}
		return result;
	}

	protected String execute(Detach order) {
		String result = "";
		for (Unit unit : order.getToDetach()) {
			unit.setDetached(true);
			result += unit.getFullLongName() + ", ";
		}
		if (result.length() > 2)
			result = result.substring(0, result.length() - 2);
		result += " have been detached. ";
		return result;
	}

	protected String execute(Attach order) {
		String result = "";
		order.getToAttach().setDetached(false);
		result = order.getToAttach().getFullLongName() + " has been attached to " + order.getToAttach().getParent().getFullLongName();
		return result;
	}

	protected String execute(Move order) {
		String result = "";
		return result;
		//TODO implement		
	}

}

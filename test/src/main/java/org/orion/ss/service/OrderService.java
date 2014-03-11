package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Player;
import org.orion.ss.orders.Move;
import org.orion.ss.orders.Order;

public class OrderService extends Service {

	protected OrderService(Game game) {
		super(game);
	}

	protected List<Activable> loadActivables(Player player) {
		// TODO
		return null;
	}

	protected List<Order> buildOrders(Activable target) {
		List<Order> result = new ArrayList<Order>();
		if (target instanceof Mobile) {
			result.add(new Move());
		}
		return result;
	}

}

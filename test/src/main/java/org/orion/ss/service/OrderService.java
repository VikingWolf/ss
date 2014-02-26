package org.orion.ss.service;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.impl.Game;
import org.orion.ss.orders.Move;
import org.orion.ss.orders.Order;

public class OrderService {

	private Game game;

	public OrderService(Game game) {
		this.game = game;
	}

	public List<Order> buildOrders(Activable target) {
		List<Order> result = new ArrayList<Order>();
		if (target instanceof Mobile) {
			result.add(new Move());
		}
		return result;
	}

	/* getters & setters */

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}

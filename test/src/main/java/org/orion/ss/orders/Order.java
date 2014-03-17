package org.orion.ss.orders;

import org.orion.ss.model.Activable;
import org.orion.ss.model.core.OrderTime;
import org.orion.ss.model.impl.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Order<E extends Activable> {

	protected final static Logger logger = LoggerFactory.getLogger(Order.class);

	private E executor;

	private Game game;

	public abstract String getDenomination();

	public abstract OrderTime getOrderTime();

	public abstract boolean checkRequirements();

	public abstract String execute();

	public Order(E executor, Game game) {
		super();
		this.executor = executor;
		this.game = game;
	}

	public static String getDescription() {
		return "Generic Order";
	}

	@Override
	public String toString() {
		return getDenomination() + ", executor=" + this.getExecutor();
	}

	/* getters & setters */

	public E getExecutor() {
		return executor;
	}

	public void setExecutor(E executor) {
		this.executor = executor;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
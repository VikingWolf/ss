package org.orion.ss.test.order;

import java.awt.Rectangle;

import org.orion.ss.model.Activable;
import org.orion.ss.orders.Move;

public class MoveOrderPanel extends OrderPanel<Activable, Move> {

	private static final long serialVersionUID = 2302431681035739809L;

	public MoveOrderPanel(Move order, Rectangle bounds, Activable target, OrderExecutor dialog) {
		super(order, bounds, target, dialog);
	}

	@Override
	protected void mount() {
		//TODO mount;
	}

	/* getters & setters */

}

package org.orion.ss.test.order;

import java.awt.Rectangle;

import org.orion.ss.model.Activable;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.orders.Attach;
import org.orion.ss.orders.Detach;
import org.orion.ss.orders.Move;
import org.orion.ss.orders.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderPanelFactory {

	protected final static Logger logger = LoggerFactory.getLogger(OrderPanelFactory.class);

	private Rectangle bounds;

	private final Activable target;

	private OrderExecutor dialog;

	public OrderPanelFactory(Activable target, OrderExecutor dialog) {
		super();
		this.target = target;
		this.dialog = dialog;
	}

	public void setPanelsBounds(int x, int y, int w, int h) {
		bounds = new Rectangle(x, y, w, h);
	}

	public OrderPanel<?, ?> getOrderPanel(Order order) {
		OrderPanel<?, ?> result = null;
		if (order instanceof Detach) result = new DetachOrderPanel((Detach) order, bounds, (Formation) target, getDialog());
		if (order instanceof Attach) result = new AttachOrderPanel((Attach) order, bounds, (Formation) target, getDialog());
		if (order instanceof Move) result = new MoveOrderPanel((Move) order, bounds, target, getDialog());
		return result;
	}

	/* getters & setters */

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Activable getTarget() {
		return target;
	}

	public OrderExecutor getDialog() {
		return dialog;
	}

	public void setDialog(OrderExecutor dialog) {
		this.dialog = dialog;
	}

}

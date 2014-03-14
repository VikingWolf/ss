package org.orion.ss.test.order;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.BorderFactory;

import org.orion.ss.model.Activable;
import org.orion.ss.orders.Order;
import org.orion.ss.test.components.FastPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OrderPanel<T extends Activable, O extends Order> extends FastPanel {

	private static final long serialVersionUID = 2302431681035739809L;

	protected final static Logger logger = LoggerFactory.getLogger(OrderPanel.class);

	private final T target;

	private final O order;

	private OrderExecutor dialog;

	public OrderPanel(O order, Rectangle bounds, T target, OrderExecutor dialog) {
		super();
		this.target = target;
		this.order = order;
		this.dialog = dialog;
		setLayout(null);
		setBounds(bounds);
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Order Info: " + order.getDenomination()));
		mount();
	}

	public T getTarget() {
		return target;
	}

	protected abstract void mount();

	/* getters & setters */

	public O getOrder() {
		return order;
	}

	public OrderExecutor getDialog() {
		return dialog;
	}

	public void setDialog(OrderExecutor dialog) {
		this.dialog = dialog;
	}

}

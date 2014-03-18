package org.orion.ss.test.order;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.BorderFactory;

import org.orion.ss.model.Activable;
import org.orion.ss.orders.Garrison;
import org.orion.ss.orders.Order;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.test.components.FastPanel;
import org.orion.ss.test.dialogs.UnitOrdersDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderPanel<T extends Activable, O extends Order<?>> extends FastPanel {

	private static final long serialVersionUID = 2302431681035739809L;

	protected final static Logger logger = LoggerFactory.getLogger(OrderPanel.class);

	private final T target;

	private final O order;

	private UnitOrdersDialog dialog;

	public OrderPanel(O order, Rectangle bounds, T target, UnitOrdersDialog dialog) {
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

	protected void mount() {
		int y = 0;
		this.addLabel(Garrison.getDescription(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
				this.getWidth() - GraphicTest.LEFT_MARGIN - GraphicTest.RIGHT_MARGIN - GraphicTest.LATERAL_SWING_MARGIN,
				GraphicTest.ROW_HEIGHT);
		y++;
		getDialog().updateExecution(true);
	}

	/* getters & setters */

	public O getOrder() {
		return order;
	}

	public UnitOrdersDialog getDialog() {
		return dialog;
	}

	public void setDialog(UnitOrdersDialog dialog) {
		this.dialog = dialog;
	}

}

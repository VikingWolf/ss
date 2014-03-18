package org.orion.ss.test.order;

import java.awt.Rectangle;

import org.orion.ss.model.Activable;
import org.orion.ss.model.Mobile;
import org.orion.ss.model.Unit;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.orders.Attach;
import org.orion.ss.orders.AutoSupply;
import org.orion.ss.orders.Detach;
import org.orion.ss.orders.Garrison;
import org.orion.ss.orders.Move;
import org.orion.ss.orders.Order;
import org.orion.ss.orders.Ungarrison;
import org.orion.ss.test.dialogs.UnitOrdersDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderPanelFactory {

	protected final static Logger logger = LoggerFactory.getLogger(OrderPanelFactory.class);

	private Rectangle bounds;

	private final Activable target;

	private UnitOrdersDialog dialog;

	public OrderPanelFactory(Activable target, UnitOrdersDialog dialog) {
		super();
		this.target = target;
		this.dialog = dialog;
	}

	public void setPanelsBounds(int x, int y, int w, int h) {
		bounds = new Rectangle(x, y, w, h);
	}

	public OrderPanel<?, ?> getOrderPanel(Order<?> order) {
		OrderPanel<?, ?> result = null;
		if (order instanceof Detach)
			result = new DetachOrderPanel((Detach) order, bounds, (Formation) target, getDialog());
		else if (order instanceof Attach)
			result = new OrderPanel<Unit, Attach>((Attach) order, bounds, (Unit) target, getDialog());
		else if (order instanceof Move)
			result = new MoveOrderPanel((Move) order, bounds, (Mobile) target, getDialog());
		else if (order instanceof AutoSupply)
			result = new AutoSupplyOrderPanel((AutoSupply) order, bounds, (Unit) target, getDialog());
		else if (order instanceof Garrison)
			result = new OrderPanel<Unit, Garrison>((Garrison) order, bounds, (Unit) target, getDialog());
		else if (order instanceof Ungarrison)
			result = new OrderPanel<Unit, Ungarrison>((Ungarrison) order, bounds, (Unit) target, getDialog());
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

	public UnitOrdersDialog getDialog() {
		return dialog;
	}

	public void setDialog(UnitOrdersDialog dialog) {
		this.dialog = dialog;
	}

}

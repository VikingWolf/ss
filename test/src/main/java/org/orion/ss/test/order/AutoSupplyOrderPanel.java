package org.orion.ss.test.order;

import java.awt.Rectangle;

import org.orion.ss.model.Unit;
import org.orion.ss.orders.AutoSupply;
import org.orion.ss.orders.Detach;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.test.dialogs.UnitOrdersDialog;

public class AutoSupplyOrderPanel extends OrderPanel<Unit, AutoSupply> {

	private static final long serialVersionUID = 2302431681035739809L;

	public AutoSupplyOrderPanel(AutoSupply order, Rectangle bounds, Unit target, UnitOrdersDialog dialog) {
		super(order, bounds, target, dialog);
	}

	@Override
	protected void mount() {
		int y = 0;
		this.addLabel(Detach.getDescription(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
				this.getWidth() - GraphicTest.LEFT_MARGIN - GraphicTest.RIGHT_MARGIN - GraphicTest.LATERAL_SWING_MARGIN,
				GraphicTest.ROW_HEIGHT);
		y++;
		this.addLabel("Auto-Supply will be set to " + !this.getTarget().isAutoSupply(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
				this.getWidth() - GraphicTest.LEFT_MARGIN - GraphicTest.RIGHT_MARGIN - GraphicTest.LATERAL_SWING_MARGIN,
				GraphicTest.ROW_HEIGHT);
		y++;
		getDialog().updateExecution(true);
	}
}

package org.orion.ss.test.order;

import java.awt.Rectangle;

import org.orion.ss.model.Unit;
import org.orion.ss.orders.Garrison;
import org.orion.ss.test.GraphicTest;

public class GarrisonOrderPanel extends OrderPanel<Unit, Garrison> {

	private static final long serialVersionUID = 2302431681035739809L;

	public GarrisonOrderPanel(Garrison order, Rectangle bounds, Unit target, OrderExecutor dialog) {
		super(order, bounds, target, dialog);
	}

	@Override
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
}

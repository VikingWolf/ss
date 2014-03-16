package org.orion.ss.test.order;

import java.awt.Rectangle;

import org.orion.ss.model.impl.Formation;
import org.orion.ss.orders.Attach;
import org.orion.ss.orders.Detach;
import org.orion.ss.test.GraphicTest;

public class AttachOrderPanel extends OrderPanel<Formation, Attach> {

	private static final long serialVersionUID = 2302431681035739809L;

	public AttachOrderPanel(Attach order, Rectangle bounds, Formation target, OrderExecutor dialog) {
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
		getDialog().updateExecution(true);
	}
}

package org.orion.ss.test.order;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.orion.ss.model.Mobile;
import org.orion.ss.model.geo.Location;
import org.orion.ss.orders.Move;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.test.dialogs.UnitOrdersDialog;

public class MoveOrderPanel extends OrderPanel<Mobile, Move> {

	private static final long serialVersionUID = 2302431681035739809L;

	private JButton tracePathB;

	private int lastRow = 1;

	public MoveOrderPanel(Move order, Rectangle bounds, Mobile target, UnitOrdersDialog dialog) {
		super(order, bounds, target, dialog);
		getDialog().setModal(false);
	}

	@Override
	protected void mount() {
		int y = 0;
		this.addLabel(Move.getDescription(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
				this.getWidth() - GraphicTest.LEFT_MARGIN - GraphicTest.RIGHT_MARGIN - GraphicTest.LATERAL_SWING_MARGIN,
				GraphicTest.ROW_HEIGHT);
		y++;
		tracePathB = new JButton("Trace Movement Path");
		tracePathB.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 3 + GraphicTest.ROW_HEIGHT * y,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		add(tracePathB);
		tracePathB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				getDialog().getTurnPanel().startMoveMode(getTarget());
				getDialog().setVisible(false);
			}

		});
		y++;
	}

	public void addLocation(Location location) {
		getOrder().addToMovementPath(location);
		addLabel(location.toString(),
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN + tracePathB.getY() + tracePathB.getHeight() + lastRow * GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH_XLARGE,
				GraphicTest.ROW_HEIGHT);
		lastRow++;
	}
}

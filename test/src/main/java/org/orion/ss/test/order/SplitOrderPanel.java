package org.orion.ss.test.order;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.OrderTime;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.orders.Split;
import org.orion.ss.test.GraphicTest;

public class SplitOrderPanel extends OrderPanel<Formation, Split> {

	private static final long serialVersionUID = 2302431681035739809L;

	private Map<Unit, JCheckBox> checkBoxes;

	public SplitOrderPanel(Split order, Rectangle bounds, Formation target, OrderExecutor dialog) {
		super(order, bounds, target, dialog);
	}

	@Override
	protected void mount() {

		int y = 0;
		if (getOrder().getOrderTime() == OrderTime.AUTOMATIC) {
			this.addLabel("This order will be executed automatically, spending no time",
					GraphicTest.LEFT_MARGIN,
					GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
					GraphicTest.COLUMN_WIDTH_XXXXLARGE,
					GraphicTest.ROW_HEIGHT);
			y++;
		}
		this.addLabel("Select the units to detach",
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
				GraphicTest.COLUMN_WIDTH_XXXXLARGE,
				GraphicTest.ROW_HEIGHT);
		y++;
		CheckBoxListener listener = new CheckBoxListener();
		checkBoxes = new HashMap<Unit, JCheckBox>();
		for (Formation subordinate : getTarget().getSubordinates()) {
			if (subordinate.isDetachable()) {
				JCheckBox checkBox = new JCheckBox();
				checkBox.setBounds(
						GraphicTest.LEFT_MARGIN,
						GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
						GraphicTest.COLUMN_WIDTH_XXNARROW,
						GraphicTest.ROW_HEIGHT);
				checkBox.addActionListener(listener);
				add(checkBox);
				checkBoxes.put(subordinate, checkBox);
				this.addLabel(subordinate.getLongName(),
						GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_XXNARROW,
						GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
						GraphicTest.COLUMN_WIDTH_XXLARGE,
						GraphicTest.ROW_HEIGHT);
				y++;
			}
		}
		for (Company company : getTarget().getCompanies()) {
			if (company.isDetachable()) {
				JCheckBox checkBox = new JCheckBox();
				checkBox.setBounds(
						GraphicTest.LEFT_MARGIN,
						GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
						GraphicTest.COLUMN_WIDTH_XXNARROW,
						GraphicTest.ROW_HEIGHT);
				checkBox.addActionListener(listener);
				add(checkBox);
				checkBoxes.put(company, checkBox);
				this.addLabel(company.getFullLongName(),
						GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_XXNARROW,
						GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * y,
						GraphicTest.COLUMN_WIDTH_XXLARGE,
						GraphicTest.ROW_HEIGHT);
				y++;
			}
		}
		//TODO mount;
	}

	class CheckBoxListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			boolean canExecute = false;
			for (Unit unit : checkBoxes.keySet()) {
				if (checkBoxes.get(unit).isSelected()) {
					canExecute = true;
					getOrder().addToDetach(unit);
				} else {
					getOrder().removeToDetach(unit);
				}
			}
			getDialog().updateExecution(canExecute);
		}

	}

}

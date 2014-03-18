package org.orion.ss.test.order;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;

import org.orion.ss.model.Unit;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.orders.Detach;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.test.dialogs.UnitOrdersDialog;

public class DetachOrderPanel extends OrderPanel<Formation, Detach> {

	private static final long serialVersionUID = 2302431681035739809L;

	private Map<Unit, JCheckBox> checkBoxes;

	public DetachOrderPanel(Detach order, Rectangle bounds, Formation target, UnitOrdersDialog dialog) {
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

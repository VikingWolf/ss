package org.orion.ss.test.components;

import javax.swing.event.TreeSelectionEvent;

import org.orion.ss.model.impl.Position;

public class ManagementTreePanel extends PositionTreePanel {

	public ManagementTreePanel(PlayerPanel parent, Position position, int x, int y, int w, int h) {
		super(parent, position, x, y, w, h);
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
	}

}

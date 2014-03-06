package org.orion.ss.test.components;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Position;

public class ManagementTreePanel extends PositionTreePanel {

	public ManagementTreePanel(ManagementPanel parent, Position position, int x, int y, int w, int h) {
		super(parent, position, x, y, w, h);
	}

	@Override
	public ManagementPanel getParent(){
		return (ManagementPanel) super.getParent();
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getTree().getLastSelectedPathComponent();
		if (node == null) return;
		Object nodeInfo = node.getUserObject();
		if (nodeInfo instanceof Company) {
			Company company = (Company) nodeInfo;
			getParent().updateDetails(company);
		}
		if (nodeInfo instanceof Formation) {
			Formation formation = (Formation) nodeInfo;
			getParent().updateDetails(formation);
		}
	}

}

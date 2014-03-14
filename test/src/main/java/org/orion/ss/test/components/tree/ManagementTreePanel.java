package org.orion.ss.test.components.tree;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Position;
import org.orion.ss.test.components.UnitDetailsDisplayer;

public class ManagementTreePanel extends FormationTreePanel {

	private final UnitDetailsDisplayer parent;

	public ManagementTreePanel(UnitDetailsDisplayer parent, Position position, int x, int y, int w, int h) {
		super(position, x, y, w, h);
		this.parent = parent;
	}

	@Override
	protected JTree createTree() {
		return new JTree(getTop());
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getTree().getLastSelectedPathComponent();
		if (node == null) return;
		Object nodeInfo = node.getUserObject();
		if (nodeInfo instanceof Company) {
			Company company = (Company) nodeInfo;
			parent.updateUnitDetails(company);
		}
		if (nodeInfo instanceof Formation) {
			Formation formation = (Formation) nodeInfo;
			parent.updateUnitDetails(formation);
		}
	}

}

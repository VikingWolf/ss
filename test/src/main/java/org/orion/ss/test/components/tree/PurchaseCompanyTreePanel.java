package org.orion.ss.test.components.tree;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import org.orion.ss.model.Unit;
import org.orion.ss.model.impl.Position;
import org.orion.ss.test.components.UnitDetailsDisplayer;

public class PurchaseCompanyTreePanel extends PositionTreePanel {

	private final UnitDetailsDisplayer parent;

	public PurchaseCompanyTreePanel(UnitDetailsDisplayer parent, Position position, int x, int y, int w, int h) {
		super(position, x, y, w, h, PositionTreePanel.MODE_EXCLUDE_COMPANIES);
		this.parent = parent;
	}

	@Override
	protected JTree createTree() {
		return new DeploymentTree(getTop());
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		JTree tree = (JTree) e.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		Unit unit = (Unit) node.getUserObject();
		parent.updateUnitDetails(unit);
	}

}

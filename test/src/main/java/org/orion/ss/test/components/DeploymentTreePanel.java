package org.orion.ss.test.components;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import org.orion.ss.model.Unit;
import org.orion.ss.model.impl.Position;

public class DeploymentTreePanel extends PositionTreePanel {

	public DeploymentTreePanel(DeploymentPanel parent, Position position, int x, int y, int w, int h) {
		super(parent, position, x, y, w, h);
	}

	@Override
	protected JTree createTree() {
		return new DeploymentTree(getTop());
	}

	@Override
	public DeploymentPanel getParent() {
		return (DeploymentPanel) super.getParent();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		JTree tree = (JTree) e.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		Unit unit = (Unit) node.getUserObject();
		getParent().updateMap(unit);
	}

}

package org.orion.ss.test.components;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.orion.ss.model.Unit;

public class DeploymentTree extends JTree {

	private static final long serialVersionUID = 792464345012159206L;

	public DeploymentTree(TreeNode root) {
		super(root);
	}

	@Override
	public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		String result = "";
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		Unit unit = (Unit) node.getUserObject();
		result += unit.toString();
		if (unit.getLocation() != null) result += "(deployed)";
		return result;
	}

}

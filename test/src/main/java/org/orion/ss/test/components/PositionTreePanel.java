package org.orion.ss.test.components;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.orion.ss.model.impl.AirSquadron;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Position;

public class PositionTreePanel implements TreeSelectionListener {

	private final Position position;
	private final JTree tree;

	private final PlayerPanel parent;
	private final JScrollPane panel;

	public PositionTreePanel(PlayerPanel parent, Position position, int x, int y, int w, int h) {
		super();
		this.position = position;
		this.parent = parent;
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(position.getName());
		tree = new JTree(top);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		createNodes(top);
		tree.addTreeSelectionListener(this);
		panel = new JScrollPane(tree);
		panel.setBounds(x, y, w, h);
	}

	private void createNodes(DefaultMutableTreeNode top) {
		for (Formation formation : position.getSubordinates()) {
			createNodes(top, formation);
		}
	}

	private void createNodes(DefaultMutableTreeNode parent, Formation formation) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(formation);
		for (Company company : formation.getCompanies()) {
			node.add(new DefaultMutableTreeNode(company));
		}
		parent.add(node);
	}

	public JScrollPane getPanel() {
		return this.panel;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node == null) return;

		Object nodeInfo = node.getUserObject();
		if (node.isLeaf()) {
			if (nodeInfo instanceof Company) {
				Company company = (Company) nodeInfo;
				parent.updateUnitPanel(company);
			} else if (nodeInfo instanceof AirSquadron) {
				AirSquadron airSquadron = (AirSquadron) nodeInfo;
				parent.updateUnitPanel(airSquadron);
			}
		}
	}

}

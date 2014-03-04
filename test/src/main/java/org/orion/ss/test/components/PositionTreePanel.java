package org.orion.ss.test.components;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionTreePanel implements TreeSelectionListener {

	protected final static Logger logger = LoggerFactory.getLogger(PositionTreePanel.class);

	private final Position position;
	private final JTree tree;

	private final ManagementPanel parent;
	private final JScrollPane panel;

	private final DefaultMutableTreeNode top;

	public PositionTreePanel(ManagementPanel parent, Position position, int x, int y, int w, int h) {
		super();
		this.position = position;
		this.parent = parent;
		top = new DefaultMutableTreeNode(position);
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
		return panel;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node == null) return;
		Object nodeInfo = node.getUserObject();
		if (nodeInfo instanceof Company) {
			Company company = (Company) nodeInfo;
			parent.updateDetails(company);
		}
		if (nodeInfo instanceof Formation) {
			Formation formation = (Formation) nodeInfo;
			parent.updateDetails(formation);
		}
	}

}

package org.orion.ss.test.components.tree;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PositionTreePanel implements TreeSelectionListener {

	protected final static Logger logger = LoggerFactory.getLogger(PositionTreePanel.class);

	public final static int MODE_INCLUDE_ALL = 1;
	public final static int MODE_EXCLUDE_COMPANIES = 2;

	private final Position position;
	private final JTree tree;

	private final JScrollPane panel;

	private final DefaultMutableTreeNode top;

	private int mode = MODE_INCLUDE_ALL;

	public PositionTreePanel(Position position, int x, int y, int w, int h) {
		this(position, x, y, w, h, MODE_INCLUDE_ALL);
	}

	public PositionTreePanel(Position position, int x, int y, int w, int h, int mode) {
		super();
		this.mode = mode;
		this.position = position;
		top = new DefaultMutableTreeNode(position);
		tree = createTree();
		createNodes(top);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		panel = new JScrollPane(tree);
		panel.setBounds(x, y, w, h);
	}

	protected abstract JTree createTree();

	protected void createNodes(DefaultMutableTreeNode top) {
		if (mode != MODE_EXCLUDE_COMPANIES) {
			for (Company company : position.getCompanies()) {
				top.add(new DefaultMutableTreeNode(company));
			}
		}
		for (Formation formation : position.getSubordinates()) {
			createNodes(top, formation);
		}
	}

	protected void createNodes(DefaultMutableTreeNode parent, Formation formation) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(formation);
		if (mode != MODE_EXCLUDE_COMPANIES) {
			for (Company company : formation.getCompanies()) {
				node.add(new DefaultMutableTreeNode(company));
			}
		}
		for (Formation subordinate : formation.getSubordinates()) {
			createNodes(node, subordinate);
		}
		parent.add(node);
	}

	/* getters & setters */

	public JScrollPane getPanel() {
		return panel;
	}

	public Position getPosition() {
		return position;
	}

	public JTree getTree() {
		return tree;
	}

	public DefaultMutableTreeNode getTop() {
		return top;
	}

}

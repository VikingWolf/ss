package org.orion.ss.test.components.tree;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FormationTreePanel implements TreeSelectionListener {

	protected final static Logger logger = LoggerFactory.getLogger(FormationTreePanel.class);

	public final static int MODE_INCLUDE_ALL = 1;
	public final static int MODE_EXCLUDE_COMPANIES = 2;
	public final static int MODE_EXCLUDE_NOT_DETACHABLE = 3;

	private final Formation formation;
	private final JTree tree;

	private final JScrollPane panel;

	private final DefaultMutableTreeNode top;

	private int mode = MODE_INCLUDE_ALL;

	public FormationTreePanel(Formation formation, int x, int y, int w, int h) {
		this(formation, x, y, w, h, MODE_INCLUDE_ALL);
	}

	public FormationTreePanel(Formation formation, int x, int y, int w, int h, int mode) {
		super();
		this.mode = mode;
		this.formation = formation;
		top = new DefaultMutableTreeNode(formation);
		createNodes(top);
		tree = createTree();
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		panel = new JScrollPane(tree);
		panel.setBounds(x, y, w, h);
	}

	protected abstract JTree createTree();

	protected void createNodes(DefaultMutableTreeNode top) {
		if (mode != MODE_EXCLUDE_COMPANIES) {
			for (Company company : formation.getCompanies()) {
				if (mode == MODE_EXCLUDE_NOT_DETACHABLE) {
					if (company.isDetachable()) {
						top.add(new DefaultMutableTreeNode(company));
					}
				} else {
					top.add(new DefaultMutableTreeNode(company));
				}
			}
		}
		for (Formation subordinate : formation.getSubordinates()) {
			createNodes(top, subordinate);
		}
	}

	protected void createNodes(DefaultMutableTreeNode parent, Formation formation) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(formation);
		if (mode != MODE_EXCLUDE_COMPANIES) {
			for (Company company : formation.getCompanies()) {
				if (mode == MODE_EXCLUDE_NOT_DETACHABLE) {
					if (company.isDetachable()) {
						node.add(new DefaultMutableTreeNode(company));
					}
				} else {
					node.add(new DefaultMutableTreeNode(company));
				}
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

	public Formation getFormation() {
		return formation;
	}

	public JTree getTree() {
		return tree;
	}

	public DefaultMutableTreeNode getTop() {
		return top;
	}

}

package org.orion.ss.test.components;

import java.awt.Color;

import javax.swing.JLabel;

import org.orion.ss.model.impl.Game;
import org.orion.ss.test.GraphicTest;

public class DeploymentPanel extends PlayerPanel {

	private static final long serialVersionUID = 8107876468158803580L;

	public DeploymentPanel(GraphicTest parent, Game game) {
		super(parent, game);
		setLayout(null);
		JLabel label = new JLabel("CACA");
		label.setBounds(100, 100, 200, 200);
		label.setBackground(Color.RED);
		add(label);
		mount();

	}

	public void mount() {
		removeAll();
	}

}

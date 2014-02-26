package org.orion.ss.test.components;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import org.orion.ss.model.impl.Player;
import org.orion.ss.test.GraphicTest;

public class CurrentPlayerLabel extends JLabel implements Observer {

	private static final long serialVersionUID = -8942090764796550645L;

	public CurrentPlayerLabel() {
		super();
		setBounds(GraphicTest.LEFT_MARGIN, 330, 300, 200);
		this.setForeground(Color.RED);
		setText("Current Player : ");
	}

	@Override
	public void update(Observable observed, Object source) {
		Player player = (Player) source;
		this.setText("Current Player : " + player.getEmail());
		repaint();
	}

}

package org.orion.ss.test.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.orion.ss.model.impl.Game;
import org.orion.ss.test.GraphicTest;

public class PlayerPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -6731886729299310601L;

	private final Game game;

	private final GraphicTest parent;

	/* GUI components */
	private JButton button;

	public PlayerPanel(GraphicTest parent, Game game) {
		super();
		this.game = game;
		this.parent = parent;
		setBounds(GraphicTest.TAB_BOUNDS);
		setLayout(null);
	}

	protected void mount() {
		this.removeAll();
		addLabel("Administration, " + game.getCurrentPlayer().getEmail(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
		button = new JButton("End Turn");
		button.setBounds(GraphicTest.LEFT_MARGIN, 340, GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.nextPlayer();
			}

		});

	}

	protected void addLabel(String text, int x, int y, int w, int h) {
		JLabel label = new JLabel(text);
		label.setBounds(x, y, w, h);
		add(label);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		mount();
	}

	public void dismiss() {
		button.setEnabled(false);
	}

}

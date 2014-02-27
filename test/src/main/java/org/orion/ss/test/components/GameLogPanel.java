package org.orion.ss.test.components;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.orion.ss.test.GraphicTest;

public class GameLogPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 6487852662932859447L;

	private final JTextArea textArea;

	public GameLogPanel() {
		super();
		setLayout(null);
		setBounds(GraphicTest.WINDOW_BOUNDS);
		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN, this.getWidth() - GraphicTest.LATERAL_SWING_MARGIN
				- GraphicTest.LEFT_MARGIN - GraphicTest.RIGHT_MARGIN, GraphicTest.LOG_TEXT_AREA_HEIGHT);
		add(scrollPane);
	}

	public void update(String text) {
		textArea.setText(text);
		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		this.update((String)arg);
	}

}

package org.orion.ss.test.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FastPanel extends JPanel {

	private static final long serialVersionUID = -741604751506467854L;

	public void addLabel(String text, int x, int y, int w, int h) {
		JLabel label = new JLabel(text);
		label.setBounds(x, y, w, h);
		add(label);
	}

	public void addNotEditableTextField(String text, int x, int y, int w, int h) {
		JTextField textField = new JTextField();
		textField.setBounds(x, y, w, h);
		textField.setText(text);
		textField.setEditable(false);
		add(textField);
	}
}

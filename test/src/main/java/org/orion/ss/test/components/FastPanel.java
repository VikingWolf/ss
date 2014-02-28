package org.orion.ss.test.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class FastPanel extends JPanel {

	private static final long serialVersionUID = -741604751506467854L;

	protected void addLabel(String text, int x, int y, int w, int h) {
		JLabel label = new JLabel(text);
		label.setBounds(x, y, w, h);
		add(label);
	}

	protected void addNotEditableTextField(String text, int x, int y, int w, int h) {
		JTextField textField = new JTextField();
		textField.setBounds(x, y, w, h);
		textField.setText(text);
		textField.setEditable(false);
		add(textField);
	}
}

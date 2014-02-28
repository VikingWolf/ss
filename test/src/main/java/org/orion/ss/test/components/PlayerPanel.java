package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.impl.AirSquadron;
import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Defense;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.WeaponModel;
import org.orion.ss.service.ManagementService;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.utils.NumberFormats;

public class PlayerPanel extends FastPanel implements Observer {

	private static final long serialVersionUID = -6731886729299310601L;

	private final Game game;

	private final GraphicTest parent;

	private final ManagementService managementService;

	/* GUI components */
	private JButton button;
	private FastPanel unitDetailPanel;

	public PlayerPanel(GraphicTest parent, Game game) {
		super();
		this.game = game;
		this.parent = parent;
		setBounds(GraphicTest.TAB_BOUNDS);
		setLayout(null);
		managementService = new ManagementService(game);
	}

	protected void mount() {
		this.removeAll();
		addLabel("Administration, " + game.getCurrentPlayer().getEmail(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
		button = new JButton("End Turn");
		button.setBounds(GraphicTest.LEFT_MARGIN, 440, GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
		add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean gameEnded = parent.nextPlayer();
				if (gameEnded) {
					button.setEnabled(false);
					parent.endGame();
				}
			}

		});
		PositionTreePanel treePanel = new PositionTreePanel(this, game.getCurrentPlayerPosition(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN
				* 2 + GraphicTest.ROW_HEIGHT, GraphicTest.COLUMN_WIDTH_XLARGE, 380);
		add(treePanel.getPanel());
		unitDetailPanel = new FastPanel();
		unitDetailPanel.setLayout(null);
		unitDetailPanel.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.TOP_MARGIN, GraphicTest.COLUMN_WIDTH * 2
				+ GraphicTest.COLUMN_WIDTH_LARGE * 2 + GraphicTest.LEFT_MARGIN * 3, 460);
		add(unitDetailPanel);

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		mount();
	}

	public void updateUnitPanel(Company company) {
		unitDetailPanel.removeAll();
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), company.getId());
		unitDetailPanel.setBorder(title);
		/* First column */
		List<String> labels1 = new ArrayList<String>(Arrays.asList(new String[] { "Type", "Model", "Mobility", "Speed", "Initiative", "Strength",
				"Organization", "Experience" }));
		List<String> textfields1 = new ArrayList<String>(
				Arrays.asList(new String[] { "" + company.getModel().getType(), company.getModel().getCode(), "" + company.getModel().getMobility(),
						"" + company.getModel().getSpeed() + "km/h", "" + company.computeInitiative(),
						company.getStrength() + "(" + company.getModel().getMaxStrength() + ")", "" + company.getOrganization(),
						"" + company.getExperience() }));
		for (Attack attack : company.computeAttacks()) {
			labels1.add(attack.getType().getDenomination());
			textfields1.add(NumberFormats.DF_2.format(attack.getStrength()) + " at " + attack.getRange() + " km");
		}
		for (Defense defense : company.computeDefenses()) {
			labels1.add(defense.getType().getDenomination());
			textfields1.add("" + NumberFormats.DF_2.format(defense.getStrength()));
		}
		for (SupplyType stock : company.getMaxSupplies().keySet()) {
			labels1.add("Max " + stock.getDenomination());
			textfields1.add("" + company.getMaxSupplies().get(stock));
		}
		for (int i = 0; i < labels1.size(); i++) {
			unitDetailPanel.addLabel(labels1.get(i), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
					GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
			unitDetailPanel.addNotEditableTextField(textfields1.get(i), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH, GraphicTest.TOP_MARGIN
					* 2
					+ GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_LARGE, GraphicTest.ROW_HEIGHT);
		}
		/* Second Column */
		List<String> labels2 = new ArrayList<String>();
		List<String> textfields2 = new ArrayList<String>();
		int i = 0;
		for (WeaponModel weaponModel : company.getModel().getWeaponry().keySet()) {
			unitDetailPanel.addLabel(labels2.get(i), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i,
					GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);

			i++;
		}

		// TODO montando panel
	}

	public void updateUnitPanel(AirSquadron airSquadron) {
		// TODO montar panel
	}

}

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

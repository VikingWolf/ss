package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.orion.ss.model.impl.AirSquadron;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Game;
import org.orion.ss.service.ManagementService;
import org.orion.ss.test.GraphicTest;

public class PlayerPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -6731886729299310601L;

	private final Game game;

	private final GraphicTest parent;

	private ManagementService managementService;
	
	/* GUI components */
	private JButton button;
	private JPanel unitDetailPanel;

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
		button.setBounds(GraphicTest.LEFT_MARGIN, 340, GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
		add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean gameEnded = parent.nextPlayer();
				if (gameEnded){
					button.setEnabled(false);
					parent.endGame();
				}
			}

		});
		PositionTreePanel treePanel = new PositionTreePanel(this, game.getCurrentPlayerPosition(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT, GraphicTest.COLUMN_WIDTH_XLARGE, 280);
		add(treePanel.getPanel());
		unitDetailPanel = new JPanel();
		unitDetailPanel.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT, GraphicTest.COLUMN_WIDTH * 2, 280);
		add(unitDetailPanel);

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
	
	public void updateUnitPanel(Company company){
		unitDetailPanel.removeAll();
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), company.getId());
		unitDetailPanel.setBorder(title);
		//TODO montar panel
	}
	
	public void updateUnitPanel(AirSquadron airSquadron){
		//TODO montar panel
	}

}

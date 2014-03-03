package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.service.CombatService;
import org.orion.ss.service.ManagementService;
import org.orion.ss.test.GraphicTest;
import org.orion.ss.utils.NumberFormats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerPanel extends FastPanel implements Observer {

	protected final static Logger logger = LoggerFactory.getLogger(PlayerPanel.class);

	private static final long serialVersionUID = -6731886729299310601L;

	private final Game game;

	private final GraphicTest parent;

	private final ManagementService managementService;
	private final CombatService combatService;

	/* GUI components */
	private JPanel detailsPanel;
	private JTextField prestigeTF;
	private JButton button;
	private CompanyDetailPanel unitDetailPanel;
	private CompanyUpgradePanel unitUpgradeInfoPanel;	
	private FastPanel reinforceInfoPanel;
	private FormationDetailPanel formationDetailPanel;


	public PlayerPanel(GraphicTest parent, Game game) {
		super();
		this.game = game;
		this.parent = parent;
		setBounds(GraphicTest.TAB_BOUNDS);
		setLayout(null);
		managementService = new ManagementService(game);
		combatService = new CombatService(game);
	}

	protected void mount() {
		this.removeAll();
		addLabel("Administration, " + game.getCurrentPlayer().getEmail(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN,
				GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
		button = new JButton("End Turn");
		button.setBounds(GraphicTest.LEFT_MARGIN, 540, GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.ROW_HEIGHT);
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
		addLabel("Prestige", GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT, GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		prestigeTF = new JTextField();
		prestigeTF.setBounds(GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT,
				GraphicTest.COLUMN_WIDTH, GraphicTest.ROW_HEIGHT);
		prestigeTF.setEditable(false);
		prestigeTF.setText(NumberFormats.PRESTIGE.format(game.getCurrentPlayerPosition().getPrestige()));
		add(prestigeTF);
		PositionTreePanel treePanel = new PositionTreePanel(this, game.getCurrentPlayerPosition(), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN
				* 3 + GraphicTest.ROW_HEIGHT * 2, GraphicTest.COLUMN_WIDTH_XLARGE, 435);
		add(treePanel.getPanel());
		/* variable */
		detailsPanel = new JPanel();
		detailsPanel.setLayout(null);
		add(detailsPanel);
		detailsPanel.setBounds(GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_XLARGE, GraphicTest.TOP_MARGIN, 1200, 560);
	}

	private void mountFormationPanel(){
		detailsPanel.removeAll();
		formationDetailPanel = new FormationDetailPanel(managementService);
		formationDetailPanel.setLayout(null);
		formationDetailPanel.setBounds(0, 0, GraphicTest.COLUMN_WIDTH * 6 + GraphicTest.LEFT_MARGIN, 560);
		detailsPanel.add(formationDetailPanel);
		repaint();
	}

	private void mountUnitDetailsPanel() {
		detailsPanel.removeAll();
		unitDetailPanel = new CompanyDetailPanel(managementService, combatService, this);
		unitDetailPanel.setLayout(null);
		unitDetailPanel.setBounds(0, 0, GraphicTest.COLUMN_WIDTH * 2 + GraphicTest.COLUMN_WIDTH_LARGE * 2 + GraphicTest.LEFT_MARGIN, 560);
		detailsPanel.add(unitDetailPanel);
		unitUpgradeInfoPanel = new CompanyUpgradePanel(managementService, combatService);
		unitUpgradeInfoPanel.setLayout(null);
		unitUpgradeInfoPanel.setBounds(GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH * 2
				+ GraphicTest.COLUMN_WIDTH_LARGE * 2, GraphicTest.TOP_MARGIN + GraphicTest.ROW_HEIGHT * 6,
				GraphicTest.COLUMN_WIDTH * 2 + GraphicTest.COLUMN_WIDTH_LARGE * 2, 400);
		detailsPanel.add(unitUpgradeInfoPanel);
		repaint();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		mount();
	}

	public void updateReinforceInfoPanel() {		
		reinforceInfoPanel = new FastPanel();
		int width = GraphicTest.LEFT_MARGIN * 3 + GraphicTest.COLUMN_WIDTH_NARROW * 2 + GraphicTest.COLUMN_WIDTH_XNARROW * 2;
		reinforceInfoPanel.setBounds(detailsPanel.getWidth() - width - 75, 0, width, 160);
		reinforceInfoPanel.setLayout(null);
		detailsPanel.add(reinforceInfoPanel);
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Reinforce Availability");
		reinforceInfoPanel.setBorder(title);
		double[] firstColumnValues = {1.0d, 2.0d, 2.5d, 3.0d, 3.5d};
		double[] secondColumnValues = {4.0d, 4.25d, 4.5d, 4.75d, 5.0d};
		for (int i = 0; i < 5; i++) {
			reinforceInfoPanel.addLabel(NumberFormats.XP.format(firstColumnValues[i]), GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
			reinforceInfoPanel.addNotEditableTextField(""+managementService.getReinforceAvailability(firstColumnValues[i], game.getCurrentPlayerPosition()), GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.ROW_HEIGHT);
			reinforceInfoPanel.addLabel(NumberFormats.XP.format(secondColumnValues[i]), GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_NARROW + GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
			reinforceInfoPanel.addNotEditableTextField(""+managementService.getReinforceAvailability(secondColumnValues[i], game.getCurrentPlayerPosition()), GraphicTest.LEFT_MARGIN * 2 + GraphicTest.COLUMN_WIDTH_NARROW * 2 + GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.TOP_MARGIN * 2 + GraphicTest.ROW_HEIGHT * i, GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.ROW_HEIGHT);
		}
	} 

	public void updateDetails(final Company company) {
		mountUnitDetailsPanel();
		unitDetailPanel.updateUnitPanel(company);
		updateReinforceInfoPanel();
	}

	public void updateDetails(final Formation formation){
		mountFormationPanel();
		formationDetailPanel.update(formation);
		updateReinforceInfoPanel();		
	}

	public void updateUpgradeUnitPanel(final Company company, final CompanyModel upgradeModel) {
		unitUpgradeInfoPanel.update(company, upgradeModel);
	}


	protected void updatePrestigeTF() {
		prestigeTF.setText(NumberFormats.PRESTIGE.format(game.getCurrentPlayerPosition().getPrestige()));
	}

}


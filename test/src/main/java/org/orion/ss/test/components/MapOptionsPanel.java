package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import org.orion.ss.test.GraphicTest;

public class MapOptionsPanel extends FastPanel {

	private final static double _radiusStep = 1.20d;

	private static final long serialVersionUID = -6836154494057986385L;

	private final ScrollableMap parent;

	final JCheckBox gridCB = new JCheckBox();
	final JCheckBox numberCB = new JCheckBox();
	final JCheckBox buildingsCB = new JCheckBox();
	final JCheckBox unitsCB = new JCheckBox();
	final JCheckBox infraCB = new JCheckBox();

	public MapOptionsPanel(ScrollableMap parent) {
		super();
		mount();
		this.parent = parent;
	}

	public void mount() {
		setLayout(null);
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Map Options"));
		final JButton zoomPlus = new JButton("Zoom +");
		zoomPlus.setBounds(
				GraphicTest.LEFT_MARGIN,
				GraphicTest.TOP_MARGIN * 2,
				GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		add(zoomPlus);
		final JButton zoomMinus = new JButton("Zoom -");
		zoomMinus.setBounds(
				GraphicTest.LEFT_MARGIN * 3 + GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.TOP_MARGIN * 2,
				GraphicTest.COLUMN_WIDTH_NARROW,
				GraphicTest.ROW_HEIGHT);
		zoomPlus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.setRadius(parent.getRadius() * _radiusStep);
				double newRadius = parent.getRadius() * _radiusStep;
				zoomMinus.setEnabled(true);
				if (newRadius > MapPanel.MAX_RADIUS) {
					zoomPlus.setEnabled(false);
				}
			}

		});
		zoomMinus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.setRadius(parent.getRadius() / _radiusStep);
				double newRadius = parent.getRadius() / _radiusStep;
				zoomPlus.setEnabled(true);
				if (newRadius < MapPanel.MIN_RADIUS) {
					zoomMinus.setEnabled(false);
				}
			}

		});
		add(zoomMinus);
		CheckBoxesListener listener = new CheckBoxesListener();
		gridCB.setBounds(GraphicTest.LEFT_MARGIN, zoomMinus.getY() + zoomMinus.getHeight(), GraphicTest.COLUMN_WIDTH_XXNARROW, GraphicTest.ROW_HEIGHT);
		gridCB.addActionListener(listener);
		gridCB.setActionCommand("grid");
		gridCB.setSelected(true);
		add(gridCB);
		addLabel("Grid", GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_XXNARROW, zoomMinus.getY() + zoomMinus.getHeight(), GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.ROW_HEIGHT);
		numberCB.setBounds(GraphicTest.LEFT_MARGIN, gridCB.getY() + gridCB.getHeight(), GraphicTest.COLUMN_WIDTH_XXNARROW, GraphicTest.ROW_HEIGHT);
		addLabel("Numbers", GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_XXNARROW, gridCB.getY() + gridCB.getHeight(), GraphicTest.COLUMN_WIDTH_NARROW, GraphicTest.ROW_HEIGHT);
		numberCB.addActionListener(listener);
		numberCB.setActionCommand("numbers");
		numberCB.setSelected(true);
		add(numberCB);
		buildingsCB.setBounds(GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_XXNARROW + GraphicTest.COLUMN_WIDTH_XNARROW, zoomMinus.getY() + zoomMinus.getHeight(), GraphicTest.COLUMN_WIDTH_XXNARROW, GraphicTest.ROW_HEIGHT);
		buildingsCB.addActionListener(listener);
		buildingsCB.setActionCommand("buildings");
		buildingsCB.setSelected(true);
		add(buildingsCB);
		addLabel("Buildings", buildingsCB.getX() + buildingsCB.getWidth(), zoomMinus.getY() + zoomMinus.getHeight(), GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.ROW_HEIGHT);
		unitsCB.setBounds(GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_XXNARROW + GraphicTest.COLUMN_WIDTH_XNARROW, zoomMinus.getY() + zoomMinus.getHeight() + GraphicTest.ROW_HEIGHT, GraphicTest.COLUMN_WIDTH_XXNARROW, GraphicTest.ROW_HEIGHT);
		unitsCB.addActionListener(listener);
		unitsCB.setActionCommand("units");
		unitsCB.setSelected(true);
		add(unitsCB);
		addLabel("Units", buildingsCB.getX() + buildingsCB.getWidth(), zoomMinus.getY() + zoomMinus.getHeight() + GraphicTest.ROW_HEIGHT, GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.ROW_HEIGHT);
		infraCB.setBounds(GraphicTest.LEFT_MARGIN + GraphicTest.COLUMN_WIDTH_XXNARROW + GraphicTest.COLUMN_WIDTH_XNARROW, zoomMinus.getY() + zoomMinus.getHeight() + GraphicTest.ROW_HEIGHT * 2, GraphicTest.COLUMN_WIDTH_XXNARROW, GraphicTest.ROW_HEIGHT);
		infraCB.addActionListener(listener);
		infraCB.setActionCommand("infra");
		infraCB.setSelected(true);
		add(infraCB);
		addLabel("Infra", buildingsCB.getX() + buildingsCB.getWidth(), zoomMinus.getY() + zoomMinus.getHeight() + GraphicTest.ROW_HEIGHT * 2, GraphicTest.COLUMN_WIDTH_XNARROW, GraphicTest.ROW_HEIGHT);
	}

	//TODO listeners
	class CheckBoxesListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("grid")) {
				parent.setDrawGrid(gridCB.isSelected());
			} else if (e.getActionCommand().equals("numbers")) {
				parent.setDrawNumbers(numberCB.isSelected());
			} else if (e.getActionCommand().equals("buildings")) {
				parent.setDrawBuildings(buildingsCB.isSelected());
			} else if (e.getActionCommand().equals("units")) {
				parent.setDrawUnits(unitsCB.isSelected());
			} else if (e.getActionCommand().equals("infra")) {
				parent.setDrawInfrastructures(infraCB.isSelected());
			}
			parent.repaint();
		}
	}
}

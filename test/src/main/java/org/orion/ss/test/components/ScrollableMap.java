package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScrollableMap extends JPanel {

	private static final long serialVersionUID = 7638456409070692278L;
	protected static final Logger logger = LoggerFactory.getLogger(ScrollableMap.class);

	private final static int _barThickness = 18;
	private final JScrollBar vbar;
	private final JScrollBar hbar;
	private final MapPanel mapPanel;
	private final int width;
	private final int height;

	public ScrollableMap(int x, int y, int w, int h, double size, Game game, Position observer, LocationUpdatable updatable, HexSet displayArea) {
		super();
		width = w;
		height = h;
		this.setLayout(null);
		this.setBounds(x, y, w, h);
		this.setBackground(Color.BLACK);
		mapPanel = new MapPanel(size, game, observer, updatable);
		mapPanel.setBounds(0, 0, width - _barThickness, height - _barThickness);
		mapPanel.setDisplayArea(displayArea);
		add(mapPanel);
		hbar = new JScrollBar(JScrollBar.HORIZONTAL);
		hbar.setBounds(0, height - _barThickness, width - _barThickness, _barThickness);
		hbar.setMaximum(10 + horizontalMax());
		hbar.addAdjustmentListener(new MapAdjustmentListener());
		add(hbar);
		vbar = new JScrollBar(JScrollBar.VERTICAL);
		vbar.setBounds(width - _barThickness, 0, _barThickness, height - _barThickness);
		vbar.setMaximum(10 + verticalMax());
		vbar.addAdjustmentListener(new MapAdjustmentListener());
		add(vbar);
	}

	private int verticalMax() {
		int max = mapPanel.getMapRows() - mapPanel.vertCapacity();
		return Math.min(max, mapPanel.getRows());
	}

	private int horizontalMax() {
		int max = mapPanel.getMapColumns() - mapPanel.horizCapacity();
		return Math.min(max, mapPanel.getColumns());
	}

	public void setSelectedUnit(Unit unit) {
		mapPanel.setSelectedUnit(unit);
		mapPanel.repaint();
	}

	public void setRadius(double radius) {
		mapPanel.setRadius(radius);
		hbar.setMaximum(10 + Math.max(0, horizontalMax()));
		vbar.setMaximum(10 + Math.max(0, verticalMax()));
		mapPanel.repaint();
	}

	public double getRadius() {
		return mapPanel.getRadius();
	}

	/* modes */

	public void setDrawSupplyArea(boolean value) {
		mapPanel.setDrawSupplyArea(value);
	}

	public void setDrawDeployArea(boolean value) {
		mapPanel.setDrawDeployArea(value);
	}

	public void setDrawNumbers(boolean value) {
		mapPanel.setDrawNumbers(value);
	}

	public void setDrawBuildings(boolean value) {
		mapPanel.setDrawBuildings(value);
	}

	public void setDrawUnits(boolean value) {
		mapPanel.setDrawUnits(value);
	}

	public void setDrawInfrastructures(boolean value) {
		mapPanel.setDrawInfrastructures(value);
	}

	/* getters & setters */

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) mapPanel.getOffset().getX(), (int) mapPanel.getOffset().getY(), mapPanel.horizCapacity(), mapPanel.vertCapacity());
	}

	/* event listeners */

	class MapAdjustmentListener implements AdjustmentListener {

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			mapPanel.setOffset(new Point(hbar.getValue(), vbar.getValue()));
			mapPanel.repaint();
		}

	}
}

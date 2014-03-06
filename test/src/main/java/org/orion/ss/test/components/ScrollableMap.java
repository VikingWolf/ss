package org.orion.ss.test.components;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import org.orion.ss.model.geo.GeoMap;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.UnitStack;
import org.orion.ss.service.GraphService;
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

	public ScrollableMap(GeoMap map, int x, int y, int w, int h, double size, HexSet displayArea, LocationUpdatable updatable, GraphService graphService) {
		super();
		width = w;
		height = h;
		this.setLayout(null);
		this.setBounds(x, y, w, h);
		this.setBackground(Color.BLACK);
		mapPanel = new MapPanel(size, map, updatable, graphService);
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
		int max = mapPanel.getMap().getRows() - mapPanel.vertCapacity();
		return Math.min(max, mapPanel.getRows());
	}

	private int horizontalMax() {
		int max = mapPanel.getMap().getColumns() - mapPanel.horizCapacity();
		return Math.min(max, mapPanel.getColumns());
	}

	public void setDeployArea(List<Location> deployArea) {
		mapPanel.setDeployArea(deployArea);
	}

	public void setUnits(Map<Location, UnitStack> units) {
		mapPanel.setUnits(units);
	}

	/* getters & setters */

	/* event listeners */

	class MapAdjustmentListener implements AdjustmentListener {

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			mapPanel.setOffset(new Point(hbar.getValue(), vbar.getValue()));
			mapPanel.repaint();
		}

	}
}

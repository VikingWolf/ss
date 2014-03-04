package overhaul;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.impl.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScrollableMap extends JPanel {
	
	private static final long serialVersionUID = 7638456409070692278L;
	protected static final Logger logger = LoggerFactory.getLogger(ScrollableMap.class);
	
	private final static int _barThickness = 18;
	private final JScrollBar vbar;
	private final JScrollBar hbar;
	private final MapPanel mapPanel;
	private final Scenario scenario;
	private final int width;
	private final int height;
	private final double size;

	public ScrollableMap(Scenario scenario, int x, int y, int w, int h, double size, List<Hex> displayArea){
		this(scenario, x, y, w, h, size);
		this.mapPanel.setDisplayArea(displayArea);
	}

	public ScrollableMap(Scenario scenario, int x, int y, int w, int h, double size){
		super();
		this.scenario = scenario;
		this.width = w;
		this.height = h;
		this.size = size;
		this.setLayout(null);
		this.setBounds(x, y, w, h);
		this.setBackground(Color.BLACK);
		mapPanel = new MapPanel(this.size, scenario);
		mapPanel.setBounds(0, 0, this.width-_barThickness, this.height-_barThickness);		
		add(mapPanel);
		hbar=new JScrollBar(JScrollBar.HORIZONTAL);
		hbar.setBounds(0, this.height-_barThickness, this.width-_barThickness, _barThickness);
		hbar.setMaximum(10 + horizontalMax());
		hbar.addAdjustmentListener(new MapAdjustmentListener( ));
        add(hbar);
        vbar=new JScrollBar(JScrollBar.VERTICAL);
        vbar.setBounds(this.width-_barThickness, 0, _barThickness, this.height-_barThickness);
		vbar.setMaximum(10 + verticalMax());
		vbar.addAdjustmentListener(new MapAdjustmentListener( ));
        add(vbar);
	}
	
	private int verticalMax(){
		int max = scenario.getMap().getRows() - mapPanel.vertCapacity() + 1;
		return Math.min(max, mapPanel.getColumns()+1);
	}
	
	private int horizontalMax(){
		int max = scenario.getMap().getColumns() - mapPanel.horizCapacity() + 1; 
		return Math.min(max, mapPanel.getRows()+1);
	}
	
	class MapAdjustmentListener implements AdjustmentListener {

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			mapPanel.setOffset(new Point(hbar.getValue(), vbar.getValue()));
			mapPanel.repaint();
		}
		
	}
}

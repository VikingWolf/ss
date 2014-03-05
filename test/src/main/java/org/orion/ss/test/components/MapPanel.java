package org.orion.ss.test.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.orion.ss.graph.Hexographer;
import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.GeoMap;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.HexSide;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.River;
import org.orion.ss.model.geo.Terrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapPanel extends JPanel {

	protected static final Logger logger = LoggerFactory.getLogger(MapPanel.class);
	private static final long serialVersionUID = -1578397214231068502L;
	private static Font _fontHexNumber;
	private final static Map<Terrain, Color> TERRAIN_COLORS = new HashMap<Terrain, Color>();
	
	/* colors */
	private final static Color _backgroundColor = Color.DARK_GRAY;
	private final static Color _gridColor = Color.BLACK;
	private final static Color _riversColor = Color.BLUE;
	private final static Color _deployAreaColor = Color.YELLOW;
	
	/* strokes */
	private final static BasicStroke _basicStroke = new BasicStroke(1.0f);
	private final static BasicStroke _deployStroke = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

	private final double radius;
	private final Hexographer hexographer;
	private final GeoMap map;
	private HexSet displayArea;
	private int initialX = 0;
	private int initialY = 0;
	private int finalX = 0;
	private int finalY = 0;
	private List<Point> deployArea;
	private LocationUpdatable toUpdate;
	private Map<Location, Unit> units;

	private Point offset;

	static {
		TERRAIN_COLORS.put(Terrain.DESERT, new Color(180, 115, 55));
		TERRAIN_COLORS.put(Terrain.HILLS, new Color(155, 155, 55));
		TERRAIN_COLORS.put(Terrain.ICE, new Color(255, 255, 255));
		TERRAIN_COLORS.put(Terrain.MOUNTAINS, new Color(100, 100, 100));
		TERRAIN_COLORS.put(Terrain.PLAINS, new Color(100, 255, 100));
		TERRAIN_COLORS.put(Terrain.SEA, new Color(100, 100, 255));
		TERRAIN_COLORS.put(Terrain.SWAMP, new Color(255, 100, 255));
	}

	public MapPanel(double radius, GeoMap map, LocationUpdatable parent) {
		super();
		offset = new Point(0, 0);
		this.radius = radius;
		hexographer = new Hexographer(radius);
		this.map = map;
		_fontHexNumber = new Font("Verdana", Font.PLAIN, (int) (radius / 5));
		this.addMouseListener(new MapMouseListener());
		deployArea = new ArrayList<Point>();
		this.toUpdate = parent;
	}

	public MapPanel(double radius, GeoMap map, LocationUpdatable parent, HexSet displayArea, Map<Location,Unit> units) {
		this(radius, map, parent);
		setUnits(units);
		setDisplayArea(displayArea);
	}

	private void computeBounds() {
		initialX = Integer.MAX_VALUE;
		initialY = Integer.MAX_VALUE;
		finalX = Integer.MIN_VALUE;
		finalY = Integer.MIN_VALUE;
		for (Hex hex : displayArea) {
			if (hex.getCoords().getX() < initialX) {
				initialX = (int) hex.getCoords().getX();
			}
			if (hex.getCoords().getY() < initialY) {
				initialY = (int) hex.getCoords().getY();
			}
			if (hex.getCoords().getX() > finalX) {
				finalX = (int) hex.getCoords().getX();
			}
			if (hex.getCoords().getY() > finalY) {
				finalY = (int) hex.getCoords().getY();
			}
		}
		if (initialX > 0) initialX--;
		if (initialY > 0) initialY--;
		if (finalX < map.getColumns()) finalX++;
		if (finalY < map.getRows()) finalY++;
	}

	@Override
	public void paint(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		g.setColor(_backgroundColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		paintTerrain(g);
		drawRivers(g);
		drawRoads(g);
		drawUnits(g);
		drawGrid(g);
		drawDeployArea(g);
	}

	private void drawRivers(Graphics2D g) {
		g.setColor(_riversColor);
		for (int i = -1; i < horizCapacity()+1; i++) {
			for (int j = -1; j < vertCapacity()+1; j++) {
				Point o = computeCenter(i, j);
				List<River> rivers = map.getRiversOf(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				for (River river : rivers) {
					g.setStroke(new BasicStroke(3.0f * river.getDeep(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
					g.drawPolygon(hexographer.getSide(o, river.getLoc().getSide()));
				}
			}
		}
	}

	private void drawDeployArea(Graphics2D g){
		g.setColor(_deployAreaColor);		
		for (int i = -1; i < horizCapacity()+1; i++) {
			for (int j = -1; j < vertCapacity()+1; j++) {
				Point o = computeCenter(i, j);
				Point coords = new Point(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				List<Point> list = this.getDeployArea();
				if (list.contains(coords)){
					g.drawString("Deploy", (int)o.getX() - textWidth(g, "Supply") / 2, (int)o.getY() - (int) (radius * 0.3));
					for (HexSide side : HexSide.values()){
						Point target = side.getAdjacent(coords);
						if (!list.contains(target)){
							g.setStroke(_deployStroke);
							g.drawPolygon(hexographer.getSide(o, side));
						}
					}
				}
			}
		}
	}
	private void drawRoads(Graphics2D g) {
		// TODO implementar
	}

	private void drawUnits(Graphics2D g){
		g.setColor(Color.BLACK);
		
		for (int i = -1; i < horizCapacity()+1; i++) {
			for (int j = -1; j < vertCapacity()+1; j++) {
				Point o = computeCenter(i, j);
				Location coords = new Location(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				if (units.containsKey(coords)){
					String text = "" + units.get(coords).getId(); 
					g.drawString(text, (int)o.getX() - textWidth(g, text) / 2, (int)o.getY() - (int) (radius * 0.7));					
				}
			}
		}
	}
	private void paintTerrain(Graphics2D g) {
		for (int i = -1; i < horizCapacity()+1; i++) {
			for (int j = -1; j < vertCapacity()+1; j++) {
				Point o = computeCenter(i, j);
				Hex hex = map.getHexAt(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				boolean displayed = true;
				if (displayArea != null) {
					displayed = false;
					if (displayArea.contains(hex)) {
						displayed = true;
					}
				}
				if ((hex != null) && (displayed)) {
					Color terrainColor = TERRAIN_COLORS.get(hex.getTerrain()); 
					g.setColor(terrainColor);
					g.fillPolygon(hexographer.getHex(o));
				}
			}
		}
	}

	private void drawGrid(Graphics2D g) {
		g.setColor(_gridColor);
		g.setStroke(_basicStroke);
		for (int i = -1; i < horizCapacity()+1; i++) {
			for (int j = -1; j < vertCapacity()+1; j++) {
				Point o = computeCenter(i, j);
				/* info */
				Point coords = new Point(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				Hex hex = map.getHexAt(coords);
				String hexNumber = formatHexNumber(hex);
				g.setFont(_fontHexNumber);
				if (hex != null) {
					String vegetation = hex.getVegetation().toString();
					g.drawString(vegetation, (int)o.getX() - textWidth(g, vegetation) / 2, (int)o.getY() - (int) (radius * 0.5));
					g.drawString(hexNumber, (int)o.getX() - textWidth(g, hexNumber) / 2, (int)o.getY() - (int) (radius * 0.7));
					g.drawPolygon(hexographer.getSide(o, HexSide.SOUTH));
					g.drawPolygon(hexographer.getSide(o, HexSide.NORTHWEST));
					g.drawPolygon(hexographer.getSide(o, HexSide.SOUTHWEST));
				}
			}
		}
	}

	private Point computeCenter(int i, int j){
		int oX = i * (hexographer.getColumnDistance()) + (int) (radius);
		int oY = j * (hexographer.getRowDistance()) + hexographer.getRowDistance() / 2;
		if ((i + (int) getOffset().getX()) % 2 == 0) {
			oY += hexographer.getRowDistance() / 2;
		}
		return new Point(oX, oY);
	}

	private int textWidth(Graphics2D graphics, String text) {
		FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
		return metrics.stringWidth(text);
	}

	private String formatHexNumber(Hex hex) {
		if (hex != null) {
			DecimalFormat df = new DecimalFormat("00");
			return df.format(hex.getCoords().getX()) + "," + df.format(hex.getCoords().getY());
		} else return "";
	}

	public int horizCapacity() {
		double capacity = Math.floor((double)this.getWidth() / hexographer.getColumnDistance());
		return (int) capacity;
	}

	public int vertCapacity() {
		double capacity = Math.floor((double)this.getHeight() / hexographer.getRowDistance());
		return (int) capacity;
	}
	
	protected Location computeCoordsForClick(int x, int y){
		double column = (double) x / hexographer.getColumnDistance();
		int iColumn = (int) Math.floor(column);
		double row = 0d;
		if (iColumn % 2 == 0){
			y -= hexographer.getRowDistance() / 2;
		} 
		row = (double) y / hexographer.getRowDistance();
		int iRowCandidate1 = (int) Math.floor(row);
		int iRowCandidate2 = (int) Math.ceil(row);
		Point centerCandidate1 = computeCenter(iColumn, iRowCandidate1);
		Point centerCandidate2 = computeCenter(iColumn, iRowCandidate2);
		double distance1 = computeDistance(new Point(x, y), centerCandidate1);
		double distance2 = computeDistance(new Point(x, y), centerCandidate2);
		int iRow = 0;
		if (distance1<distance2) iRow = iRowCandidate1;
		else iRow = iRowCandidate2;
		return new Location(iColumn + (int)this.offset.getX(), iRow + (int)this.offset.getY());
	}
	
	protected double computeDistance(Point a, Point b){
		double result = 0d;
		result += Math.pow(Math.abs(a.getX() - b.getX()), 2);
		result += Math.pow(Math.abs(a.getY() - b.getY()), 2);
		return Math.pow(result, 0.5d);
	}
	
	protected boolean isInsideMap(Location location){
		if (    (location.getX()<=this.finalX) ||
				(location.getX()>=this.initialX) ||
				(location.getY()<=this.finalY) ||
				(location.getY()>=this.initialY) ){
			return true;
		} else return false;
	}
	
	/* getters & setters */
	
	public Point getOffset() {
		return new Point((int) offset.getX() + initialX, (int) offset.getY() + initialY);
	}

	public List<Point> getDeployArea() {
		return deployArea;
	}

	public void setDeployArea(List<Point> deployArea) {
		this.deployArea = deployArea;
	}

	public void setOffset(Point offset) {
		this.offset = offset;
	}

	public HexSet getDisplayArea() {
		return displayArea;
	}

	public void setDisplayArea(HexSet displayArea) {
		this.displayArea = displayArea;
		computeBounds();
	}

	public int getRows() {
		return finalY - initialY;
	}

	public int getColumns() {
		return finalX - initialX;
	}

	public GeoMap getMap() {
		return map;
	}

	/* Event listeners */

	public Map<Location, Unit> getPosition() {
		return units;
	}

	public void setUnits(Map<Location, Unit> units) {
		this.units = units;
	}

	public LocationUpdatable getToUpdate() {
		return toUpdate;
	}

	public void setToUpdate(LocationUpdatable parent) {
		this.toUpdate = parent;
	}

	class MapMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			getToUpdate().update(computeCoordsForClick(e.getX(), e.getY()));
		}	
	}
}


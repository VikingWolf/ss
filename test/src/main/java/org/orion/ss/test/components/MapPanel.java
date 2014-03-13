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
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.orion.ss.model.Building;
import org.orion.ss.model.Unit;
import org.orion.ss.model.geo.Bridge;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.HexSide;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.OrientedLocation;
import org.orion.ss.model.geo.Railway;
import org.orion.ss.model.geo.River;
import org.orion.ss.model.geo.Road;
import org.orion.ss.model.geo.Terrain;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.model.impl.UnitStack;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.GraphService;
import org.orion.ss.service.Hexographer;
import org.orion.ss.service.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapPanel extends JPanel {

	protected static final Logger logger = LoggerFactory.getLogger(MapPanel.class);
	private static final long serialVersionUID = -1578397214231068502L;
	private final static Map<Terrain, Color> TERRAIN_COLORS = new HashMap<Terrain, Color>();

	public final static int MIN_RADIUS = 16;
	public final static int MAX_RADIUS = 128;

	/* colors */
	private final static Color _backgroundColor = Color.DARK_GRAY;
	private final static Color _gridColor = Color.BLACK;
	private final static Color _riversColor = Color.BLUE;
	private final static Color _deployAreaColor = Color.YELLOW;
	private final static Color _roadsColor = new Color(115, 60, 0);
	private final static Color _railwaysColor = Color.black;
	private final static Color _bridgesColorOuter = Color.BLACK;

	/* strokes */
	private final static float[] _railwaysDash = { 0.08f, 0.32f };
	private final static BasicStroke _basicStroke = new BasicStroke(1.0f);
	private final static BasicStroke _deployStroke = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

	static {
		TERRAIN_COLORS.put(Terrain.DESERT, new Color(180, 115, 55));
		TERRAIN_COLORS.put(Terrain.HILLS, new Color(155, 155, 55));
		TERRAIN_COLORS.put(Terrain.ICE, new Color(255, 255, 255));
		TERRAIN_COLORS.put(Terrain.MOUNTAINS, new Color(100, 100, 100));
		TERRAIN_COLORS.put(Terrain.PLAINS, new Color(100, 255, 100));
		TERRAIN_COLORS.put(Terrain.SEA, new Color(100, 100, 255));
		TERRAIN_COLORS.put(Terrain.SWAMP, new Color(255, 100, 255));
	}

	/* fonts */
	private Font fontSmall;

	/* modes */
	private boolean drawGrid = true;
	private boolean drawNumbers = true;
	private boolean drawBuildings = true;
	private boolean drawUnits = true;
	private boolean drawInfrastructures = true;

	private final Hexographer hexographer;
	private final GeoService geoService;

	private double radius;
	private HexSet displayArea;
	private int initialX = 0;
	private int initialY = 0;
	private int finalX = 0;
	private int finalY = 0;
	private List<Location> deployArea;
	private LocationUpdatable toUpdate;
	private Unit selectedUnit = null;
	private final Position observer;

	private Point offset;

	private final GraphService graphService;

	public MapPanel(double radius, Game game, Position observer, LocationUpdatable parent) {
		super();
		offset = new Point(0, 0);
		this.observer = observer;
		hexographer = new Hexographer(radius);
		geoService = ServiceFactory.getGeoService(game);
		graphService = ServiceFactory.getGraphService(game);
		this.addMouseListener(new MapMouseListener());
		deployArea = new ArrayList<Location>();
		toUpdate = parent;
		setRadius(radius);
	}

	public MapPanel(double radius, Game game, Position observer, LocationUpdatable parent, HexSet displayArea, Map<Location, UnitStack> units, GraphService graphService) {
		this(radius, game, observer, parent);
		setDisplayArea(displayArea);
	}

	private void computeBounds() {
		initialX = Integer.MAX_VALUE;
		initialY = Integer.MAX_VALUE;
		finalX = Integer.MIN_VALUE;
		finalY = Integer.MIN_VALUE;
		for (Hex hex : displayArea) {
			if (hex.getCoords().getX() < initialX) {
				initialX = hex.getCoords().getX();
			}
			if (hex.getCoords().getY() < initialY) {
				initialY = hex.getCoords().getY();
			}
			if (hex.getCoords().getX() > finalX) {
				finalX = hex.getCoords().getX();
			}
			if (hex.getCoords().getY() > finalY) {
				finalY = hex.getCoords().getY();
			}
		}
		if (initialX > 0) initialX--;
		if (initialY > 0) initialY--;
		if (finalX < geoService.getMap().getColumns()) finalX++;
		if (finalY < geoService.getMap().getRows()) finalY++;
	}

	@Override
	public void paint(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		g.setColor(_backgroundColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		paintTerrain(g);
		drawGrid(g);
		drawRivers(g);
		if (drawInfrastructures) {
			drawRoads(g);
			drawRailways(g);
			drawBridges(g);
		}
		if (drawBuildings) {
			drawBuildings(g);
		}
		drawDeployArea(g);
		if (drawUnits) {
			drawUnits(g);
		}
	}

	private void drawBridges(Graphics2D g) {
		for (Bridge bridge : geoService.getBridgesOf(getBounds())) {
			GeneralPath pathOuter = new GeneralPath();
			GeneralPath pathInner = new GeneralPath();
			for (int i = 0; i < bridge.getLocations().size(); i++) {
				OrientedLocation location = bridge.getLocations().get(i);
				Point o = computeCenter(location.getX() - (int) getOffset().getX(), location.getY() - (int) getOffset().getY());
				hexographer.addRadius(pathOuter, o, location.getSide(), i, 0.3d);
				hexographer.addRadius(pathInner, o, location.getSide(), i, 0.25d);
			}
			g.setColor(_bridgesColorOuter);
			g.setStroke(new BasicStroke((int) (radius / 4), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
			g.draw(pathOuter);
			g.setColor(graphService.getBridgeColor(bridge));
			g.setStroke(new BasicStroke((int) (radius / 8), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
			g.draw(pathInner);
		}
	}

	private void drawRivers(Graphics2D g) {
		g.setColor(_riversColor);
		for (River river : geoService.getRiversOf(getBounds())) {
			GeneralPath path = new GeneralPath();
			for (OrientedLocation location : river.getLocations()) {
				Point o = computeCenter(location.getX() - (int) getOffset().getX(), location.getY() - (int) getOffset().getY());
				hexographer.addSide(path, o, location.getSide());
			}
			g.setStroke(new BasicStroke((int) (radius * river.getDeep() / 12), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.draw(path);
		}
	}

	private void drawRoads(Graphics2D g) {
		g.setColor(_roadsColor);
		for (Road road : geoService.getRoadsOf(getBounds())) {
			GeneralPath path = new GeneralPath();
			for (int i = 0; i < road.getLocations().size(); i++) {
				OrientedLocation location = road.getLocations().get(i);
				Point o = computeCenter(location.getX() - (int) getOffset().getX(), location.getY() - (int) getOffset().getY());
				hexographer.addRadius(path, o, location.getSide(), i);
			}
			g.setStroke(new BasicStroke((int) (radius / 6), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.draw(path);
		}
	}

	private void drawRailways(Graphics2D g) {
		g.setColor(_railwaysColor);
		for (Railway railway : geoService.getRailwaysOf(getBounds())) {
			GeneralPath path = new GeneralPath();
			for (int i = 0; i < railway.getLocations().size(); i++) {
				OrientedLocation location = railway.getLocations().get(i);
				Point o = computeCenter(location.getX() - (int) getOffset().getX(), location.getY() - (int) getOffset().getY());
				hexographer.addRadius(path, o, location.getSide(), i);
			}
			g.setStroke(new BasicStroke((int) (radius / 12), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.draw(path);
			g.setStroke(new BasicStroke((float) (radius / 4), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, adjustDashToHexSize(), 0.5f));
			g.draw(path);
		}
	}

	private float[] adjustDashToHexSize() {
		float[] result = new float[_railwaysDash.length];
		for (int i = 0; i < _railwaysDash.length; i++) {
			result[i] = (float) (_railwaysDash[i] * radius);
		}
		return result;
	}

	private void drawDeployArea(Graphics2D g) {
		g.setColor(_deployAreaColor);
		for (int i = -1; i < horizCapacity() + 1; i++) {
			for (int j = -1; j < vertCapacity() + 1; j++) {
				Point o = computeCenter(i, j);
				Location coords = new Location(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				List<Location> list = this.getDeployArea();
				if (list.contains(coords)) {
					for (HexSide side : HexSide.values()) {
						Location target = side.getAdjacent(coords);
						if (!list.contains(target)) {
							g.setStroke(_deployStroke);
							g.drawPolygon(hexographer.getSide(o, side));
						}
					}
				}
			}
		}
	}

	private void drawBuildings(Graphics2D g) {
		Map<Location, Building> buildings = geoService.getBuildingsAt(getBounds());
		for (int i = -1; i < horizCapacity() + 1; i++) {
			for (int j = -1; j < vertCapacity() + 1; j++) {
				Point o = computeCenter(i, j);
				Location coords = new Location(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				if (buildings.containsKey(coords)) {
					BufferedImage symbol = graphService.getBuildingSymbol(buildings.get(coords), observer);
					g.drawImage(symbol, (int) o.getX() - symbol.getWidth() / 2, (int) o.getY() - symbol.getHeight() / 2, symbol.getWidth(), symbol.getHeight(), null);
				}
			}
		}
	}

	private void drawUnits(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(fontSmall);
		Map<Location, UnitStack> units = geoService.getAllUnitsLocatedAt(getBounds());
		Map<Location, Stock> supplies = geoService.getStocksLocatedAt(getBounds(), geoService.getGame().getCurrentPlayerPosition());
		for (int i = -1; i < horizCapacity() + 1; i++) {
			for (int j = -1; j < vertCapacity() + 1; j++) {
				Point o = computeCenter(i, j);
				Location coords = new Location(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				if (units.containsKey(coords)) {
					UnitStack stack = units.get(coords);
					int symbolSize = 0;
					if (stack.size() > 0) {
						if (selectedUnit != null) {
							if (stack.containsAnyElementOf(selectedUnit)) {
								g.setColor(Color.RED);
								g.fillRect((int) o.getX() - graphService.getSymbolSize() / 2 - 3, (int) o.getY() - graphService.getSymbolSize() / 2 - 3, graphService.getSymbolSize() + 6, graphService.getSymbolSize() + 6);
							}
						}
						BufferedImage symbol = graphService.getUnitSymbol(stack.get(stack.size() - 1));
						symbolSize = symbol.getWidth();
						g.drawImage(symbol, (int) o.getX() - symbol.getWidth() / 2, (int) o.getY() - symbol.getHeight() / 2, symbol.getWidth(), symbol.getHeight(), null);
					}
					if (stack.size() > 1) {
						g.drawString("" + stack.size(), (int) o.getX() - (int) (radius * 0.75), (int) o.getY());
					}
					if (supplies.containsKey(coords)) {
						// draw small stocks
						BufferedImage symbol = graphService.getSupplySmallSymbol(supplies.get(coords));
						g.drawImage(symbol, (int) o.getX() + symbolSize / 2, (int) o.getY() - symbol.getHeight() / 2, symbol.getWidth(), symbol.getHeight(), null);
					}
				} else {
					if (supplies.containsKey(coords)) {
						BufferedImage symbol = graphService.getSupplySymbol(supplies.get(coords));
						g.drawImage(symbol, (int) o.getX() - symbol.getWidth() / 2, (int) o.getY() - symbol.getHeight() / 2, symbol.getWidth(), symbol.getHeight(), null);
						// draw stocks
					}
				}
			}
		}
	}

	private void paintTerrain(Graphics2D g) {
		for (int i = -1; i < horizCapacity() + 1; i++) {
			for (int j = -1; j < vertCapacity() + 1; j++) {
				Point o = computeCenter(i, j);
				Hex hex = geoService.getMap().getHexAt(i + (int) getOffset().getX(), j + (int) getOffset().getY());
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
		HexSet visible = geoService.getVisibleArea(observer, this.getBounds());
		g.setColor(_gridColor);
		g.setStroke(_basicStroke);
		for (int i = -1; i < horizCapacity() + 1; i++) {
			for (int j = -1; j < vertCapacity() + 1; j++) {
				Point o = computeCenter(i, j);
				/* info */
				Location coords = new Location(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				Hex hex = geoService.getMap().getHexAt(coords);
				String hexNumber = formatHexNumber(hex);
				g.setFont(fontSmall);
				if (hex != null) {
					String vegetation = hex.getVegetation().toString();
					g.drawString(vegetation, (int) o.getX() - textWidth(g, vegetation) / 2, (int) o.getY() - (int) (radius * 0.5));
					if (drawNumbers)
						g.drawString(hexNumber, (int) o.getX() - textWidth(g, hexNumber) / 2, (int) o.getY() - (int) (radius * 0.7));
					if (!visible.contains(hex)) {
						Color color = new Color(0, 0, 0, 150);
						g.setColor(color);
						g.fillPolygon(hexographer.getHex(o));
					}
					if (drawGrid) {
						g.setColor(Color.BLACK);
						g.drawPolygon(hexographer.getSide(o, HexSide.SOUTH));
						g.drawPolygon(hexographer.getSide(o, HexSide.NORTHWEST));
						g.drawPolygon(hexographer.getSide(o, HexSide.SOUTHWEST));
					}
				}
			}
		}
	}

	private Point computeCenter(int i, int j) {
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
		double capacity = Math.floor((double) this.getWidth() / hexographer.getColumnDistance());
		return (int) capacity;
	}

	public int vertCapacity() {
		double capacity = Math.floor((double) this.getHeight() / hexographer.getRowDistance());
		return (int) capacity;
	}

	protected Location computeCoordsForClick(int x, int y) {
		double column = (double) x / hexographer.getColumnDistance();
		int iColumn = (int) Math.floor(column);
		double row = 0d;
		if (iColumn % 2 == 0) {
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
		if (distance1 < distance2)
			iRow = iRowCandidate1;
		else iRow = iRowCandidate2;
		return new Location(iColumn + (int) offset.getX(), iRow + (int) offset.getY());
	}

	protected double computeDistance(Point a, Point b) {
		double result = 0d;
		result += Math.pow(Math.abs(a.getX() - b.getX()), 2);
		result += Math.pow(Math.abs(a.getY() - b.getY()), 2);
		return Math.pow(result, 0.5d);
	}

	protected boolean isInsideMap(Location location) {
		if ((location.getX() <= finalX) ||
				(location.getX() >= initialX) ||
				(location.getY() <= finalY) ||
				(location.getY() >= initialY)) {
			return true;
		} else return false;
	}

	/* getters & setters */

	public Point getOffset() {
		return new Point((int) offset.getX() + initialX, (int) offset.getY() + initialY);
	}

	public List<Location> getDeployArea() {
		return deployArea;
	}

	public void setDeployArea(List<Location> deployArea) {
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

	public LocationUpdatable getToUpdate() {
		return toUpdate;
	}

	public void setToUpdate(LocationUpdatable parent) {
		toUpdate = parent;
	}

	public int getMapRows() {
		return geoService.getMap().getRows();
	}

	public int getMapColumns() {
		return geoService.getMap().getColumns();
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		graphService.setSymbolSize((int) (radius * 1.20));
		hexographer.setRadius(radius);
		this.radius = radius;
		fontSmall = new Font("Verdana", Font.PLAIN, (int) (radius / 5));
		if (displayArea != null)
			computeBounds();
	}

	/* Event listeners */

	public boolean isDrawGrid() {
		return drawGrid;
	}

	public void setDrawGrid(boolean drawGrid) {
		this.drawGrid = drawGrid;
	}

	public boolean isDrawNumbers() {
		return drawNumbers;
	}

	public void setDrawNumbers(boolean drawNumbers) {
		this.drawNumbers = drawNumbers;
	}

	public boolean isDrawBuildings() {
		return drawBuildings;
	}

	public void setDrawBuildings(boolean drawBuildings) {
		this.drawBuildings = drawBuildings;
	}

	public boolean isDrawUnits() {
		return drawUnits;
	}

	public void setDrawUnits(boolean drawUnits) {
		this.drawUnits = drawUnits;
	}

	public boolean isDrawInfrastructures() {
		return drawInfrastructures;
	}

	public void setDrawInfrastructures(boolean drawInfrastructures) {
		this.drawInfrastructures = drawInfrastructures;
	}

	public Unit getSelectedUnit() {
		return selectedUnit;
	}

	public void setSelectedUnit(Unit selectedUnit) {
		this.selectedUnit = selectedUnit;
	}

	class MapMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == 1) {
				getToUpdate().updateLocation(computeCoordsForClick(e.getX(), e.getY()));
			}
			if (e.getButton() == 3) {
				getToUpdate().locationInfo(computeCoordsForClick(e.getX(), e.getY()), e.getX() - (int) offset.getX(), e.getY() - (int) offset.getY());
			}

		}
	}
}

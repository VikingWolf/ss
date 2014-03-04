package overhaul;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSide;
import org.orion.ss.model.geo.River;
import org.orion.ss.model.geo.Terrain;
import org.orion.ss.model.impl.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapPanel extends JPanel {
	
	protected static final Logger logger = LoggerFactory.getLogger(MapPanel.class);	
	private static final long serialVersionUID = -1578397214231068502L;
	private static Font _fontHexNumber;
	private final static Map<Terrain, Color> TERRAIN_COLORS = new HashMap<Terrain, Color>();

		
	private final double radius;
	private final Hexographer hexographer;
	private final Scenario scenario;
	private List<Hex> displayArea;
	private int initialX = 0;
	private int initialY = 0;
	private int finalX = 0;
	private int finalY = 0;

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

	public MapPanel(double radius, Scenario scenario, List<Hex> displayArea){
		this(radius, scenario);
		setDisplayArea(displayArea);
	}
	
	private void computeBounds(){
		initialX = Integer.MAX_VALUE;
		initialY = Integer.MAX_VALUE;
		finalX = Integer.MIN_VALUE;
		finalY = Integer.MIN_VALUE;
		for (Hex hex : displayArea){
			if (hex.getCoords().getX()<initialX){
				initialX = (int)hex.getCoords().getX();
			}
			if (hex.getCoords().getY()<initialY){
				initialY = (int)hex.getCoords().getY();				
			}			
			if (hex.getCoords().getX()<finalX){
				finalX = (int)hex.getCoords().getX();
			}
			if (hex.getCoords().getY()>finalY){
				finalY = (int)hex.getCoords().getY();				
			}					
		}
		if (initialX>0) initialX--;
		if (initialY>0) initialY--;
		if (finalX<this.scenario.getMap().getColumns()) finalX++;
		if (finalY<this.scenario.getMap().getRows()) finalY++;
	}
	
	public MapPanel(double radius, Scenario scenario){
		super();
		offset = new Point(0, 0);
		this.radius = radius;
		hexographer = new Hexographer(radius);
		this.scenario = scenario;		
		 _fontHexNumber = new Font("Verdana", Font.PLAIN, (int)(radius / 5));
	}

	@Override
	public void paint(Graphics gr){
		Graphics2D g = (Graphics2D) gr;
		g.setColor(Color.WHITE);
		g.fillRect(0,  0, this.getWidth(), this.getHeight());
		paintTerrain(g);
		drawRivers(g);
		drawRoads(g);
		drawGrid(g);
	}

	private void drawRivers(Graphics2D g){
		g.setColor(Color.BLUE);
		
		for (int i = 0; i < horizCapacity(); i++){
			for (int j = 0; j < vertCapacity(); j++){
				int oX = i * (hexographer.getColumnDistance()) + (int)(radius);
				int oY = j * (hexographer.getRowDistance());
				if ( (i + (int) getOffset().getX() )%2 == 0){
					oY += hexographer.getRowDistance()/2;
				}
				List<River> rivers = scenario.getMap().getRiversOf(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				for (River river : rivers){
					g.setStroke(new BasicStroke(3.0f * river.getDeep(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
					g.drawPolygon(hexographer.getSide(new Point(oX, oY), river.getLoc().getSide()));
				}
			}
		}
	}
	
	private void drawRoads(Graphics2D g){
		//TODO implementar
	}
	
	private void paintTerrain(Graphics2D g){
		for (int i = 0; i < horizCapacity(); i++){
			for (int j = 0; j < vertCapacity(); j++){
				int oX = i * (hexographer.getColumnDistance()) + (int)(radius);
				int oY = j * (hexographer.getRowDistance());
				if ( (i + (int) getOffset().getX() )%2 == 0){
					oY += hexographer.getRowDistance()/2;
				} 
				Hex hex = this.scenario.getMap().getHexAt(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				boolean displayed = true;
				if (displayArea != null){
					displayed = false;
					if (displayArea.contains(hex)){
						displayed = true;
					}
				}
				if ((hex != null) && (displayed)){
					g.setColor(TERRAIN_COLORS.get(hex.getTerrain()));
					g.fillPolygon(hexographer.getHex(new Point(oX,oY)));
				}
			}
		}		
	}

	private void drawGrid(Graphics2D g){
		g.setColor(Color.BLACK);
		for (int i = 0; i < horizCapacity(); i++){
			for (int j = 0; j < vertCapacity(); j++){
				int oX = i * (hexographer.getColumnDistance()) + (int)(radius);
				int oY = j * (hexographer.getRowDistance());
				/* grid lines */
				if ( (i + (int) getOffset().getX() )%2 == 0){
					oY += hexographer.getRowDistance()/2;
					g.drawPolygon(hexographer.getHex(new Point(oX,oY)));
				} else {
					g.drawPolygon(hexographer.getSide(new Point(oX, oY), HexSide.SOUTH));
				}
				/* info */
				Hex hex = scenario.getMap().getHexAt(i + (int) getOffset().getX(), j + (int) getOffset().getY());
				String hexNumber = formatHexNumber(hex);
				g.setFont(_fontHexNumber);
				g.drawString(hexNumber, oX - textWidth(g, hexNumber) / 2, oY - (int)(radius * 0.5));
				if (hex != null){
					String vegetation = hex.getVegetation().toString(); 
					g.drawString(vegetation, oX - textWidth(g, vegetation)/2, oY - (int)(radius * 0.3));
				}
			}
		}
	}
	
	private int textWidth(Graphics2D graphics, String text){
		FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
		return metrics.stringWidth(text);		
	}
	
	private String formatHexNumber(Hex hex){		
		DecimalFormat df = new DecimalFormat("00");		
		return df.format(hex.getCoords().getX()) + "," + df.format(hex.getCoords().getY());		
	}
	
	public int horizCapacity(){
		return (int) (Math.ceil(this.getWidth() / hexographer.getColumnDistance())) + 1;
	}
	
	public int vertCapacity(){
		return (int) (Math.ceil(this.getHeight() / hexographer.getRowDistance())) + 1;
	}

	public Point getOffset() {
		return new Point((int)offset.getX() + this.initialX, (int)offset.getY() + this.initialY);
	}

	public void setOffset(Point offset) {
		this.offset = offset;
	}

	public List<Hex> getDisplayArea() {
		return displayArea;
	}

	public void setDisplayArea(List<Hex> displayArea) {
		this.displayArea = displayArea;
		computeBounds();
	}
	
	public int getRows(){
		return finalY - initialY;
	}
	
	public int getColumns(){
		return finalX - initialX;
	}
	
}
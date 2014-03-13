package org.orion.ss.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Building;
import org.orion.ss.model.Unit;
import org.orion.ss.model.core.CompanyTrait;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.ObjectiveType;
import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.geo.Airfield;
import org.orion.ss.model.geo.Bridge;
import org.orion.ss.model.geo.Fortification;
import org.orion.ss.model.geo.UrbanCenter;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.utils.NumberFormats;

public class GraphService extends Service {

	private final static List<Color> _corpsColors = new ArrayList<Color>();

	private final static int FORTIFICATION_HIGH = 5;

	static {
		int[] rgbValues = { 0, 75, 165, 255 };
		int darkTreshold = rgbValues[1];
		for (int red : rgbValues) {
			for (int green : rgbValues) {
				for (int blue : rgbValues) {
					if (red > darkTreshold || green > darkTreshold || blue > darkTreshold) {
						_corpsColors.add(new Color(red, green, blue));
					}
				}
			}
		}
	}

	private final ScenarioService scenarioService;
	private final GameService gameService;

	private int symbolSize;
	private Font smallFont;
	private Font smallBoldFont;
	private Font boldFont;
	private Font xpFont;

	private Point symbolCenter;
	private Point symbolUL;

	protected GraphService(Game game) {
		super(game);
		scenarioService = ServiceFactory.getScenarioService(game);
		gameService = ServiceFactory.getGameService(game);
	}

	public Color getBridgeColor(Bridge bridge) {
		Color result = null;
		switch (bridge.getBridgeType()) {
			case STONE:
				result = Color.LIGHT_GRAY;
			break;
			case STEEL:
				result = Color.WHITE;
			break;
			case WOOD:
				result = new Color(100, 75, 0);
			break;
			case POONTOON:
				result = Color.RED;
			break;
			default:
			break;

		}
		return result;
	}

	public BufferedImage getBuildingSymbol(Building building, Position position) {
		double heightRatio = 1.4d;
		double widthRatio = 1.3d;
		BufferedImage result = new BufferedImage((int) (getSymbolSize() * widthRatio), (int) (getSymbolSize() * heightRatio), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D canvas = (Graphics2D) result.getGraphics();
		if (building instanceof UrbanCenter) {
			UrbanCenter center = (UrbanCenter) building;
			drawUrbanCenterSymbol(center, canvas, position, widthRatio, heightRatio);
		} else if (building instanceof Airfield) {
			Airfield airfield = (Airfield) building;
			drawAirfieldSymbol(airfield, canvas, position, widthRatio, heightRatio);
		} else if (building instanceof Fortification) {
			Fortification fortification = (Fortification) building;
			drawFortificationSymbol(fortification, canvas, position, widthRatio);
		}

		return result;
	}

	protected void drawFortificationSymbol(Fortification fortification, Graphics2D canvas, Position position, double widthRatio) {
		double size = 0.60;
		double x = 0.20 + widthRatio / 2 - 0.5;
		double y = 0.40;
		double border = 0.10;
		canvas.setColor(Color.BLACK);
		canvas.fillRect(getSize(x) - getSize(border), getSize(y) - getSize(border), getSize(size) + 2 * getSize(border), getSize(size) + 2 * getSize(border));
		drawCountryFlag(fortification, canvas, position, size, x, y);
		drawFortificationStrength(fortification.getStrength(), canvas, size + 2 * border, x - border, y - border);

	}

	protected void drawFortificationStrength(int strength, Graphics2D canvas, double dSize, double dX, double dY) {
		int fortSize = getSize(0.2d);
		canvas.setColor(Color.BLACK);
		canvas.fillRect(getSize(dX) - fortSize / 2, getSize(dY) - fortSize / 2, fortSize, fortSize);
		canvas.fillRect(getSize(dX) + getSize(dSize) - fortSize / 2, getSize(dY) - fortSize / 2, fortSize, fortSize);
		canvas.fillRect(getSize(dX) - fortSize / 2, getSize(dY) + getSize(dSize) - fortSize / 2, fortSize, fortSize);
		canvas.fillRect(getSize(dX) + getSize(dSize) - fortSize / 2, getSize(dY) + getSize(dSize) - fortSize / 2, fortSize, fortSize);
		if (strength >= FORTIFICATION_HIGH) {
			Polygon triangle = new Polygon();
			triangle.addPoint(getSize(dX) + getSize(dSize) / 2 - fortSize / 2, getSize(dY));
			triangle.addPoint(getSize(dX) + getSize(dSize) / 2, getSize(dY) - fortSize);
			triangle.addPoint(getSize(dX) + getSize(dSize) / 2 + fortSize / 2, getSize(dY));
			canvas.fillPolygon(triangle);
			triangle = new Polygon();
			triangle.addPoint(getSize(dX) + getSize(dSize) / 2 - fortSize / 2, getSize(dY) + getSize(dSize));
			triangle.addPoint(getSize(dX) + getSize(dSize) / 2, getSize(dY) + getSize(dSize) + fortSize);
			triangle.addPoint(getSize(dX) + getSize(dSize) / 2 + fortSize / 2, getSize(dY) + getSize(dSize));
			canvas.fillPolygon(triangle);
			triangle = new Polygon();
			triangle.addPoint(getSize(dX), getSize(dY) + getSize(dSize) / 2 - fortSize / 2);
			triangle.addPoint(getSize(dX) - fortSize, getSize(dY) + getSize(dSize) / 2);
			triangle.addPoint(getSize(dX), getSize(dY) + getSize(dSize) / 2 + fortSize / 2);
			canvas.fillPolygon(triangle);
			triangle = new Polygon();
			triangle.addPoint(getSize(dX) + getSize(dSize), getSize(dY) + getSize(dSize) / 2 - fortSize / 2);
			triangle.addPoint(getSize(dX) + getSize(dSize) + fortSize, getSize(dY) + getSize(dSize) / 2);
			triangle.addPoint(getSize(dX) + getSize(dSize), getSize(dY) + getSize(dSize) / 2 + fortSize / 2);
			canvas.fillPolygon(triangle);
		}
	}

	protected void drawAirfieldSymbol(Airfield airfield, Graphics2D canvas, Position position, double widthRatio, double heightRatio) {
		drawCountryFlag(airfield, canvas, position, 0.60, 0.20 + widthRatio / 2 - 0.5, 0.25);
		BufferedImage flag = scenarioService.getAirfieldSymbol();
		int symbolSize = getSize(0.45);
		int x = getSize(widthRatio) / 2 - symbolSize / 2;
		int y = (int) (getSize(0.68) * heightRatio);
		canvas.drawImage(flag.getScaledInstance(symbolSize, symbolSize, Image.SCALE_SMOOTH), x, y, new Color(0, 0, 0, 0), null);
	}

	protected void drawUrbanCenterSymbol(UrbanCenter center, Graphics2D canvas, Position position, double widthRatio, double heightRatio) {
		canvas.setFont(smallFont);
		drawCountryFlag(center, canvas, position, 0.70, 0.15 + widthRatio / 2 - 0.5, 0.35);
		FontMetrics metrics = canvas.getFontMetrics(canvas.getFont());
		int x = getSize(1, widthRatio) / 2 - metrics.stringWidth(center.getName()) / 2;
		int y = (int) (getSize(1) * heightRatio) - smallFont.getSize() / 3;
		canvas.setColor(Color.BLACK);
		canvas.fillRect(x - 3, y - smallFont.getSize(), metrics.stringWidth(center.getName()) + 6, smallFont.getSize() + 4);
		canvas.setColor(Color.WHITE);
		canvas.drawString(center.getName(), x, y);
	}

	public BufferedImage getSupplySymbol(Stock stock) {
		BufferedImage result = new BufferedImage(getSymbolSize(), getSymbolSize(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D canvas = (Graphics2D) result.getGraphics();
		canvas.setColor(Color.BLACK);
		canvas.fillRect(0, 0, result.getWidth(), result.getHeight());
		canvas.setColor(Color.WHITE);
		canvas.fillRect(2, 2, result.getWidth() - 4, result.getHeight() - 4);
		canvas.setColor(Color.BLACK);
		canvas.setFont(smallFont);
		FontMetrics metrics = canvas.getFontMetrics(canvas.getFont());
		int i = 1;
		for (SupplyType supply : stock.keySet()) {
			String name = supply.getDenomination();
			canvas.drawString(name, smallFont.getSize(), i * (int) (smallFont.getSize() * 1.6));
			String amount = NumberFormats.DF_2.format(stock.get(supply));
			canvas.drawString(amount, result.getWidth() - (int) (smallFont.getSize() * 0.5) - metrics.stringWidth(amount), i * (int) (smallFont.getSize() * 1.6));
			i++;
		}
		return result;
	}

	public BufferedImage getSupplySmallSymbol(Stock stock) {
		BufferedImage result = new BufferedImage(getSymbolSize() / 5, stock.size() * getSymbolSize() / 5, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D canvas = (Graphics2D) result.getGraphics();
		canvas.setColor(Color.BLACK);
		canvas.fillRect(0, 0, result.getWidth(), result.getHeight());
		canvas.setColor(Color.WHITE);
		canvas.setFont(smallBoldFont);
		FontMetrics metrics = canvas.getFontMetrics(canvas.getFont());
		int i = 1;
		for (SupplyType supply : stock.keySet()) {
			String code = supply.getDenomination().substring(0, 1).toUpperCase();
			canvas.drawString(code,
					(result.getWidth() - metrics.stringWidth(code)) / 2,
					i * result.getHeight() / (2 * stock.size()) + smallBoldFont.getSize() * i / 2);
			i++;
		}
		return result;
	}

	public BufferedImage getUnitSymbol(Unit unit) {
		if (unit instanceof Formation) {
			return getUnitSymbol((Formation) unit);
		} else if (unit instanceof Company) {
			return getUnitSymbol((Company) unit);

		}
		else return null;
	}

	public BufferedImage getUnitSymbol(Formation unit) {
		BufferedImage result = new BufferedImage(getSymbolSize(), getSymbolSize(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D canvas = mountCanvas(result, unit);
		drawTroopTypeSymbol(unit.getTroopType(), canvas);
		drawFormationLevel(unit, canvas);
		drawSupplyLimit(unit, canvas);
		drawFormationStrength(unit, canvas);
		if (unit.getTogetherCompanies().size() == 1) {
			int textWidth = textWidth(canvas, "HQ");
			canvas.setColor(Color.WHITE);
			canvas.drawString("HQ", (int) symbolCenter.getX() - textWidth / 2, (int) symbolCenter.getY() + boldFont.getSize() / 2);
		}
		drawFlag(unit, canvas);
		drawCorpsStrip(unit, canvas);
		drawUnitName(unit, canvas);
		return result;
	}

	public BufferedImage getUnitSymbol(Company unit) {
		BufferedImage result = new BufferedImage(getSymbolSize(), getSymbolSize(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D canvas = mountCanvas(result, unit);
		drawTroopTypeSymbol(unit.getModel().getType(), canvas);
		drawCompanyTraitsSymbols(unit, canvas, symbolCenter);
		drawFormationLevel(unit, canvas);
		drawExperience(unit, canvas);
		drawStrengthLevel(unit, canvas);
		drawFlag(unit, canvas);
		drawCorpsStrip(unit, canvas);
		drawUnitName(unit, canvas);
		return result;
	}

	private void drawUnitName(Unit unit, Graphics2D canvas) {
		canvas.setFont(smallFont);
		int textWidth = textWidth(canvas, unit.getFullShortName());
		canvas.setColor(Color.BLACK);
		canvas.drawString(unit.getFullShortName(), getSize(0.5d) - textWidth / 2, getSize(0.93d));
	}

	private void drawCorpsStrip(Unit unit, Graphics2D canvas) {
		Formation corps = unit.getParentFormation(FormationLevel.CORPS);
		if (unit.getFormationLevel() == FormationLevel.CORPS) {
			corps = (Formation) unit;
		}
		if (corps != null) {
			canvas.setColor(_corpsColors.get(corps.getId()));
			canvas.fillRect(getSize(0.05), getSize(0.80), getSize(0.90), getSize(0.15));
		}
	}

	private void drawFlag(Unit unit, Graphics2D canvas) {
		if (unit.getParentFormation(FormationLevel.DIVISION) != null) {
			drawFormationSymbol(unit.getParentFormation(FormationLevel.DIVISION), canvas);
		} else if (unit.getParentFormation(FormationLevel.CORPS) != null) {
			drawFormationSymbol(unit.getParentFormation(FormationLevel.CORPS), canvas);
		} else {
			drawCountryFlag(unit, canvas);
		}
	}

	private void drawFormationSymbol(Unit unit, Graphics2D canvas) {
		int size = getSize(0.28);
		int x = getSize(0.025);
		int y = getSize(0.025);
		canvas.fillRect(x, y, size, size);
		BufferedImage flag = scenarioService.getFormationFlag((Formation) unit);
		canvas.drawImage(flag.getScaledInstance(size, size, Image.SCALE_SMOOTH), x, y, new Color(0, 0, 0), null);
	}

	private void drawCountryFlag(Building building, Graphics2D canvas, Position position, double dSize, double dX, double dY) {
		int size = getSize(dSize);
		int x = getSize(dX);
		int y = getSize(dY);
		int border = getSize(0.05);
		ObjectiveType objectiveType = gameService.getObjectiveType(building.getLocation(), position);
		if (objectiveType == null) {
			canvas.setColor(Color.WHITE);
		} else if (objectiveType == ObjectiveType.MAIN) {
			canvas.setColor(Color.YELLOW);
		} else {
			canvas.setColor(Color.GREEN);
		}
		canvas.fillRect(x - border, y - border, size + 2 * border, size + 2 * border);
		BufferedImage flag = scenarioService.getCountryFlag(building.getController());
		canvas.drawImage(flag.getScaledInstance(size, size, Image.SCALE_SMOOTH), x, y, new Color(0, 0, 0), null);
	}

	private void drawCountryFlag(Unit unit, Graphics2D canvas) {
		int size = getSize(0.28);
		int x = getSize(0.025);
		int y = getSize(0.025);
		canvas.fillRect(x, y, size, size);
		BufferedImage flag = scenarioService.getCountryFlag(unit.getCountry());
		canvas.drawImage(flag.getScaledInstance(size, size, Image.SCALE_SMOOTH), x, y, new Color(0, 0, 0), null);
	}

	private void drawFormationStrength(Formation unit, Graphics2D canvas) {
		canvas.setColor(Color.BLACK);
		canvas.fillRect(getSize(0.04), getSize(0.45), getSize(0.22), getSize(0.21));
		canvas.setColor(Color.WHITE);
		canvas.setFont(boldFont);
		String size = "" + unit.getTogetherCompanies().size();
		int textWidth = textWidth(canvas, size);
		canvas.drawString(size, getSize(0.16) - textWidth / 2, getSize(0.64) - 2);

	}

	private void drawStrengthLevel(Company unit, Graphics2D canvas) {
		double inX = 0.05;
		double width = 0.15;
		if (unit.getStrength() > 0) {
			canvas.setColor(Color.RED);
			canvas.fillRect(getSize(inX), getSize(0.65), getSize(width), getSize(0.1));
		}
		if (unit.getStrength() > 0.25) {
			canvas.setColor(new Color(255, 69, 0));
			canvas.fillRect(getSize(inX), getSize(0.55), getSize(width), getSize(0.1));
		}
		if (unit.getStrength() > 0.50) {
			canvas.setColor(Color.YELLOW);
			canvas.fillRect(getSize(inX), getSize(0.45), getSize(width), getSize(0.1));
		}
		if (unit.getStrength() > 0.75) {
			canvas.setColor(Color.GREEN);
			canvas.fillRect(getSize(inX), getSize(0.35), getSize(width), getSize(0.1));
		}
		canvas.setColor(Color.BLACK);
		canvas.drawRect(getSize(inX), getSize(0.35), getSize(width), getSize(0.4));
	}

	private void drawFormationLevel(Unit unit, Graphics2D canvas) {
		canvas.setFont(smallBoldFont);
		canvas.setColor(Color.BLACK);
		String level = unit.getFormationLevel().getCode();
		int textWidth = textWidth(canvas, level);
		canvas.drawString(level, (int) symbolCenter.getX() - textWidth / 2, (int) symbolUL.getY() - 2);
	}

	private void drawSupplyLimit(Formation formation, Graphics2D canvas) {
		//TODO supply range?
	}

	private void drawExperience(Company company, Graphics2D canvas) {
		canvas.setFont(xpFont);
		canvas.setColor(Color.BLACK);
		String str = NumberFormats.SHORT_XP.format(company.getExperience());
		int textWidth = textWidth(canvas, str);
		canvas.drawString(str, getSize(1.00) - textWidth, getSize(0.16));
	}

	private void drawTroopTypeSymbol(TroopType type, Graphics2D canvas) {
		canvas.setColor(Color.BLACK);
		int iX = getSize(0.30d);
		int iY = getSize(0.35d);
		int fX = iX + getSize(0.6d);
		int fY = iY + getSize(0.4d);
		symbolUL = new Point(iX, iY);
		symbolCenter = new Point((iX + fX) / 2, (iY + fY) / 2);

		canvas.setStroke(new BasicStroke(symbolSize / 25, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
		GeneralPath path = new GeneralPath();
		path.moveTo(iX, fY);
		path.lineTo(iX, iY);
		path.lineTo(fX, iY);
		path.lineTo(fX, fY);
		path.lineTo(iX, fY);
		switch (type) {
			case CAVALRY:
				path.lineTo(fX, iY);
			break;
			case INFANTRY:
				path.lineTo(fX, iY);
				path.lineTo(iX, iY);
				path.lineTo(fX, fY);
			break;
			case ARTILLERY:
				canvas.fillOval((int) symbolCenter.getX() - getSize(0.1), (int) symbolCenter.getY() - getSize(0.1), getSize(0.2), getSize(0.2));
			break;
			default:
			break;
		}
		canvas.draw(path);
	}

	private void drawCompanyTraitsSymbols(Company company, Graphics2D canvas, Point symbolCenter) {
		canvas.setFont(boldFont);
		if (company.getTraits().contains(CompanyTrait.HQ)) {
			int textWidth = textWidth(canvas, "HQ");
			canvas.setColor(Color.WHITE);
			canvas.drawString("HQ", (int) symbolCenter.getX() - textWidth / 2, (int) symbolCenter.getY() + boldFont.getSize() / 2);
		}
		canvas.setFont(smallFont);
	}

	private Graphics2D mountCanvas(BufferedImage target, Unit unit) {
		Graphics2D canvas = (Graphics2D) target.getGraphics();
		canvas.setColor(unit.getCountry().getColor());
		canvas.setFont(smallFont);
		canvas.fillRect(0, 0, target.getWidth(), target.getHeight());
		return canvas;
	}

	private void computeSizes() {
		smallFont = new Font("Arial Narrow", Font.BOLD, (int) (Math.pow(symbolSize, 0.58)));
		boldFont = new Font("Verdana", Font.BOLD, symbolSize / 6);
		xpFont = new Font("Courier", Font.BOLD, symbolSize / 6);
	}

	private int getSize(double ratio) {
		return (int) (Math.ceil(ratio * getSymbolSize()));
	}

	private int getSize(double ratio, double widthRatio) {
		return (int) (Math.ceil(ratio * getSymbolSize() * widthRatio));
	}

	private int textWidth(Graphics2D graphics, String text) {
		FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
		return metrics.stringWidth(text);
	}

	/* getters & setters */

	public int getSymbolSize() {
		return symbolSize;
	}

	public void setSymbolSize(int symbolSize) {
		this.symbolSize = symbolSize;
		computeSizes();
	}

}

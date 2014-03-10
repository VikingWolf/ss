package org.orion.ss.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.CompanyTrait;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.utils.NumberFormats;

public class GraphService extends Service {

	private final static List<Color> _CorpsColors = new ArrayList<Color>();

	static {
		_CorpsColors.add(Color.CYAN);
		_CorpsColors.add(Color.RED);
		_CorpsColors.add(Color.GREEN);
		_CorpsColors.add(Color.BLUE);
		_CorpsColors.add(Color.LIGHT_GRAY);
		_CorpsColors.add(Color.YELLOW);
		_CorpsColors.add(Color.WHITE);
		_CorpsColors.add(Color.ORANGE);
		_CorpsColors.add(Color.BLACK);
		_CorpsColors.add(Color.DARK_GRAY);
		_CorpsColors.add(Color.MAGENTA);
		_CorpsColors.add(Color.PINK);
		_CorpsColors.add(Color.GRAY);
		_CorpsColors.add(new Color(150, 0, 0));
		_CorpsColors.add(new Color(0, 150, 0));
		_CorpsColors.add(new Color(0, 0, 150));
		_CorpsColors.add(new Color(150, 0, 150));
		_CorpsColors.add(new Color(0, 150, 150));
		_CorpsColors.add(new Color(150, 0, 0));
		// TODO mas colores
		_CorpsColors.add(Color.CYAN);
		_CorpsColors.add(Color.RED);
		_CorpsColors.add(Color.GREEN);
		_CorpsColors.add(Color.BLUE);
		_CorpsColors.add(Color.LIGHT_GRAY);
		_CorpsColors.add(Color.YELLOW);
		_CorpsColors.add(Color.WHITE);
		_CorpsColors.add(Color.ORANGE);
		_CorpsColors.add(Color.BLACK);
		_CorpsColors.add(Color.DARK_GRAY);
		_CorpsColors.add(Color.MAGENTA);
		_CorpsColors.add(Color.PINK);
		_CorpsColors.add(Color.GRAY);
		_CorpsColors.add(new Color(150, 0, 0));
		_CorpsColors.add(new Color(0, 150, 0));
		_CorpsColors.add(new Color(0, 0, 150));
		_CorpsColors.add(new Color(150, 0, 150));
		_CorpsColors.add(new Color(0, 150, 150));
		_CorpsColors.add(new Color(150, 0, 0));
	}

	private final ScenarioService scenarioService;

	private int symbolSize;
	private Font smallFont;
	private Font smallBoldFont;
	private Font boldFont;
	private Font xpFont;

	private Point symbolCenter;
	private Point symbolUL;

	public GraphService(Game game) {
		super(game);
		scenarioService = new ScenarioService(game);
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

	private void drawUnitName(Unit unit, Graphics2D target) {
		target.setFont(smallFont);
		int textWidth = textWidth(target, unit.getFullShortName());
		target.setColor(Color.BLACK);
		target.drawString(unit.getFullShortName(), getSize(0.5d) - textWidth / 2, getSize(0.93d));
	}

	private void drawCorpsStrip(Unit unit, Graphics2D target) {
		Formation corps = unit.getParentFormation(FormationLevel.CORPS);
		if (unit.getFormationLevel() == FormationLevel.CORPS) {
			corps = (Formation) unit;
		}
		if (corps != null) {
			target.setColor(_CorpsColors.get(corps.getId()));
			target.fillRect(getSize(0.05), getSize(0.80), getSize(0.90), getSize(0.15));
		}
	}

	private void drawFlag(Unit unit, Graphics2D target) {
		logger.error("unit=" + unit.getFullLongName() + unit.getParentFormation(FormationLevel.CORPS));
		if (unit.getParentFormation(FormationLevel.DIVISION) != null) {
			drawFormationSymbol(unit.getParentFormation(FormationLevel.DIVISION), target);
		} else if (unit.getParentFormation(FormationLevel.CORPS) != null) {
			drawFormationSymbol(unit.getParentFormation(FormationLevel.CORPS), target);
		} else {
			drawCountryFlag(unit, target);
		}
	}

	private void drawFormationSymbol(Unit unit, Graphics2D target) {
		int size = getSize(0.28);
		int x = getSize(0.025);
		int y = getSize(0.025);
		target.fillRect(x, y, size, size);
		BufferedImage flag = scenarioService.getFormationFlag((Formation) unit);
		target.drawImage(flag.getScaledInstance(size, size, Image.SCALE_SMOOTH), x, y, new Color(0, 0, 0), null);
	}

	private void drawCountryFlag(Unit unit, Graphics2D target) {
		int size = getSize(0.28);
		int x = getSize(0.025);
		int y = getSize(0.025);
		target.fillRect(x, y, size, size);
		BufferedImage flag = scenarioService.getCountryFlag(unit.getCountry());
		target.drawImage(flag.getScaledInstance(size, size, Image.SCALE_SMOOTH), x, y, new Color(0, 0, 0), null);
	}

	private void drawFormationStrength(Formation unit, Graphics2D target) {
		target.setColor(Color.BLACK);
		target.fillRect(getSize(0.04), getSize(0.45), getSize(0.22), getSize(0.21));
		target.setColor(Color.WHITE);
		target.setFont(boldFont);
		String size = "" + unit.getTogetherCompanies().size();
		int textWidth = textWidth(target, size);
		target.drawString(size, getSize(0.16) - textWidth / 2, getSize(0.64) - 2);

	}

	private void drawStrengthLevel(Company unit, Graphics2D target) {
		double inX = 0.05;
		double width = 0.15;
		if (unit.getStrength() > 0) {
			target.setColor(Color.RED);
			target.fillRect(getSize(inX), getSize(0.65), getSize(width), getSize(0.1));
		}
		if (unit.getStrength() > 0.25) {
			target.setColor(new Color(255, 69, 0));
			target.fillRect(getSize(inX), getSize(0.55), getSize(width), getSize(0.1));
		}
		if (unit.getStrength() > 0.50) {
			target.setColor(Color.YELLOW);
			target.fillRect(getSize(inX), getSize(0.45), getSize(width), getSize(0.1));
		}
		if (unit.getStrength() > 0.75) {
			target.setColor(Color.GREEN);
			target.fillRect(getSize(inX), getSize(0.35), getSize(width), getSize(0.1));
		}
		target.setColor(Color.BLACK);
		target.drawRect(getSize(inX), getSize(0.35), getSize(width), getSize(0.4));
	}

	private void drawFormationLevel(Unit unit, Graphics2D target) {
		target.setFont(smallBoldFont);
		target.setColor(Color.BLACK);
		String level = unit.getFormationLevel().getCode();
		int textWidth = textWidth(target, level);
		target.drawString(level, (int) symbolCenter.getX() - textWidth / 2, (int) symbolUL.getY() - 2);
	}

	private void drawSupplyLimit(Formation formation, Graphics2D target) {
		target.setFont(smallBoldFont);
		target.setColor(Color.BLACK);
		String str = formation.getAllCompanies().size() + " / " + formation.getFormationLevel().getSupplyLimit();
		int textWidth = textWidth(target, str);
		target.drawString(str, getSize(1.00) - textWidth, getSize(0.16));
	}

	private void drawExperience(Company company, Graphics2D target) {
		target.setFont(xpFont);
		target.setColor(Color.BLACK);
		String str = NumberFormats.SHORT_XP.format(company.getExperience());
		int textWidth = textWidth(target, str);
		target.drawString(str, getSize(1.00) - textWidth, getSize(0.16));
	}

	private void drawTroopTypeSymbol(TroopType type, Graphics2D target) {
		target.setColor(Color.BLACK);
		int iX = getSize(0.30d);
		int iY = getSize(0.35d);
		int fX = iX + getSize(0.6d);
		int fY = iY + getSize(0.4d);
		symbolUL = new Point(iX, iY);
		symbolCenter = new Point((iX + fX) / 2, (iY + fY) / 2);

		target.drawLine(iX, iY, fX, iY);
		target.drawLine(fX, iY, fX, fY);
		target.drawLine(fX, fY, iX, fY);
		target.drawLine(iX, fY, iX, iY);
		switch (type) {
			case CAVALRY:
				target.drawLine(iX, fY, fX, iY);
			break;
			case INFANTRY:
				target.drawLine(iX, fY, fX, iY);
				target.drawLine(iX, iY, fX, fY);
			break;
			case ARTILLERY:
				target.fillOval((int) symbolCenter.getX() - getSize(0.1), (int) symbolCenter.getY() - getSize(0.1), getSize(0.2), getSize(0.2));
			break;
			default:
			break;
		}
	}

	private void drawCompanyTraitsSymbols(Company company, Graphics2D target, Point symbolCenter) {
		target.setFont(boldFont);
		if (company.getTraits().contains(CompanyTrait.HQ)) {
			int textWidth = textWidth(target, "HQ");
			target.setColor(Color.WHITE);
			target.drawString("HQ", (int) symbolCenter.getX() - textWidth / 2, (int) symbolCenter.getY() + boldFont.getSize() / 2);
		}
		target.setFont(smallFont);
	}

	private Graphics2D mountCanvas(BufferedImage target, Unit unit) {
		Graphics2D canvas = (Graphics2D) target.getGraphics();
		canvas.setColor(unit.getCountry().getColor());
		canvas.setFont(smallFont);
		canvas.fillRect(0, 0, target.getWidth(), target.getHeight());
		return canvas;
	}

	private void computeSizes() {
		smallFont = new Font("Arial", Font.PLAIN, symbolSize / 6);
		smallBoldFont = new Font("Arial", Font.BOLD, symbolSize / 6);
		boldFont = new Font("Verdana", Font.BOLD, symbolSize / 6);
		xpFont = new Font("Courier", Font.BOLD, symbolSize / 6);
	}

	private int getSize(double ratio) {
		return (int) (Math.ceil(ratio * getSymbolSize()));
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

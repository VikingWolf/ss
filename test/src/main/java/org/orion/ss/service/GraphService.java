package org.orion.ss.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
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
	}

	private int symbolSize;
	private Font smallFont;
	private Font smallBoldFont;
	private Font boldFont;
	private Font xpFont;

	private Point symbolCenter;
	private Point symbolUL;

	public GraphService(Game game) {
		super(game);
	}

	public BufferedImage getUnitSymbol(Company unit) {
		BufferedImage result = new BufferedImage(getSymbolSize(), getSymbolSize(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D canvas = mountCanvas(result, unit);
		if (unit instanceof Company) {
			drawTroopTypeSymbol(unit.getModel().getType(), canvas);
			drawCompanyTraitsSymbols(unit, canvas, symbolCenter);
		}
		drawFormationLevel(unit, canvas);
		drawStrengthLevel(unit, canvas);
		drawDivisionSymbol(unit, canvas);
		drawCorpsStrip(unit, canvas);
		canvas.setFont(smallFont);
		int textWidth = textWidth(canvas, unit.getFullShortName());
		canvas.setColor(Color.BLACK);
		canvas.drawString(unit.getFullShortName(), getSize(0.5d) - textWidth / 2, getSize(0.93d));
		return result;
	}

	private void drawCorpsStrip(Company unit, Graphics2D target) {
		Formation corps = unit.getParentFormation(FormationLevel.CORPS);
		if (corps != null) {
			target.setColor(_CorpsColors.get(corps.getId()));
			target.fillRect(getSize(0.05), getSize(0.80), getSize(0.90), getSize(0.15));
		}
	}

	private void drawDivisionSymbol(Company unit, Graphics2D target) {
		target.setColor(Color.WHITE);
		target.fillRect(getSize(0.025), getSize(0.025), getSize(0.28), getSize(0.28));
		// TODO symbol of the division
	}

	private void drawStrengthLevel(Company unit, Graphics2D target) {
		double inX = 0.05;
		double width = 0.15;
		if (unit.getStrength() > 0) {
			target.setColor(Color.RED);
			target.fillRect(getSize(inX), getSize(0.65), getSize(width), getSize(0.1));
		}
		if (unit.getStrength() > 0.25) {
			target.setColor(Color.ORANGE);
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

	private void drawFormationLevel(Company unit, Graphics2D target) {
		target.setFont(smallBoldFont);
		String level = "";
		if (unit.getTraits().contains(CompanyTrait.HQ)) {
			level = unit.getParent().getFormationLevel().getCode();
		} else {
			level = unit.getFormationLevel().getCode();
		}
		drawExperience(unit, target);
		target.setColor(Color.BLACK);
		int textWidth = textWidth(target, level);
		target.drawString(level, (int) symbolCenter.getX() - textWidth / 2, (int) symbolUL.getY() - 2);
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

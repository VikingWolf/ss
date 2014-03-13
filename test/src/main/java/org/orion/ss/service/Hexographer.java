package org.orion.ss.service;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.GeneralPath;

import org.orion.ss.model.geo.HexSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hexographer {

	protected final static Logger logger = LoggerFactory.getLogger("Hexographer.class");

	private final static double offset = -Math.PI / 6;

	private int columnDistance;
	private int rowDistance;
	private final double radius;

	public Hexographer(double radius) {
		super();
		this.radius = radius;
		calculateDistances();
	}

	private void calculateDistances() {
		Polygon hex = getHex(new Point(0, 0));
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (int i = 0; i < hex.xpoints.length; i++) {
			if (hex.xpoints[i] < minX) minX = hex.xpoints[i];
			if (hex.xpoints[i] > maxX) maxX = hex.xpoints[i];
			if (hex.ypoints[i] < minX) minY = hex.ypoints[i];
			if (hex.ypoints[i] > maxX) maxY = hex.ypoints[i];
		}
		columnDistance = (int) (Math.abs(maxX - minX) * Math.pow(Math.cos(Math.PI / 6), 2));
		rowDistance = Math.abs(maxY - minY) + 1;
	}

	public Polygon getHex(Point origin) {
		Polygon result = new Polygon();
		for (int i = 0; i < 6; i++) {
			int xC = (int) (Math.sin(offset + Math.PI / 3 * i) * radius + origin.getX());
			int yC = (int) (Math.cos(offset + Math.PI / 3 * i) * radius + origin.getY());
			result.addPoint(xC, yC);
		}
		return result;
	}

	public int getColumnDistance() {
		return columnDistance;
	}

	public void setColumnDistance(int columnDistance) {
		this.columnDistance = columnDistance;
	}

	public int getRowDistance() {
		return rowDistance;
	}

	public void setRowDistance(int rowDistance) {
		this.rowDistance = rowDistance;
	}

	public void addSide(GeneralPath path, Point origin, HexSide side) {
		double xC = Math.ceil(Math.sin(offset + Math.PI / 3 * side.getIndex()) * radius + origin.getX());
		double yC = Math.ceil(Math.cos(offset + Math.PI / 3 * side.getIndex()) * radius + origin.getY());
		if (!path.contains(xC, yC)) {
			path.moveTo(xC, yC);
		}
		xC = Math.ceil(Math.sin(offset + Math.PI / 3 * (side.getIndex() - 1) % 6) * radius + origin.getX());
		yC = Math.ceil(Math.cos(offset + Math.PI / 3 * (side.getIndex() - 1) % 6) * radius + origin.getY());
		path.lineTo(xC, yC);
	}

	public Polygon getSide(Point origin, HexSide side) {
		Polygon result = new Polygon();
		double xC = Math.sin(offset + Math.PI / 3 * side.getIndex()) * radius + origin.getX();
		double yC = Math.cos(offset + Math.PI / 3 * side.getIndex()) * radius + origin.getY();
		result.addPoint((int) xC, (int) yC);
		xC = Math.sin(offset + Math.PI / 3 * (side.getIndex() - 1) % 6) * radius + origin.getX();
		yC = Math.cos(offset + Math.PI / 3 * (side.getIndex() - 1) % 6) * radius + origin.getY();
		result.addPoint((int) xC, (int) yC);
		return result;
	}

	public Polygon getRadius(Point origin, HexSide side) {
		logger.error("side=" + side.getIndex());
		Polygon result = new Polygon();
		result.addPoint((int) origin.getX(), (int) origin.getY());
		double xC = Math.sin(Math.PI / 3 * (side.getIndex() - 1)) * radius + origin.getX();
		double yC = Math.cos(Math.PI / 3 * (side.getIndex() - 1)) * radius + origin.getY();
		result.addPoint((int) Math.ceil(xC), (int) Math.ceil(yC));
		return result;

	}
}

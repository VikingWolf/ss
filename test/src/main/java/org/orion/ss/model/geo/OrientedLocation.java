package org.orion.ss.model.geo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrientedLocation extends Location {

	protected final static Logger logger = LoggerFactory.getLogger(OrientedLocation.class);

	private HexSide side;

	public OrientedLocation() {
		super();
	}

	public OrientedLocation(int x, int y, HexSide side) {
		super(x, y);
		this.side = side;
	}

	public HexSide getSide() {
		return side;
	}

	public void setSide(HexSide side) {
		this.side = side;
	}

	public OrientedLocation getComplementary() {
		OrientedLocation adjacent = new OrientedLocation();
		adjacent.setLocation(side.getAdjacent(this));
		adjacent.setSide(side.complementary());
		return adjacent;
	}

	@Override
	public String toString() {
		return "OrientedLocation [side=" + side + ", getX()=" + getX() + ", getY()=" + getY() + "]";
	}

}

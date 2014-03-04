package org.orion.ss.model.geo;

import java.awt.Point;
import java.io.Serializable;


public class HexSidedPoint extends Point implements Serializable {

	private static final long serialVersionUID = 457363877407482615L;

	private HexSide side;

	public HexSidedPoint(){
		super();
	}
	
	public HexSidedPoint(Point loc){
		super();
		this.setLocation(loc);
	}
	
	public HexSide getSide() {
		return side;
	}

	public void setSide(HexSide side) {
		this.side = side;
	}
	
	public HexSidedPoint getComplementary(){
		HexSidedPoint adjacent = new HexSidedPoint();
		adjacent.setLocation(side.getAdjacent(this));
		adjacent.setSide(side.complementary());
		return adjacent;
	}

}

package org.orion.ss.model.geo;

import java.awt.Point;

public enum HexSide {

	/*			code	index	adjX	adjYeven	adjYodd */
	SOUTHWEST(	"SW", 	0,		-1,		+1,			0),	
	
	SOUTH(		"S", 	1,		+0,		+1,			+1),
	SOUTHEAST(	"SE", 	2,		+1,		+1,			0),			
	NORTHEAST(	"NE", 	3,		+1,		+0,			+1),
	NORTH(		"N", 	4,		+0,		-1,			-1),		
	
	NORTHWEST(	"NW", 	5,		-1,		+0,			-1);

	
	private final String code;
	private int adjX;
	private int index;
	private int adjYeven;
	private int adjYodd;
	
	private HexSide(String code, int index, int adjX, int adjYeven, int adjYodd){
		this.code = code;
		this.index = index;
		this.adjX = adjX;
		this.adjYeven = adjYeven;
		this.adjYodd = adjYodd;
	}

	public String getCode() {
		return code;
	}

	public Point getAdjacent(Point point){
		return getAdjacent((int)point.getX(), (int)point.getY());
	}
	
	public Point getAdjacent(int x, int y){
		int xResult = x + adjX;
		int yResult = y;

		if (x%2 == 0){
			yResult += this.adjYeven;
		} else{
			yResult += this.adjYodd;			
		}
		return new Point(xResult, yResult);
	}
	
	public int getIndex() {
		return index;
	}

	public HexSide complementary(){	
		return HexSide.values()[(this.index+3)%6];
	}
	
}

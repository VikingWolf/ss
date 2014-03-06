package org.orion.ss.model.geo;


public enum HexSide {

	/* code 		index 		adjX 	adjYeven adjYodd */
	SOUTHWEST("SW", 	0, 		-1, 	+1, 	0),
	SOUTH("S", 			1, 		+0, 	+1, 	+1),
	SOUTHEAST("SE", 	2, 		+1, 	+1, 	0),
	NORTHEAST("NE", 	3, 		+1, 	+0, 	-1),
	NORTH("N", 			4, 		+0, 	-1, 	-1),
	NORTHWEST("NW", 	5, 		-1, 	+0, 	-1);

	private final String code;
	private int adjX;
	private int index;
	private int adjYeven;
	private int adjYodd;

	private HexSide(String code, int index, int adjX, int adjYeven, int adjYodd) {
		this.code = code;
		this.index = index;
		this.adjX = adjX;
		this.adjYeven = adjYeven;
		this.adjYodd = adjYodd;
	}

	public String getCode() {
		return code;
	}

	public Location getAdjacent(Location location) {
		return getAdjacent(location.getX(), location.getY());
	}

	public Location getAdjacent(int x, int y) {
		int xResult = x + adjX;
		int yResult = y;

		if (x % 2 == 0) {
			yResult += adjYeven;
		} else {
			yResult += adjYodd;
		}
		return new Location(xResult, yResult);
	}

	public int getIndex() {
		return index;
	}

	public HexSide complementary() {
		return HexSide.values()[(index + 3) % 6];
	}

}

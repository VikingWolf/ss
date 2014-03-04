package org.orion.ss.model.geo;


public class River extends OrientedFeature {

	private int deep;

	/* getters & setters */
	public int getDeep() {
		return deep;
	}

	public void setDeep(int deep) {
		this.deep = deep;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		River other = (River) obj;
		if (getLoc() == null) {
			if (other.getLoc() != null)
				return false;
		} else if (!getLoc().equals(other.getLoc()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "River [loc=" + getLoc() + ", deep=" + deep + "]";
	}

	@Override
	public TerrainFeatureType getType() { return TerrainFeatureType.RIVER; }

}

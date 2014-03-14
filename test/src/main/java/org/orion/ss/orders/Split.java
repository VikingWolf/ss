package org.orion.ss.orders;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.OrderTime;

public class Split extends Order {

	private List<Unit> toDetach;

	public Split() {
		super();
		toDetach = new ArrayList<Unit>();
	}

	@Override
	public String getDenomination() {
		return "Split";

	}

	@Override
	public OrderTime getOrderTime() {
		return OrderTime.AUTOMATIC;
	}

	/* adders */
	public void addToDetach(Unit unit) {
		toDetach.add(unit);
	}

	public void removeToDetach(Unit unit) {
		toDetach.remove(unit);
	}

	/* getters & setters */

	public List<Unit> getToDetach() {
		return toDetach;
	}

	public void setToDetach(List<Unit> toDetach) {
		this.toDetach = toDetach;
	}

}

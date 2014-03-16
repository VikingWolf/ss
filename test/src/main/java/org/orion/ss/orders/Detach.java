package org.orion.ss.orders;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.OrderTime;

public class Detach extends Order {

	private List<Unit> toDetach;

	public Detach() {
		super();
		toDetach = new ArrayList<Unit>();
	}

	public static String getDescription() {
		return "Detaching units will permit to give orders separately from the parents unit.";
	}

	@Override
	public String getDenomination() {
		return "Detach";

	}

	@Override
	public OrderTime getOrderTime() {
		return OrderTime.NONE;
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

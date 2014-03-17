package org.orion.ss.orders;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.OrderTime;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;

public class Detach extends Order<Formation> {

	public final static Class<Formation> EXECUTOR_CLASS = Formation.class;

	private List<Unit> toDetach;

	public Detach(Formation executor, Game game) {
		super(executor, game);
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

	@Override
	public boolean checkRequirements() {
		return getExecutor().canBeSplit();
	}

	@Override
	public String execute() {
		String result = "";
		for (Unit unit : getToDetach()) {
			unit.setDetached(true);
			result += unit.getFullLongName() + ", ";
		}
		if (result.length() > 2)
			result = result.substring(0, result.length() - 2);
		result += " have been detached. ";
		return result;
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

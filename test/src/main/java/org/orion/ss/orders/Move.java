package org.orion.ss.orders;

import java.util.ArrayList;
import java.util.List;

import org.orion.ss.model.Mobile;
import org.orion.ss.model.core.OrderTime;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.impl.Game;
import org.orion.ss.service.MovementService;
import org.orion.ss.service.ServiceFactory;

public class Move extends Order<Mobile> {

	public final static Class<Mobile> EXECUTOR_CLASS = Mobile.class;

	private final List<Location> movementPath;

	public Move(Mobile executor, Game game) {
		super(executor, game);
		movementPath = new ArrayList<Location>();
	}

	public static String getDescription() {
		return "Moves the unit, selecting the move path on map. Movement cannot be undone once executed.";
	}

	@Override
	public String getDenomination() {
		return "Move";
	}

	@Override
	public OrderTime getOrderTime() {
		return OrderTime.VARIABLE;
	}

	@Override
	public boolean checkRequirements() {
		MovementService movementService = ServiceFactory.getMovementService(getGame());
		HexSet area = movementService.getMoveArea(null, getExecutor());
		if (area.size() > 0)
			return true;
		else return false;
	}

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return null;
	}

	/* getters & setters */

	public void addToMovementPath(Location location) {
		movementPath.add(location);
	}

	public List<Location> getMovementPath() {
		return movementPath;
	}

}

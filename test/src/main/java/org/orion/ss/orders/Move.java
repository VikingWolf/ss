package org.orion.ss.orders;

import org.orion.ss.model.Mobile;
import org.orion.ss.model.core.OrderTime;
import org.orion.ss.model.geo.HexSet;
import org.orion.ss.model.impl.Game;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.ServiceFactory;

public class Move extends Order<Mobile> {

	public final static Class<Mobile> EXECUTOR_CLASS = Mobile.class;

	private final GeoService geoService;

	public Move(Mobile executor, Game game) {
		super(executor, game);
		geoService = ServiceFactory.getGeoService(game);
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
		HexSet area = geoService.getMoveArea(getExecutor());
		logger.error("executor=" + this.getExecutor() + ", area=" + area.size());
		if (area.size() > 0)
			return true;
		else return false;
	}

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return null;
	}

}

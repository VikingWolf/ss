package org.orion.ss.orders;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.OrderTime;
import org.orion.ss.model.impl.Game;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.ServiceFactory;

public class Garrison extends Order<Unit> {

	public final static Class<Unit> EXECUTOR_CLASS = Unit.class;

	private final GeoService geoService;

	public Garrison(Unit executor, Game game) {
		super(executor, game);
		geoService = ServiceFactory.getGeoService(game);
	}

	public static String getDescription() {
		return "Garrison will station the unit at the fortifications, improving greatly his defense. ";
	}

	@Override
	public String getDenomination() {
		return "Garrison";
	}

	@Override
	public OrderTime getOrderTime() {
		return OrderTime.NONE;
	}

	@Override
	public boolean checkRequirements() {
		return !getExecutor().isGarrison() && geoService.canGarrison(getExecutor());
	}

	@Override
	public String execute() {
		getExecutor().setGarrison(true);
		return getExecutor().getFullLongName() + " is now garrison at " + getExecutor().getLocation() + " fortifications.";
	}

}

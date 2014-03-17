package org.orion.ss.orders;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.OrderTime;
import org.orion.ss.model.impl.Game;
import org.orion.ss.service.GeoService;
import org.orion.ss.service.ServiceFactory;

public class AutoSupply extends Order<Unit> {

	public final static Class<Unit> EXECUTOR_CLASS = Unit.class;

	public AutoSupply(Unit executor, Game game) {
		super(executor, game);
	}

	public static String getDescription() {
		return "If the auto supply feature is enabled, the unit will be re-supplied automatically from nearest available supply source.";
	}

	@Override
	public String getDenomination() {
		return "Auto-Supply";
	}

	@Override
	public OrderTime getOrderTime() {
		return OrderTime.NONE;
	}

	@Override
	public boolean checkRequirements() {
		GeoService geoService = ServiceFactory.getGeoService(getGame());
		return geoService.getNearestSupplySource(getExecutor()) != null;
	}

	@Override
	public String execute() {
		getExecutor().setAutoSupply(!getExecutor().isAutoSupply());
		return getExecutor().getFullLongName() + " auto-supply set to " + getExecutor().isAutoSupply();
	}

}

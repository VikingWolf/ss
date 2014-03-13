package org.orion.ss.service;

import java.util.List;

import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.MultiLocatedFeature;
import org.orion.ss.model.geo.OrientedLocation;
import org.orion.ss.model.impl.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextService extends Service {

	protected final static Logger logger = LoggerFactory.getLogger(TextService.class);

	protected TextService(Game game) {
		super(game);
	}

	public <T extends MultiLocatedFeature> String getStringSides(List<T> list, Location coords) {
		String result = "";
		for (T item : list) {
			for (OrientedLocation location : item.getLocations()) {
				if (location.getX() == coords.getX() && location.getY() == coords.getY()) {
					result += location.getSide().getCode() + ", ";
				}
			}
		}
		if (result.length() > 2) {
			result = result.substring(0, result.length() - 2);
		}
		return result;
	}

}

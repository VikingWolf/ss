package org.orion.ss.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Scenario;

public class ServiceFactory {

	protected final static Map<Game, List<Service>> services = new HashMap<Game, List<Service>>();

	public static GameService getGameService(Game game) {
		return findService(GameService.class, game);
	}

	public static GameService getGameService(Game game, Scenario scenario) {
		GameService result = findService(GameService.class, game);
		if (result == null) {
			result = new GameService(game, scenario);
			addService(result);
		}
		return result;
	}

	public static GeoService getGeoService(Game game) {
		GeoService result = findService(GeoService.class, game);
		if (result == null) {
			result = new GeoService(game);
			addService(result);
		}
		return result;
	}

	public static ManagementService getManagementService(Game game) {
		ManagementService result = findService(ManagementService.class, game);
		if (result == null) {
			result = new ManagementService(game);
			addService(result);
		}
		return result;
	}

	public static CombatService getCombatService(Game game) {
		CombatService result = findService(CombatService.class, game);
		if (result == null) {
			result = new CombatService(game);
			addService(result);
		}
		return result;
	}

	public static GraphService getGraphService(Game game) {
		GraphService result = findService(GraphService.class, game);
		if (result == null) {
			result = new GraphService(game);
			addService(result);
		}
		return result;
	}

	public static ScenarioService getScenarioService(Game game) {
		ScenarioService result = findService(ScenarioService.class, game);
		if (result == null) {
			result = new ScenarioService(game);
			addService(result);
		}
		return result;
	}

	public static TextService getTextService(Game game) {
		TextService result = findService(TextService.class, game);
		if (result == null) {
			result = new TextService(game);
			addService(result);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static <T extends Service> T findService(Class<T> clazz, Game game) {
		T result = null;
		if (services.containsKey(game)) {
			for (Service service : services.get(game)) {
				if (service.getClass().equals(clazz)) {
					result = (T) service;
					break;
				}
			}
		}
		return result;
	}

	private static void addService(Service service) {
		List<Service> list = null;
		if (services.containsKey(service.getGame())) {
			list = services.get(service.getGame());
		} else {
			list = new ArrayList<Service>();
		}
		list.add(service);
		services.put(service.getGame(), list);
	}

}

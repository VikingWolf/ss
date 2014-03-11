package org.orion.ss.model.geo;

import java.util.HashMap;

public class WeatherForecast extends HashMap<WeatherType, Double> {

	private static final long serialVersionUID = 6452495050391040150L;

	public WeatherType resolveWeather(double random) {
		WeatherType result = null;
		double acc = 0d;
		for (WeatherType weather : this.keySet()) {
			acc += this.get(weather);
			if (random <= acc) {
				result = weather;
				break;
			}
		}
		return result;
	}

}

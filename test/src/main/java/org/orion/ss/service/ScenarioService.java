package org.orion.ss.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.orion.ss.model.impl.Country;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;

public class ScenarioService extends Service {

	private final static String _scenarioPath = "src\\main\\resources\\scenarios\\";
	private final static String _flagsPath = "\\flags";

	protected ScenarioService(Game game) {
		super(game);
	}

	public BufferedImage getCountryFlag(Country country) {
		return loadImage(new File(_scenarioPath + getGame().getScenarioName() + _flagsPath + "\\" + country.getName().toLowerCase() + ".png"));
	}

	public BufferedImage getFormationFlag(Formation formation) {
		logger.error(_scenarioPath + getGame().getScenarioName() + _flagsPath + "\\" + formation.getFormationLevel().getDenomination().toLowerCase() + "_" + formation.getId() + ".png");
		return loadImage(new File(_scenarioPath + getGame().getScenarioName() + _flagsPath + "\\" + formation.getFormationLevel().getDenomination().toLowerCase() + "_" + formation.getId() + ".png"));
	}

	private BufferedImage loadImage(File source) {
		BufferedImage result = null;
		try {
			result = ImageIO.read(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}

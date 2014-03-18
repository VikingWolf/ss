package org.orion.ss.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Country;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;

public class ScenarioService extends Service {

	private final static String _scenarioPath = "src\\main\\resources\\scenarios\\";
	private final static String _flagsPath = "\\flags";
	private final static String _modelsPath = "\\models";

	protected ScenarioService(Game game) {
		super(game);
	}

	public BufferedImage getAirfieldSymbol() {
		return loadImage(new File(_scenarioPath + getGame().getScenarioName() + "\\airfield.png"));
	}

	public BufferedImage getCountryFlag(Country country) {
		return loadImage(new File(_scenarioPath + getGame().getScenarioName() + _flagsPath + "\\" + country.getName().toLowerCase() + ".png"));
	}

	public BufferedImage getFormationFlag(Formation formation) {
		return loadImage(new File(_scenarioPath + getGame().getScenarioName() + _flagsPath + "\\" + formation.getCountry().getName().toLowerCase() + "\\" + formation.getFormationLevel().getDenomination().toLowerCase() + "_" + formation.getId() + ".png"));
	}

	public BufferedImage getCompanyModelFigure(CompanyModel model) {
		String fileName = _scenarioPath + getGame().getScenarioName() + _modelsPath + "\\" + model.getCode().replace(" ", "") + ".png";
		logger.error("name=" + fileName);
		return loadImage(new File(fileName));
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

package org.orion.ss.test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.Mobility;
import org.orion.ss.model.core.PositionRole;
import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.core.WeaponType;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.Terrain;
import org.orion.ss.model.geo.Vegetation;
import org.orion.ss.model.geo.WeatherForecast;
import org.orion.ss.model.geo.WeatherType;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Country;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.GameSettings;
import org.orion.ss.model.impl.Market;
import org.orion.ss.model.impl.Player;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.Scenario;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.model.impl.WeaponModel;

public class GameSample2Players {

	private Game game;
	private Scenario scenario;

	private final Country uk = new Country("UK", 0.9d, Color.MAGENTA);
	private final Country ger = new Country("GER", 1.0d, Color.LIGHT_GRAY);
	private final Position gerInfReg1 = new Position(FormationLevel.REGIMENT, TroopType.INFANTRY, 1, PositionRole.ATTACKER);
	private final Position ukInfBg2 = new Position(FormationLevel.REGIMENT, TroopType.INFANTRY, 2, PositionRole.DEFENDER);

	private WeaponModel leeEnfieldMk1;
	private WeaponModel mauser98k;
	private WeaponModel vickersBerthierLMG;

	private CompanyModel ukRifleCompanyModel39;
	private CompanyModel gerGrenadierCompanyModel39;
	private CompanyModel ukRifleCompanyModel40;

	public Game buildGame() {
		buildCountries();
		buildWeaponModels();
		buildCompanyModels();
		configScenario();
		buildMap();
		mountForecast();
		buildGERPosition();
		buildUKPosition();
		configGame();
		mountCamps();
		return game;
	}

	protected void buildCountries() {
		Market ukMarket = new Market();
		ukMarket.put(SupplyType.AMMO, 800);
		ukMarket.put(SupplyType.FUEL, 950);
		uk.setMarket(ukMarket);
		Market gerMarket = new Market();
		gerMarket.put(SupplyType.AMMO, 800);
		gerMarket.put(SupplyType.FUEL, 1150);
		ger.setMarket(gerMarket);
	}

	protected void buildWeaponModels() {
		mauser98k = new WeaponModel("Mauser 98k", WeaponType.SMALL_ARM, 12, 0.86, 0.5d, 0.000012d, 1, 5);
		leeEnfieldMk1 = new WeaponModel("Lee-Enfield Mk 1", WeaponType.SMALL_ARM, 12, 0.853, 0.457, 0.000012d, 1, 6);
		vickersBerthierLMG = new WeaponModel("VB LMG", WeaponType.SMALL_ARM, 180, 0.745, 0.457, 0.000012d, 1, 11);
	}

	protected void buildCompanyModels() {
		ukRifleCompanyModel39 = new CompanyModel("Rifle Company 39", TroopType.INFANTRY, Mobility.FOOT, 4.5d, 3, 135, uk);
		ukRifleCompanyModel39.addWeaponry(leeEnfieldMk1, 135);
		Stock ukRifleCompany39MaxStock = new Stock();
		ukRifleCompany39MaxStock.put(SupplyType.AMMO, 0.005);
		ukRifleCompanyModel39.setMaxSupplies(ukRifleCompany39MaxStock);
		uk.addCompanyModel(ukRifleCompanyModel39);

		gerGrenadierCompanyModel39 = new CompanyModel("Grenadier Kompanie 39", TroopType.INFANTRY, Mobility.FOOT, 4.5d, 4, 105, uk);
		gerGrenadierCompanyModel39.addWeaponry(mauser98k, 105);
		Stock gerGrenadierCompanyMax39Stock = new Stock();
		gerGrenadierCompanyMax39Stock.put(SupplyType.AMMO, 0.005);
		gerGrenadierCompanyModel39.setMaxSupplies(gerGrenadierCompanyMax39Stock);
		ger.addCompanyModel(gerGrenadierCompanyModel39);

		ukRifleCompanyModel40 = new CompanyModel("Rifle Company 40", TroopType.INFANTRY, Mobility.FOOT, 4.5d, 3, 135, ger);
		ukRifleCompanyModel40.addWeaponry(leeEnfieldMk1, 135);
		ukRifleCompanyModel40.addWeaponry(vickersBerthierLMG, 10);
		Stock ukRifleCompany40Stock = new Stock();
		ukRifleCompany40Stock.put(SupplyType.AMMO, 0.005);
		ukRifleCompanyModel40.setMaxSupplies(ukRifleCompany40Stock);
		ukRifleCompanyModel39.addUpgrade(ukRifleCompanyModel40);
		uk.addCompanyModel(ukRifleCompanyModel40);
	}

	protected void configScenario() {
		GameSettings settings = new GameSettings();
		settings.setTurnDuration(6);
		settings.setInitialTime(new Date());
		settings.setTimeLimit(2);
		settings.setTimeMargin(1);
		settings.setHexSide(1.0f);
		settings.setStackLimit(12);
		scenario = new Scenario("sample2p", 6, 11);
		scenario.setSettings(settings);
		scenario.addPosition(gerInfReg1);
		scenario.addPosition(ukInfBg2);
	}

	protected void buildMap() {
		scenario.getMap().setHexAt(new Location(1, 1), new Hex(Terrain.HILLS, Vegetation.NONE));
		scenario.getMap().setHexAt(new Location(4, 3), new Hex(Terrain.HILLS, Vegetation.NONE));
		scenario.getMap().setHexAt(new Location(4, 4), new Hex(Terrain.HILLS, Vegetation.NONE));
		scenario.getMap().setHexAt(new Location(5, 4), new Hex(Terrain.HILLS, Vegetation.NONE));
	}

	protected void buildGERPosition() {
		gerInfReg1.setCountry(ger);
		gerInfReg1.setPrestige(8000);
		scenario.addDeployPoint(new Location(0, 0), gerInfReg1);
		scenario.addDeployPoint(new Location(1, 0), gerInfReg1);
		scenario.addDeployPoint(new Location(1, 1), gerInfReg1);
		Formation gerBn1 = new Formation(FormationLevel.BATTALION, TroopType.INFANTRY, 1);
		gerInfReg1.addSubordinate(gerBn1);
		Company gerGrenadierCompany39_1 = new Company(gerGrenadierCompanyModel39, 0, 1.5d, 0.6d, 0.9d);
		Stock gerGrenadierCompany39Stock_1 = new Stock();
		gerGrenadierCompany39Stock_1.put(SupplyType.AMMO, 0.32d);
		gerGrenadierCompany39_1.setSupplies(gerGrenadierCompany39Stock_1);
		gerBn1.addCompany(gerGrenadierCompany39_1);
		Company gerGrenadierCompany39_2 = new Company(gerGrenadierCompanyModel39, 0, 1.5d, 0.6d, 0.9d);
		Stock gerGrenadierCompany39Stock_2 = new Stock();
		gerGrenadierCompany39Stock_2.put(SupplyType.AMMO, 0.32d);
		gerGrenadierCompany39_2.setSupplies(gerGrenadierCompany39Stock_2);
		gerInfReg1.addCompany(gerGrenadierCompany39_2);

	}

	protected void buildUKPosition() {
		ukInfBg2.setCountry(uk);
		ukInfBg2.setPrestige(9500);
		scenario.addSupplySource(new Location(10, 0), ukInfBg2);
		Formation ukBn1 = new Formation(FormationLevel.BATTALION, TroopType.INFANTRY, 1);
		ukInfBg2.addSubordinate(ukBn1);
		Company ukRifleCompany39_1 = new Company(ukRifleCompanyModel39, 0, 1.8d, 0.8d, 0.9d);
		Stock ukRifleCompany39Stock_1 = new Stock();
		ukRifleCompany39Stock_1.put(SupplyType.AMMO, 0.5d);
		ukRifleCompany39_1.setSupplies(ukRifleCompany39Stock_1);
		ukBn1.addCompany(ukRifleCompany39_1);
		Company ukRifleCompany39_2 = new Company(ukRifleCompanyModel39, 1, 1.8d, 0.8d, 1.0d);
		Stock ukRifleCompany39Stock_2 = new Stock();
		ukRifleCompany39Stock_2.put(SupplyType.AMMO, 0.55d);
		ukRifleCompany39_2.setSupplies(ukRifleCompany39Stock_2);
		ukBn1.addCompany(ukRifleCompany39_2);
		Company ukRifleCompany39_3 = new Company(ukRifleCompanyModel39, 0, 1.8d, 0.8d, 0.9d);
		Stock ukRifleCompany39Stock_3 = new Stock();
		ukRifleCompany39Stock_3.put(SupplyType.AMMO, 0.55d);
		ukRifleCompany39_3.setSupplies(ukRifleCompany39Stock_3);
		ukInfBg2.addCompany(ukRifleCompany39_3);
	}

	protected void configGame() {
		game = new Game("test", scenario.getSettings(), scenario.getName());
		game.setMap(scenario.getMap());
	}

	protected void mountCamps() {
		Player ukPlayer = new Player("uk@player");
		Player gerPlayer = new Player("ger@player");
		game.addPosition(ukPlayer, ukInfBg2);
		game.addPosition(gerPlayer, gerInfReg1);
	}

	protected void mountForecast() {
		List<WeatherForecast> forecast = new ArrayList<WeatherForecast>();
		WeatherForecast t1 = new WeatherForecast();
		t1.put(WeatherType.CLEAR, 0.50d);
		t1.put(WeatherType.OVERCAST, 0.50d);
		WeatherForecast t2 = new WeatherForecast();
		t1.put(WeatherType.OVERCAST, 0.25d);
		t1.put(WeatherType.RAIN, 0.75d);
		WeatherForecast t3 = new WeatherForecast();
		t3.put(WeatherType.RAIN, 0.40d);
		t3.put(WeatherType.SNOW, 0.60d);
		forecast.add(t1);
		forecast.add(t2);
		forecast.add(t3);
		scenario.setForecast(forecast);
	}

}

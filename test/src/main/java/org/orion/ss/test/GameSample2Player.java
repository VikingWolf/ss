package org.orion.ss.test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.orion.ss.model.core.BridgeType;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.MobilityType;
import org.orion.ss.model.core.ObjectiveType;
import org.orion.ss.model.core.PositionRole;
import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.core.TroopType;
import org.orion.ss.model.core.WeaponType;
import org.orion.ss.model.geo.Airfield;
import org.orion.ss.model.geo.Bridge;
import org.orion.ss.model.geo.Fortification;
import org.orion.ss.model.geo.Hex;
import org.orion.ss.model.geo.HexSide;
import org.orion.ss.model.geo.Location;
import org.orion.ss.model.geo.OrientedLocation;
import org.orion.ss.model.geo.Railway;
import org.orion.ss.model.geo.River;
import org.orion.ss.model.geo.Road;
import org.orion.ss.model.geo.Terrain;
import org.orion.ss.model.geo.UrbanCenter;
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
import org.orion.ss.model.impl.Objective;
import org.orion.ss.model.impl.Player;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.Scenario;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.model.impl.WeaponModel;

public class GameSample2Player {

	private Game game;
	private Scenario scenario;

	private final Country uk = new Country("UK", 0.9d, new Color(100, 50, 0));
	private final Country ger = new Country("GER", 1.0d, Color.LIGHT_GRAY);

	private final Position ukCorpsI = new Position(FormationLevel.CORPS, TroopType.INFANTRY, 30, PositionRole.ATTACKER);
	private final Position gerCorpsII = new Position(FormationLevel.CORPS, TroopType.INFANTRY, 4, PositionRole.DEFENDER);

	private WeaponModel leeEnfieldMk1;
	private WeaponModel vickersBerthierLMG;
	private WeaponModel qf25pdr;

	private WeaponModel mauser98k;

	private CompanyModel ukRifleCompanyModel39;
	private CompanyModel ukRifleCompanyModel40;
	private CompanyModel ukArtilleryBatteryModel39;

	private CompanyModel gerGrenadierCompanyModel39;

	public Game buildGame() {
		buildCountries();
		buildWeaponModels();
		buildUKCompanyModels();
		buildGERCompanyModels();
		configScenario();
		buildMap();
		mountForecast();
		buildUKPosition();
		buildGERPosition();
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
		gerMarket.put(SupplyType.AMMO, 750);
		gerMarket.put(SupplyType.FUEL, 1100);
		ger.setMarket(gerMarket);
	}

	protected void buildWeaponModels() {
		leeEnfieldMk1 = new WeaponModel("Lee-Enfield Mk 1", WeaponType.SMALL_ARM, 12, 0.853, 0.457, 0.000012d, 1, 12);
		vickersBerthierLMG = new WeaponModel("VB LMG", WeaponType.SMALL_ARM, 180, 0.745, 0.457, 0.000012d, 1, 22);
		qf25pdr = new WeaponModel("QF 25-pdr", WeaponType.FIELD_GUN, 7, 0.532, 12.253, 0.0115, 6, 1500);

		mauser98k = new WeaponModel("Mauser 98k", WeaponType.SMALL_ARM, 12, 0.86, 0.5d, 0.000012d, 1, 5);
	}

	protected void buildGERCompanyModels() {
		gerGrenadierCompanyModel39 = new CompanyModel("Grenadier Kompanie 39", TroopType.INFANTRY, MobilityType.FOOT, 4.5d, 4, 105, uk);
		gerGrenadierCompanyModel39.addWeaponry(mauser98k, 105);
		Stock gerGrenadierCompanyMax39Stock = new Stock();
		gerGrenadierCompanyMax39Stock.put(SupplyType.AMMO, 0.005);
		gerGrenadierCompanyModel39.setMaxSupplies(gerGrenadierCompanyMax39Stock);
		ger.addCompanyModel(gerGrenadierCompanyModel39);
	}

	protected void buildUKCompanyModels() {
		ukRifleCompanyModel39 = new CompanyModel("Rifle Company 39", TroopType.INFANTRY, MobilityType.FOOT, 4.5d, 3, 135, uk);
		ukRifleCompanyModel39.addWeaponry(leeEnfieldMk1, 135);
		Stock ukRifleCompany39MaxStock = new Stock();
		ukRifleCompany39MaxStock.put(SupplyType.AMMO, 0.005);
		ukRifleCompanyModel39.setMaxSupplies(ukRifleCompany39MaxStock);
		uk.addCompanyModel(ukRifleCompanyModel39);

		ukRifleCompanyModel40 = new CompanyModel("Rifle Company 40", TroopType.INFANTRY, MobilityType.FOOT, 4.5d, 3, 135, uk);
		ukRifleCompanyModel40.addWeaponry(leeEnfieldMk1, 135);
		ukRifleCompanyModel40.addWeaponry(vickersBerthierLMG, 10);

		Stock ukRifleCompany40Stock = new Stock();
		ukRifleCompany40Stock.put(SupplyType.AMMO, 0.005);
		ukRifleCompanyModel40.setMaxSupplies(ukRifleCompany40Stock);
		ukRifleCompanyModel39.addUpgrade(ukRifleCompanyModel40);
		uk.addCompanyModel(ukRifleCompanyModel40);

		ukArtilleryBatteryModel39 = new CompanyModel("Artillery Battery 39", TroopType.ARTILLERY, MobilityType.FOOT, 4.5d, 3, 90, uk);
		ukArtilleryBatteryModel39.addWeaponry(leeEnfieldMk1, 40);
		ukArtilleryBatteryModel39.addWeaponry(qf25pdr, 4);
		Stock ukArtilleryBattery39Stock = new Stock();
		ukArtilleryBattery39Stock.put(SupplyType.AMMO, 0.05);
		ukArtilleryBatteryModel39.setMaxSupplies(ukArtilleryBattery39Stock);
		uk.addCompanyModel(ukArtilleryBatteryModel39);

	}

	protected void configScenario() {
		GameSettings settings = new GameSettings();
		settings.setTurnDuration(6);
		settings.setInitialTime(new Date());
		settings.setTimeLimit(2);
		settings.setTimeMargin(1);
		settings.setHexSide(2.5f);
		settings.setStackLimitModifier(3.0);
		scenario = new Scenario("sample1p", 6, 11);
		scenario.setSettings(settings);
		scenario.addPosition(ukCorpsI);
		scenario.addObjective(new Objective(ObjectiveType.DEFENDER, new Location(1, 1)));
		scenario.addObjective(new Objective(ObjectiveType.MAIN, new Location(6, 3)));
		scenario.addObjective(new Objective(ObjectiveType.SECONDARY, new Location(8, 4)));
	}

	protected void buildMap() {
		scenario.getMap().setHexAt(new Location(1, 1), new Hex(Terrain.HILLS, Vegetation.NONE));
		scenario.getMap().setHexAt(new Location(4, 3), new Hex(Terrain.HILLS, Vegetation.NONE));
		scenario.getMap().setHexAt(new Location(4, 4), new Hex(Terrain.HILLS, Vegetation.NONE));
		scenario.getMap().setHexAt(new Location(5, 4), new Hex(Terrain.HILLS, Vegetation.NONE));

		scenario.getMap().setHexAt(new Location(2, 4), new Hex(Terrain.HILLS, Vegetation.FOREST));
		scenario.getMap().setHexAt(new Location(2, 4), new Hex(Terrain.HILLS, Vegetation.FOREST));
		scenario.getMap().setHexAt(new Location(3, 4), new Hex(Terrain.HILLS, Vegetation.FOREST));
		scenario.getMap().setHexAt(new Location(4, 4), new Hex(Terrain.HILLS, Vegetation.FOREST));
		scenario.getMap().setHexAt(new Location(4, 4), new Hex(Terrain.HILLS, Vegetation.FOREST));
		scenario.getMap().setHexAt(new Location(5, 5), new Hex(Terrain.HILLS, Vegetation.FOREST));
		scenario.getMap().setHexAt(new Location(6, 5), new Hex(Terrain.HILLS, Vegetation.FOREST));

		River rhein = new River("Rhein", 2);
		rhein.addLocation(new OrientedLocation(4, 0, HexSide.NORTHEAST));
		rhein.addLocation(new OrientedLocation(4, 0, HexSide.SOUTHEAST));
		rhein.addLocation(new OrientedLocation(4, 1, HexSide.NORTHEAST));
		rhein.addLocation(new OrientedLocation(4, 1, HexSide.SOUTHEAST));
		rhein.addLocation(new OrientedLocation(4, 1, HexSide.SOUTH));
		rhein.addLocation(new OrientedLocation(3, 2, HexSide.SOUTHEAST));
		rhein.addLocation(new OrientedLocation(3, 3, HexSide.NORTHEAST));
		scenario.getMap().addRiver(rhein);

		Road road1 = new Road();
		road1.addLocation(new OrientedLocation(1, 1, HexSide.SOUTHEAST));
		road1.addLocation(new OrientedLocation(2, 1, HexSide.NORTHWEST));
		road1.addLocation(new OrientedLocation(2, 1, HexSide.SOUTH));
		road1.addLocation(new OrientedLocation(2, 2, HexSide.NORTH));
		road1.addLocation(new OrientedLocation(2, 2, HexSide.SOUTHEAST));
		road1.addLocation(new OrientedLocation(3, 3, HexSide.NORTHWEST));
		road1.addLocation(new OrientedLocation(3, 3, HexSide.SOUTHEAST));
		road1.addLocation(new OrientedLocation(4, 3, HexSide.NORTHWEST));
		road1.addLocation(new OrientedLocation(4, 3, HexSide.SOUTHEAST));
		road1.addLocation(new OrientedLocation(5, 4, HexSide.NORTHWEST));
		road1.addLocation(new OrientedLocation(5, 4, HexSide.NORTHEAST));
		road1.addLocation(new OrientedLocation(6, 3, HexSide.SOUTHWEST));
		scenario.getMap().addRoad(road1);

		Railway railway1 = new Railway(1);
		railway1.addLocation(new OrientedLocation(1, 1, HexSide.SOUTHEAST));
		railway1.addLocation(new OrientedLocation(2, 1, HexSide.NORTHWEST));
		railway1.addLocation(new OrientedLocation(2, 1, HexSide.SOUTHEAST));
		railway1.addLocation(new OrientedLocation(3, 2, HexSide.NORTHWEST));
		railway1.addLocation(new OrientedLocation(3, 2, HexSide.SOUTHEAST));
		railway1.addLocation(new OrientedLocation(4, 2, HexSide.NORTHWEST));
		railway1.addLocation(new OrientedLocation(4, 2, HexSide.SOUTHEAST));
		railway1.addLocation(new OrientedLocation(5, 3, HexSide.NORTHWEST));
		railway1.addLocation(new OrientedLocation(5, 3, HexSide.SOUTHEAST));
		railway1.addLocation(new OrientedLocation(6, 3, HexSide.NORTHWEST));
		scenario.getMap().addRailway(railway1);

		Bridge bridge1 = new Bridge(BridgeType.STEEL);
		bridge1.addLocation(new OrientedLocation(3, 2, HexSide.SOUTHEAST));
		bridge1.addLocation(new OrientedLocation(4, 2, HexSide.NORTHWEST));
		scenario.getMap().addBridge(bridge1);

		scenario.getMap().addBuilding(new UrbanCenter("Grossheim", 500, 6, new Location(1, 1), uk));
		scenario.getMap().addBuilding(new UrbanCenter("Wassenburg", 1000, 6, new Location(6, 3), ger));
		scenario.getMap().addBuilding(new UrbanCenter("Tarbek", 500, 6, new Location(8, 4), ger));
		scenario.getMap().addBuilding(new Airfield(6, new Location(7, 2), ger));
		scenario.getMap().addBuilding(new Fortification(2, 3, new Location(1, 2), uk));
	}

	protected void buildGERPosition() {
		gerCorpsII.setPrestige(11000);
		gerCorpsII.setCountry(ger);

		scenario.addDeployPoint(new Location(6, 3), gerCorpsII);
		scenario.addDeployPoint(new Location(6, 4), gerCorpsII);
		scenario.addDeployPoint(new Location(7, 3), gerCorpsII);
		scenario.addDeployPoint(new Location(7, 2), gerCorpsII);
		scenario.addDeployPoint(new Location(7, 4), gerCorpsII);
		scenario.addDeployPoint(new Location(7, 5), gerCorpsII);
		scenario.addDeployPoint(new Location(8, 4), gerCorpsII);

		scenario.addSupplySource(new Location(7, 5), gerCorpsII);
		scenario.addSupplySource(new Location(8, 5), gerCorpsII);
		scenario.addSupplySource(new Location(9, 5), gerCorpsII);

		Company corpsHQCompany = new Company(gerGrenadierCompanyModel39, 0, 4.0d, 0.8d, 0.9d);
		Stock gerStock1 = new Stock();
		gerStock1.put(SupplyType.AMMO, 0.5d);
		corpsHQCompany.setSupplies(gerStock1);

		gerCorpsII.addCompany(corpsHQCompany);

	}

	protected void buildUKPosition() {
		ukCorpsI.setPrestige(9500);
		ukCorpsI.setCountry(uk);
		scenario.addSupplySource(new Location(0, 0), ukCorpsI);
		scenario.addSupplySource(new Location(1, 0), ukCorpsI);
		scenario.addSupplySource(new Location(0, 1), ukCorpsI);

		scenario.addDeployPoint(new Location(0, 0), ukCorpsI);
		scenario.addDeployPoint(new Location(1, 0), ukCorpsI);
		scenario.addDeployPoint(new Location(1, 1), ukCorpsI);
		scenario.addDeployPoint(new Location(0, 1), ukCorpsI);
		scenario.addDeployPoint(new Location(2, 0), ukCorpsI);
		scenario.addDeployPoint(new Location(1, 2), ukCorpsI);
		scenario.addDeployPoint(new Location(2, 1), ukCorpsI);

		Company corpsHQCompany = new Company(ukRifleCompanyModel39, 0, 4.0d, 0.8d, 0.9d);
		Stock ukRifleCompany39Stock_1 = new Stock();
		ukRifleCompany39Stock_1.put(SupplyType.AMMO, 0.5d);
		corpsHQCompany.setSupplies(ukRifleCompany39Stock_1);

		Company divisionHQCompany = new Company(ukRifleCompanyModel39, 0, 1.8d, 0.8d, 1.0d);
		Stock ukRifleCompany39Stock_2 = new Stock();
		ukRifleCompany39Stock_2.put(SupplyType.AMMO, 0.55d);
		divisionHQCompany.setSupplies(ukRifleCompany39Stock_2);

		Company brigadeHQCompany = new Company(ukRifleCompanyModel39, 0, 1.8d, 0.8d, 0.9d);
		Stock ukRifleCompany39Stock_3 = new Stock();
		ukRifleCompany39Stock_3.put(SupplyType.AMMO, 0.55d);
		brigadeHQCompany.setSupplies(ukRifleCompany39Stock_3);

		Company battalionHQCompany = new Company(ukRifleCompanyModel39, 0, 1.8d, 0.8d, 0.9d);
		Stock ukRifleCompany39Stock_4 = new Stock();
		ukRifleCompany39Stock_4.put(SupplyType.AMMO, 0.55d);
		battalionHQCompany.setSupplies(ukRifleCompany39Stock_4);

		Company ukRifleCompany39_1 = new Company(ukRifleCompanyModel39, 1, 1.8d, 0.8d, 0.3d);
		Stock ukRifleCompany39Stock_5 = new Stock();
		ukRifleCompany39Stock_5.put(SupplyType.AMMO, 0.10d);
		ukRifleCompany39_1.setSupplies(ukRifleCompany39Stock_5);

		Company ukArtilleryBattery39_1 = new Company(ukArtilleryBatteryModel39, 3, 1.8d, 1.0d, 0.6d);
		Stock ukArtilleryBattery39Stock_1 = new Stock();
		ukArtilleryBattery39Stock_1.put(SupplyType.AMMO, 2.00d);
		ukArtilleryBattery39_1.setSupplies(ukArtilleryBattery39Stock_1);

		Company ukRifleCompany39_5 = new Company(ukRifleCompanyModel39, 1, 1.8d, 0.8d, 0.3d);
		Stock ukRifleCompany39Stock_6 = new Stock();
		ukRifleCompany39Stock_6.put(SupplyType.AMMO, 0.10d);
		ukRifleCompany39_5.setSupplies(ukRifleCompany39Stock_6);

		Formation ukBn1 = new Formation(FormationLevel.BATTALION, TroopType.INFANTRY, 4);
		Formation ukInfBg2 = new Formation(FormationLevel.BRIGADE, TroopType.INFANTRY, 2);
		Formation ukDiv17 = new Formation(FormationLevel.DIVISION, TroopType.INFANTRY, 1);

		ukCorpsI.addCompany(corpsHQCompany);
		ukCorpsI.addCompany(ukRifleCompany39_5);
		ukCorpsI.addSubordinate(ukDiv17);

		ukDiv17.addCompany(divisionHQCompany);
		ukDiv17.addCompany(ukArtilleryBattery39_1);
		ukDiv17.addSubordinate(ukInfBg2);

		ukInfBg2.addCompany(brigadeHQCompany);
		ukInfBg2.addSubordinate(ukBn1);

		ukBn1.addCompany(battalionHQCompany);
		ukBn1.addCompany(ukRifleCompany39_1);

	}

	protected void configGame() {
		game = new Game("test", scenario.getSettings(), scenario.getName());
		game.setMap(scenario.getMap());
	}

	protected void mountCamps() {
		Player ukPlayer = new Player("uk@player");
		game.addPosition(ukPlayer, ukCorpsI);

		Player gerPlayer = new Player("ger@player");
		game.addPosition(gerPlayer, gerCorpsII);
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

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

}

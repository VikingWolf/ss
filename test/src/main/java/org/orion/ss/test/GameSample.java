package org.orion.ss.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.orion.ss.model.core.AttackType;
import org.orion.ss.model.core.CompanyType;
import org.orion.ss.model.core.Country;
import org.orion.ss.model.core.DefenseType;
import org.orion.ss.model.core.FormationLevel;
import org.orion.ss.model.core.Mobility;
import org.orion.ss.model.core.SupplyType;
import org.orion.ss.model.core.WeatherType;
import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Defense;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.GameSettings;
import org.orion.ss.model.impl.Location;
import org.orion.ss.model.impl.Market;
import org.orion.ss.model.impl.Player;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.Scenario;
import org.orion.ss.model.impl.Stock;
import org.orion.ss.model.impl.Supply;
import org.orion.ss.model.impl.WeatherForecast;

public class GameSample {

	private Game game;
	private Scenario scenario;
	private Position ger = new Position(FormationLevel.REGIMENT, "Infanterie-Regiment 1");
	private Position uk = new Position(FormationLevel.REGIMENT, "2nd Infantry Brigade");

	private CompanyModel ukRifleCompany;
	private CompanyModel gerGrenadierCompany;

	public Game buildGame(){
		buildModels();
		configScenario();
		mountForecast();
		buildPositions();
		mountMarket();
		configGame();
		mountCamps();
		return game;
	}
	
	protected void buildModels() {
		ukRifleCompany = new CompanyModel("Rifle Company", CompanyType.INFANTRY, Mobility.LEG, 4.5d, 3, 135);		
		ukRifleCompany.addAttack(new Attack(AttackType.SOFT, 0.45d, 3));
		ukRifleCompany.addAttack(new Attack(AttackType.HARD, 0d, 1));
		ukRifleCompany.addDefense(new Defense(DefenseType.GROUND, 4));
		ukRifleCompany.addDefense(new Defense(DefenseType.CLOSE, 3));
		Stock ukRifleCompanyStock = new Stock();
		ukRifleCompanyStock.put(SupplyType.AMMO, 0.003);		
		ukRifleCompany.setMaxSupplies(ukRifleCompanyStock);		
		gerGrenadierCompany = new CompanyModel("Grenadier Kompanie", CompanyType.INFANTRY, Mobility.LEG, 4.5d, 4, 105);		
		gerGrenadierCompany.addAttack(new Attack(AttackType.SOFT, 0.5d, 3));
		gerGrenadierCompany.addAttack(new Attack(AttackType.HARD, 0d, 1));
		gerGrenadierCompany.addDefense(new Defense(DefenseType.GROUND, 4));
		gerGrenadierCompany.addDefense(new Defense(DefenseType.CLOSE, 3));
		Stock gerGrenadierCompanyStock = new Stock();
		gerGrenadierCompanyStock.put(SupplyType.AMMO, 0.003);		
		gerGrenadierCompany.setMaxSupplies(gerGrenadierCompanyStock);		
	}
	
	protected void configScenario() {
		GameSettings settings = new GameSettings(); 
		settings.setTurnDuration(6);
		settings.setInitialTime(new Date());
		settings.setTimeLimit(2);
		settings.setTimeMargin(1);
		settings.setHexSide(1.0f);
		settings.setStackLimit(6);
		this.scenario = new Scenario(10, 10);
		scenario.setSettings(settings);
	}
	
	protected void buildPositions() {
		ger.setCountry(Country.GER);
		ger.setPrestige(400);
		uk.setCountry(Country.UK);
		uk.setPrestige(450);
		Formation ukBn1 = new Formation(FormationLevel.BATTALION, "1st Bn");
		ukBn1.addCompany(new Company(ukRifleCompany, "I", new Location(2, 2)));
		uk.addSubordinate(ukBn1);
		Formation gerBn1 = new Formation(FormationLevel.BATTALION, "I. Bn");
		gerBn1.addCompany(new Company(gerGrenadierCompany, "1", new Location(3,3)));
		ger.addSubordinate(gerBn1);
	}
	
	protected void configGame(){
		game = new Game("test", scenario.getSettings(), scenario.getMarket());
	}
	
	protected void mountCamps(){
		Player ukPlayer = new Player("uk@player");
		Player gerPlayer = new Player("ger@player");
		game.addAttacker(gerPlayer, ger);
		game.addDefender(ukPlayer, uk);

	}
	
	protected void mountMarket(){
		Market market = new Market();
		market.put(new Supply(SupplyType.AMMO, Country.UK), 1);
		market.put(new Supply(SupplyType.AMMO, Country.GER), 1);
		market.put(new Supply(SupplyType.FUEL, Country.UK), 3);
		market.put(new Supply(SupplyType.FUEL, Country.GER), 5);
		scenario.setMarket(market);
		
	}
	
	protected void mountForecast(){
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

package org.orion.ss.test;

import java.util.Date;

import org.orion.ss.model.core.AttackType;
import org.orion.ss.model.core.CompanyType;
import org.orion.ss.model.core.Country;
import org.orion.ss.model.core.DefenseType;
import org.orion.ss.model.core.Mobility;
import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.Company;
import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Defense;
import org.orion.ss.model.impl.Formation;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.GameSettings;
import org.orion.ss.model.impl.Location;
import org.orion.ss.model.impl.Player;
import org.orion.ss.model.impl.Position;
import org.orion.ss.model.impl.Scenario;
import org.orion.ss.service.OrderService;
import org.orion.ss.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	protected final static Logger logger = LoggerFactory.getLogger(Test.class);
	
	private Scenario scenario;
	private Game game;
	private CompanyModel ukRifleCompany = new CompanyModel();
	private CompanyModel gerGrenadierCompany = new CompanyModel();
	private Position ger = new Position();
	private Position uk = new Position();
	
	/* services */
	private OrderService orderService;
	private GameService gameService;

	public static void main(String[] args) {
		Test test = new Test();
		logger.info("Starting test at " + new Date());
		test.prepareGame();
		test.mountServices();
		test.start();
		/** secuencia
		 * 1. Determinar iniciativa de los jugadores
		 * 	1.1 El jugador con la iniciativa 
		 */
		logger.info("Ending test at " + new Date());
	}
	
	protected void start(){
		logger.info(gameService.nextPlayer() + "");
	}
	
	protected void prepareGame(){
		buildModels();
		configScenario();
		buildPositions();
		configGame();
		mountCamps();
	}
	
	protected void mountServices(){
		logger.info("Mounting services...");
		orderService = new OrderService(game);
		gameService = new GameService(game);
	}

	protected void buildModels() {
		logger.info("Building Models...");
		ukRifleCompany.setCode("Rifle Company");
		ukRifleCompany.setType(CompanyType.INFANTRY);
		ukRifleCompany.setMobility(Mobility.LEG);
		ukRifleCompany.setSpeed(4.5d);
		ukRifleCompany.setInitiative(3);
		ukRifleCompany.addAttack(new Attack(AttackType.SOFT, 0d, 3));
		ukRifleCompany.addAttack(new Attack(AttackType.HARD, 0d, 1));
		ukRifleCompany.addDefense(new Defense(DefenseType.GROUND, 4));
		ukRifleCompany.addDefense(new Defense(DefenseType.CLOSE, 3));
		gerGrenadierCompany.setCode("Rifle Company");
		gerGrenadierCompany.setType(CompanyType.INFANTRY);
		gerGrenadierCompany.setMobility(Mobility.LEG);
		gerGrenadierCompany.setSpeed(4.5d);
		gerGrenadierCompany.setInitiative(4);
		gerGrenadierCompany.addAttack(new Attack(AttackType.SOFT, 0d, 3));
		gerGrenadierCompany.addAttack(new Attack(AttackType.HARD, 0d, 1));
		gerGrenadierCompany.addDefense(new Defense(DefenseType.GROUND, 4));
		gerGrenadierCompany.addDefense(new Defense(DefenseType.CLOSE, 3));
	}

	protected void configScenario() {
		logger.info("Configurating scenario...");
		GameSettings settings = new GameSettings(); 
		settings.setTurnDuration(6);
		settings.setInitialTime(new Date());
		settings.setTimeLimit(3);
		settings.setTimeMargin(1);
		settings.setHexSide(1.0f);
		settings.setStackLimit(6);
		this.scenario = new Scenario(10, 10);
	}

	protected void buildPositions() {
		logger.info("Bulding positions...");
		ger.setCode("German Attacker");
		ger.setCountry(Country.GER);
		uk.setCode("British Defender");
		uk.setCountry(Country.UK);
		Formation ukReg1 = new Formation("3rd Rifle Regiment");
		ukReg1.addCompany(new Company(ukRifleCompany, new Location(2, 2), 1.0d, 1.0d, 1.0d));
		uk.addFormation(ukReg1);
		Formation gerReg1 = new Formation("1. Grenadierregiment");
		gerReg1.addCompany(new Company(gerGrenadierCompany, new Location(3,3), 1.0d, 1.0d, 1.0d));
		ger.addFormation(gerReg1);
	}
	
	protected void configGame(){
		logger.info("Configurating game...");
		game = new Game("test", scenario.getSettings());
	}
	
	protected void mountCamps(){
		logger.info("Mounting camps...");
		Player ukPlayer = new Player("uk@player");
		Player gerPlayer = new Player("ger@player");
		game.addAttacker(gerPlayer, ger);
		game.addDefender(ukPlayer, uk);

	}

	/* getters & setters */

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}

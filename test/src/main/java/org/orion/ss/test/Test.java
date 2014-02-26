package org.orion.ss.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.orion.ss.model.core.AttackType;
import org.orion.ss.model.core.CompanyType;
import org.orion.ss.model.core.Country;
import org.orion.ss.model.core.Mobility;
import org.orion.ss.model.impl.Attack;
import org.orion.ss.model.impl.CompanyModel;
import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	protected final static Logger logger = LoggerFactory.getLogger(Test.class);

	private Game game;

	public static void main(String[] args) {
		Test test = new Test();
		logger.error("Starting test at " + new Date());
		test.configGame();
		test.buildModels();
		test.buildPositions();
	}

	protected void configGame() {
		this.game = new Game();
		game.setName("test");
		game.setTurnDuration((byte) 6);
		game.setInitialTime(new Date());
		game.setTimeLimit((short) 3);
		game.setTimeMargin((short) 1);
		game.setHexSide(1.0f);
		game.setStackLimit((short) 6);
	}

	protected void buildModels() {
		CompanyModel ukRifleCompany = new CompanyModel();
		ukRifleCompany.setCode("Rifle Company");
		ukRifleCompany.setType(CompanyType.INFANTRY);
		ukRifleCompany.setMobility(Mobility.LEG);
		ukRifleCompany.addAttack(new Attack(AttackType.SOFT, 0d, 3));
	}

	protected void buildPositions() {
		List<Position> attacker = new ArrayList<Position>();
		List<Position> defender = new ArrayList<Position>();
		Position ger = new Position();
		ger.setCode("1st Infanterieregiment");
		ger.setCountry(Country.GER);
		attacker.add(ger);
		Position uk = new Position();
		uk.setCode("3rd Rifle Regiment");
		uk.setCountry(Country.UK);
		defender.add(uk);
		game.setAttacker(attacker);
		game.setDefender(defender);
	}

	/* getters & setters */

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}

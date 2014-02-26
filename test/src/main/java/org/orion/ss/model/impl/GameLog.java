package org.orion.ss.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class GameLog extends Observable {

	private final List<String> entries;

	public GameLog() {
		super();
		entries = new ArrayList<String>();
	}

	public String buildLog() {
		String result = "";
		for (String entry : entries) {
			result += entry + "\n";
		}
		return result;
	}

	public void addEntry(String entry) {
		entries.add(entry);
		setChanged();
		this.notifyObservers(this.buildLog());
	}

	public void addDisplay(GameSettings settings) {
		addEntry("turn duration=" + settings.getTurnDuration());
		addEntry("hex side=" + settings.getHexSide());
		addEntry("stack limit=" + settings.getStackLimit());
		addEntry("turn limit=" + settings.getTimeLimit());
		addEntry("turn limit margin=" + settings.getTimeMargin());

	}

	public void addDisplay(Market market) {
		addEntry("market=" + market.toString());
	}

	public void addSeparator() {
		addEntry("__________________________________________________________________________________________________________________\n");
	}

}

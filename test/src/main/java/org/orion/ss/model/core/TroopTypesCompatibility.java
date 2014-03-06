package org.orion.ss.model.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TroopTypesCompatibility {

	private final static Table _map = new Table();

	protected final static Logger logger = LoggerFactory.getLogger(TroopTypesCompatibility.class);

	static {
		_map.add(TroopType.INFANTRY, FormationLevel.BATTALION, TroopType.INFANTRY);
		_map.add(TroopType.INFANTRY, FormationLevel.REGIMENT, TroopType.INFANTRY);
		_map.add(TroopType.INFANTRY, FormationLevel.DIVISION, TroopType.INFANTRY);
		_map.add(TroopType.INFANTRY, FormationLevel.CORPS, TroopType.INFANTRY);
		_map.add(TroopType.INFANTRY, FormationLevel.ARMY, TroopType.INFANTRY);
		_map.add(TroopType.INFANTRY, FormationLevel.ARMY, TroopType.CAVALRY);
		_map.add(TroopType.INFANTRY, FormationLevel.ARMY, TroopType.ARTILLERY);
		_map.add(TroopType.INFANTRY, FormationLevel.ARMY_GROUP, TroopType.INFANTRY);
		_map.add(TroopType.INFANTRY, FormationLevel.ARMY_GROUP, TroopType.CAVALRY);
		_map.add(TroopType.INFANTRY, FormationLevel.ARMY_GROUP, TroopType.ARTILLERY);
		// TODO terminar de llenar
	}

	public final static List<TroopType> loadCompatibles(TroopType type, FormationLevel level) {
		return _map.get(new TableKey(type, level));
	}

}

class Table extends HashMap<TableKey, List<TroopType>> {

	private static final long serialVersionUID = 4651782425544734534L;

	public void add(TroopType troopType, FormationLevel formationLevel, TroopType compatible) {
		TableKey key = new TableKey(troopType, formationLevel);
		List<TroopType> value = this.get(key);
		if (value == null) {
			value = new ArrayList<TroopType>();
		}
		value.add(compatible);
		this.put(key, value);
	}

}

class TableKey {
	private final TroopType type;
	private final FormationLevel level;

	protected TableKey(TroopType type, FormationLevel level) {
		super();
		this.type = type;
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableKey other = (TableKey) obj;
		if (level != other.level)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TableKey [type=" + type + ", level=" + level + "]";
	}

}

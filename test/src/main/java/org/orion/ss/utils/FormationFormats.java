package org.orion.ss.utils;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;

import org.orion.ss.model.Unit;
import org.orion.ss.model.core.FormationLevel;

public class FormationFormats {

	private final static Map<FormationLevel, NumberFormat> _formats;

	static {
		_formats = new HashMap<FormationLevel, NumberFormat>();
		_formats.put(FormationLevel.COMPANY, new DirectFormat());
		_formats.put(FormationLevel.BATTALION, new OrdinalFormat());
		_formats.put(FormationLevel.REGIMENT, new OrdinalFormat());
		_formats.put(FormationLevel.BRIGADE, new OrdinalFormat());
		_formats.put(FormationLevel.DIVISION, new OrdinalFormat());
		_formats.put(FormationLevel.CORPS, new RomanFormat());
		_formats.put(FormationLevel.ARMY, new OrdinalFormat());
		_formats.put(FormationLevel.ARMY_GROUP, new LetterFormat());

	}

	public static String format(Unit unit){
		NumberFormat formatter = _formats.get(unit.getFormationLevel());
		return formatter.format(unit.getId()) + " " + unit.getFormationLevel().getAbbreviation();
	}
	
	private static String simpleFormat(Unit unit){
		NumberFormat formatter = _formats.get(unit.getFormationLevel());
		return formatter.format(unit.getId());		
	}

	public static String fullFormat(Unit unit){
		String result = "";
		if (unit != null) {
			switch (unit.getFormationLevel()){
			case COMPANY :
				result = simpleFormat(unit) + "/" + fullFormat(unit.getParent()); 
				break;
			case BATTALION :
				result = simpleFormat(unit) + ", " + fullFormat(unit.getParent()); 
				break;
			case REGIMENT :
				result = format(unit);
				if (unit.getParent() != null){
					result += "(" + fullFormat(unit.getParent()) + ")" ;
				}  
				break;				
			default : 
				result = format(unit);
			}
		}
		return result;

	}

}

class DirectFormat extends NumberFormat {

	private static final long serialVersionUID = 2046236141662654554L;

	@Override
	public StringBuffer format(double arg0, StringBuffer arg1, FieldPosition arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer format(long number, StringBuffer arg1, FieldPosition arg2) {
		if ((number <= 0) || (number > 25)) throw new IllegalArgumentException();
		StringBuffer result = new StringBuffer();
		result.append(number);
		return result;
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		// TODO Auto-generated method stub
		return null;
	}
}

class LetterFormat extends NumberFormat {

	private static final long serialVersionUID = 2046236141662654554L;

	@Override
	public StringBuffer format(double arg0, StringBuffer arg1, FieldPosition arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer format(long number, StringBuffer arg1, FieldPosition arg2) {
		if (number <= 0) throw new IllegalArgumentException();
		String[] numerals = { "", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };		
		StringBuffer result = new StringBuffer();
		result.append(numerals[(int)number]);
		return result;
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		// TODO Auto-generated method stub
		return null;
	}
}

class OrdinalFormat extends NumberFormat {

	private static final long serialVersionUID = 2046236141662654554L;

	@Override
	public StringBuffer format(double arg0, StringBuffer arg1, FieldPosition arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer format(long number, StringBuffer arg1, FieldPosition arg2) {
		if (number <= 0) throw new IllegalArgumentException();
		StringBuffer result = new StringBuffer();
		result.append(number);
		int unit = (int)number % 10;
		switch(unit) {
		case 1 : 
			result.append("st");
			break;
		case 2 :
			result.append("nd");
			break;
		case 3 :
			result.append("rd");
			break;
		default :
			result.append("th");				
		}
		return result;
	}

	@Override
	public Number parse(String arg0, ParsePosition arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}

class RomanFormat extends NumberFormat {

	private static final long serialVersionUID = -6582625254049074113L;

	@Override
	public StringBuffer format(double arg0, StringBuffer arg1, FieldPosition arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer format(long number, StringBuffer arg1, FieldPosition arg2) {
		if (number <= 0 || number > 3999){
			throw new IllegalArgumentException();
		}
		int[] values = new int[] { 1000, 900, 500, 400, 100,90, 50, 40, 10, 9, 5, 4, 1 };
		String[] numerals = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };		
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < 13; i++){
			while (number >= values[i]){
				number -= values[i];
				result.append(numerals[i]);
			}
		}
		return result;
	}

	@Override
	public Number parse(String arg0, ParsePosition arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}



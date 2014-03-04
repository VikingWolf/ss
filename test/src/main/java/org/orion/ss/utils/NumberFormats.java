package org.orion.ss.utils;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class NumberFormats {

	public final static NumberFormat DF_2 = new DecimalFormat("#0.0#");
	public final static NumberFormat DF_3 = new DecimalFormat("#0.0##");
	public final static NumberFormat PERCENT = new DecimalFormat("0.##%");
	public final static NumberFormat XP = new XpFormat();
	public final static NumberFormat MORALE = new MoraleFormat();
	public final static NumberFormat PRESTIGE = new PrestigeFormat();

}

class PrestigeFormat extends NumberFormat {

	private static final long serialVersionUID = 1302722772496701371L;

	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo,
			FieldPosition pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		StringBuffer result = new StringBuffer();
		if (number>999999){
			int entire = (int) Math.floor(number / 100000);
			int decimal = ((int)number - 100000 * entire) / 1000; /* 2 digits */		
			result.append(entire+","+decimal+"M");
		}
		if (number>9999){
			int entire = (int) Math.floor(number / 1000);
			int decimal = ((int)number - 1000 * entire) / 100;	/* 1 digit */	
			result.append(entire+","+decimal+"k");			
		} else {
			result.append("" + number);
		}
		return result;
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

class MoraleFormat extends NumberFormat {

	private static final long serialVersionUID = 8824410144907504062L;

	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo,
			FieldPosition pos) {
		return format((long)Math.floor(number*10), toAppendTo, pos);
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo,
			FieldPosition pos) {
		StringBuffer result = new StringBuffer();
		switch((int)number){
			case 9 :
				result.append("A");
				break;
			case 8 : 
				result.append("B+");
				break;
			case 7 :
				result.append("B");
				break;
			case 6 : 
				result.append("C+");
				break;
			case 5 : 
				result.append("C");
				break;
			case 4 : 
				result.append("D+");
				break;
			case 3 : 
				result.append("D");
				break;
			case 2 : 
				result.append("E+");
				break;
			case 1 : 
				result.append("E");
				break;
			case 0 : 
				result.append("F");
				break;
			default: 
				result.append("ERROR");
				break;
		}
		return result;
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		// TODO Auto-generated method stub
		return null;
	}

}

class XpFormat extends NumberFormat {

	private static final long serialVersionUID = -5949893919320744900L;

	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo,
			FieldPosition pos) {
		// TODO Auto-generated method stub
		int entire = (int) Math.floor(number);
		int decimal = (int) ((number - entire) * 4);		
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < entire; i++){
			result.append("\u2606");
		}
		for (int i = 0; i < decimal; i++){
			result.append("+");
		}
		return result; 
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		return format((double)number, toAppendTo, pos);
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		// TODO Auto-generated method stub
		return null;
	}

}

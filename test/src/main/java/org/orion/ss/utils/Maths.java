package org.orion.ss.utils;

import java.util.Random;

public class Maths {
	
	public static double gaussianRandomize(double value, double unitaryDeviation){
		return unitaryDeviation * value * new Random().nextGaussian() + value; 
	}

}

package org.orion.ss.test;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	protected final static Logger logger = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) {
		Test test = new Test();
		logger.info("Starting test at " + new Date());
		test.start();
		logger.info("Ending test at " + new Date());
	}

	protected void start() {
		int[] rgbValues = { 0, 75, 165, 255 };
		int darkTreshold = rgbValues[1];
		int count = 0;
		for (int red : rgbValues) {
			for (int green : rgbValues) {
				for (int blue : rgbValues) {
					if (red > darkTreshold || green > darkTreshold || blue > darkTreshold) {
						logger.error("red=" + red + ", green=" + green + ", blue=" + blue);
						count++;
					}
				}
			}
		}
		logger.error("colors=" + count);
	}

}

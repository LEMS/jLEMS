package org.lemsml.jlems.io.logging;

import org.lemsml.jlems.core.logging.E;

public class TimeLogger {

	long initTime;
	
	public TimeLogger() {
		initTime = System.nanoTime();
	}
	
	public void report(String s) {
		long tms = Math.round((System.nanoTime() - initTime) / 1.e6);
		E.info("Timer " + s + " " + tms + "ms");
	}
	
}

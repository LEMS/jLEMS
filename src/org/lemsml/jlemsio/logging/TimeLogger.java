package org.lemsml.jlemsio.logging;

import org.lemsml.jlems.logging.E;

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

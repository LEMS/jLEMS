package org.lemsml.jlems.core.run;

import java.util.HashMap;

public final class Constants {

	
	private Constants() {
		
	}
	
	
	public static HashMap<String, Double> constantsHM = null;
	
	public static void setConstantsHM(HashMap<String, Double> chm) {
		constantsHM = chm;
	}
	
	public static HashMap<String, Double> getConstantsHM() {
		return constantsHM;
	}
	
	
}

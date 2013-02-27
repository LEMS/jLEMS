package org.lemsml.jlems.core.run;

public final class Out {

	private Out() {
		
	}
	
	public static String formatDouble(double d) {

		// String ret = String.format("%.4g", d);
		// avoiding string.format for GWT
		String ret = "" + (float)d;
		
		return ret;
	}

}

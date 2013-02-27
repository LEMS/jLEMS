package org.lemsml.jlems.core.util;

import java.util.ArrayList;

public final class StringUtil {

		
	private StringUtil() {
		
	}
	
	
	public static String[] splitWords(String src) {
		String[] bits = src.split(" ");
		
		ArrayList<String> wk = new ArrayList<String>();
		for (String s : bits) {
			s = s.trim();
			if (s.length() > 0) {
				wk.add(s);
			}
		}
		return wk.toArray(new String[wk.size()]);
	}

	public static String[] splitCommaWords(String sob) {
		String[] ret;
		if (sob.indexOf(",") > 0) {
			String[] wk = sob.split(",");
			ret = new String[wk.length];
			for (int i = 0; i < wk.length; i++) {
				ret[i] = wk[i].trim();
 			}
			
		} else {
			ret= splitWords(sob);
		}
		return ret;
	}

	public static String join(ArrayList<String> items, String op) {
		String ret = "";
		boolean first = true;
		for (String s : items) {
			if (!first) {
				ret += op;
			}
			ret += s;
			first = false;
		}
		return ret;
	}

	public static String capitalize(String s) {
		String ret = s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
		return ret;
	}

	 
	
	
}

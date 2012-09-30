package org.lemsml.jlems.util;

import java.util.ArrayList;

public class StringUtil {

	
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

	 
	
	
}

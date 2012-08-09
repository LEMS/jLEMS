package org.lemsml.nineml;

import java.util.HashMap;
import java.util.StringTokenizer;

import org.lemsml.type.BodyValued;

public class NineML_MathInline implements BodyValued {
	
	String bodyValue = "";
	 
	
	
	static HashMap<String, String> fortranFormatSymbolMap;
	
	{
		fortranFormatSymbolMap = new HashMap<String, String>();
		fortranFormatSymbolMap.put("&gt;", ".gt.");
		fortranFormatSymbolMap.put("&lt;", ".lt.");
		fortranFormatSymbolMap.put("&gt;=", ".ge.");
		fortranFormatSymbolMap.put("&lt;=", ".le.");
	}
	
	
	
	public void setBodyValue(String sin) {
		String s = sin;
		s = s.trim();
		
		bodyValue += s;
		bodyValue += " ";
	 
	}
	 
	
	public String getBodyValue() {
		return bodyValue;
	}

	 
	public String getFortranFormatBodyValue() {
		StringTokenizer st = new StringTokenizer(bodyValue);
		String ret = "";
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			if (fortranFormatSymbolMap.containsKey(tok)) {
				tok = fortranFormatSymbolMap.get(tok);
			}
			ret += tok;
			ret += " ";
		}
		return ret;
	}
	
	
}

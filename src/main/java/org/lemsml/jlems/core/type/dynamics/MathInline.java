package org.lemsml.jlems.core.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.core.type.BodyValued;

public class MathInline implements BodyValued {

	String bodyValue = "";
	String expressionValue = "";
	
	static HashMap<String, String> importHM;
	
	static {
		importHM = new HashMap<String, String>();
		importHM.put("&gt;", ".gt.");
		importHM.put("&lt;", ".lt.");
		importHM.put("&gt;=", ".ge.");
		importHM.put("&lt;=", ".le.");
	}
	
	
	
	@Override
	public void setBodyValue(String sin) {
		String s = sin;
		s = s.trim();
		
		bodyValue += s;
		bodyValue += " ";
		
		if (importHM.containsKey(s)) {
			s = importHM.get(s);
		}
		
		expressionValue += s;
		expressionValue += " ";
	}
	
	public String getExpressionValue() {
		return expressionValue;
	}
	
	public String getBodyValue() {
		return bodyValue;
	}

}

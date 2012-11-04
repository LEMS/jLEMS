package org.lemsml.jlems.type.dynamics;

import org.lemsml.jlems.sim.ContentError;

public class ExpressionValued {
	
	public String value = null;
	
	
	public MathInline mathInline;
	
	
	public void setValue(String s) {
		value = s;
	}
	
	public String getValueExpression() {
		return value;
	}
	
	
	public void extract() throws ContentError {
		if (mathInline != null) {
			
			value = mathInline.getExpressionValue();
		} else if (value == null) {
			throw new ContentError("" + this + " requires a value attribute or a MathInline element");
		}
		
	}
	
	
}

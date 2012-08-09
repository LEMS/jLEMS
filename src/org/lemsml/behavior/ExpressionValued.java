package org.lemsml.behavior;

import org.lemsml.util.ContentError;

public class ExpressionValued {
	
	public String value = null;
	
	
	public MathInline mathInline;
	
	
	
	public void extract() throws ContentError {
		if (mathInline != null) {
			
			value = mathInline.getExpressionValue();
		} else if (value == null) {
			throw new ContentError("" + this + " requires a value attribute or a MathInline element");
		}
		
	}
	
	
}

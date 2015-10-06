package org.lemsml.jlems.core.type.dynamics;

import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.sim.ContentError;

public class ExpressionValued implements IVisitable {
	
	public String value = null;
	public MathInline mathInline;
	public ParseTree parseTree;
	
	
	public void setValue(String s) {
		value = s;
	}
	
    @Override
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

	public void copyInto(ExpressionValued ret) {
		ret.value = value;
	}

	@Override
	public ParseTree getParseTree() {
		return parseTree;
	}

	
	
}

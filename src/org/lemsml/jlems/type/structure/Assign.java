package org.lemsml.jlems.type.structure;

import org.lemsml.jlems.expression.DoubleEvaluable;

public class Assign {

	public String property;
	public String value;
	
	public String exposeAs;
	
	DoubleEvaluable doubleEvaluable;
	
	
	public String getProperty() {
		return property;
	}
	
	public String getExposeAs() {
		return exposeAs;
	}
	
	public String getExpression() {
		return value;
	}

	public void setDoubleEvaluable(DoubleEvaluable de) {
		doubleEvaluable = de;
	}

	public DoubleEvaluable getDoubleEvaluable() {
		return doubleEvaluable;
	}

    @Override
    public String toString() {
        return "Assign{" + "property=" + property + ", value=" + value + ", exposeAs=" + exposeAs + ", doubleEvaluable=" + doubleEvaluable + '}';
    }

	
	
	
}

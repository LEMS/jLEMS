package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.eval.DoubleEvaluator;

public class Assign {

	public String property;
	public String value;
	
	public String exposeAs;
	
	DoubleEvaluator doubleEvaluator;
	
	
	public Assign() {
		
	}
	
	
	public String getProperty() {
		return property;
	}
	
	public String getExposeAs() {
		return exposeAs;
	}
	
	public String getExpression() {
		return value;
	}

	public void setDoubleEvaluator(DoubleEvaluator de) {
		doubleEvaluator = de;
	}

	public DoubleEvaluator getDoubleEvaluator() {
		return doubleEvaluator;
	}

    @Override
    public String toString() {
        return "Assign{" + "property=" + property + ", value=" + value + ", exposeAs=" + exposeAs + ", doubleEvaluator=" + doubleEvaluator + '}';
    }

	
	
	
}

package org.lemsml.expression;

import java.util.HashMap;

public abstract class FloatResultNode extends FloatOperatorNode implements DoubleEvaluable {


	public FloatResultNode(String s) {
		super(s);
	}


	public double evalD(HashMap<String, Double> valHS) throws ParseError {
 		 double x = (leftEvaluable != null ? leftEvaluable.evalD(valHS) : Double.NaN);
		 double y = (rightEvaluable != null ? rightEvaluable.evalD(valHS) : Double.NaN);
		 double ret = op(x, y);
		 return ret;
	}
 
	
	public abstract double op(double x, double y);

	
	
	
}

package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.util.E;

public class Evaluator {

	
	public double evaluate(Node root, HashMap<String, Double> valHM) throws ParseError {
		double ret = Double.NaN;
		
		if (root instanceof DoubleEvaluable) {
			ret = ((DoubleEvaluable)root).evalD(valHM);
		} else {
			E.error("cant evaluate " + root);
		}
		
		return ret;
	}
	
	// could get reverse polish form and use a stack for quicker evaql
	
	
	
	
}

package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.eval.DVal;
import org.lemsml.util.ContentError;

public interface DoubleEvaluable extends Evaluable {

	public double evalD(HashMap<String, Double> valHS) throws ParseError;

	public DVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError;

	
	
}

package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.util.ContentError;

public interface DoubleEvaluable extends Evaluable {

	public double evalD(HashMap<String, Double> valHS) throws ParseError;

	public DVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError;

	
	
}

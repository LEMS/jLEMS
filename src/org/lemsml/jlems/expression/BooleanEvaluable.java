package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.BVal;
import org.lemsml.jlems.util.ContentError;

public interface BooleanEvaluable extends Evaluable {
	
	public boolean evalB(HashMap<String, Double> valHS) throws ParseError;

	public BVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError;

	public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError;
	
}

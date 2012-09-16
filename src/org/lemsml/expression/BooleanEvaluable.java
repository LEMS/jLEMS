package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.eval.BVal;
import org.lemsml.util.ContentError;

public interface BooleanEvaluable extends Evaluable {
	
	public boolean evalB(HashMap<String, Double> valHS) throws ParseError;

	public BVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError;

	public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError;
	
}

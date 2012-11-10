package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.sim.ContentError;

public interface DoubleParseTreeNode extends ParseTreeNode {
 
	public DVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError;

	public String toExpression() throws ContentError;
	
}

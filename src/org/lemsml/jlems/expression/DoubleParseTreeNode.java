package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.AbstractDVal;
import org.lemsml.jlems.sim.ContentError;

public interface DoubleParseTreeNode extends ParseTreeNode {
 
	AbstractDVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError;

	String toExpression() throws ContentError;
	
}

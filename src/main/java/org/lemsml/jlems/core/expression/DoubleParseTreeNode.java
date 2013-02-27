package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractDVal;
import org.lemsml.jlems.core.sim.ContentError;

public interface DoubleParseTreeNode extends ParseTreeNode {
 
	AbstractDVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError;

	String toExpression() throws ContentError;
	
}

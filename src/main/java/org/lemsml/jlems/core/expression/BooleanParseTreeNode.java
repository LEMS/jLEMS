package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractBVal;
import org.lemsml.jlems.core.sim.ContentError;

public interface BooleanParseTreeNode extends ParseTreeNode {
	
 
	AbstractBVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError;

	void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError;

	String toExpression() throws ContentError;
	     
}

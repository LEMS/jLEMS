package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.AbstractBVal;
import org.lemsml.jlems.sim.ContentError;

public interface BooleanParseTreeNode extends ParseTreeNode {
	
 
	AbstractBVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError;

	void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError;

	String toExpression() throws ContentError;
	     
}

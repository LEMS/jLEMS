package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.sim.ContentError;

public interface ParseTreeNode {
  
	Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError;
 
	Dimensional evaluateDimensional(HashMap<String, Dimensional> adml) throws ContentError;

	void substituteVariables(HashMap<String, String> varHM) throws ContentError;

	String toExpression() throws ContentError;

	void doVisit(ExpressionVisitor ev) throws ContentError;
}

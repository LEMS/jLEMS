package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.sim.ContentError;

public interface ParseTreeNode {
  
	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError;
 
	public Dimensional evaluateDimensional(HashMap<String, Dimensional> adml) throws ContentError;

	public void substituteVariables(HashMap<String, String> varHM) throws ContentError;

	public String toExpression() throws ContentError;
}

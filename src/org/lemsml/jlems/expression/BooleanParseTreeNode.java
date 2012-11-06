package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.BVal;
import org.lemsml.jlems.sim.ContentError;

public interface BooleanParseTreeNode extends ParseTreeNode {
	
 
	public BVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError;

	public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError;
	
}

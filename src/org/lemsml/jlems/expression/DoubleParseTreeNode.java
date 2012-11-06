package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.sim.ContentError;

public interface DoubleParseTreeNode extends ParseTreeNode {
 
	public DVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError;

}

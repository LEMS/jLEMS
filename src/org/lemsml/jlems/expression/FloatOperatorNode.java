package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.sim.ContentError;

public abstract class FloatOperatorNode extends OperatorNode {

	protected DoubleParseTreeNode leftEvaluable;
	protected DoubleParseTreeNode rightEvaluable;
	
	public FloatOperatorNode(String s) {
		super(s);
	}


	 
	
 

	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		Dimensional dl = leftEvaluable.getDimensionality(dimHM);
		Dimensional dr = rightEvaluable.getDimensionality(dimHM);
		Dimensional ret = null;
		if (dl != null && dr != null) {
			ret = dimop(dl, dr);
		} else {
			throw new ContentError("Null dimension in operator: " + dl + " " + dr + " operator: " + symbol);
		}
		return ret;
	}
	
	
	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dimHM) throws ContentError {
		return getDimensionality(dimHM);
	}
		
	public abstract Dimensional dimop(Dimensional dl, Dimensional dr) throws ContentError;
	
	
}
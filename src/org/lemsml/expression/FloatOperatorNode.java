package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.util.ContentError;

public abstract class FloatOperatorNode extends OperatorNode {

	protected DoubleEvaluable leftEvaluable;
	protected DoubleEvaluable rightEvaluable;
	
	public FloatOperatorNode(String s) {
		super(s);
	}


	
	private DoubleEvaluable getEvaluable(Node n) throws ParseError {
		DoubleEvaluable ret = null;
		if (n != null) {
		 if (n instanceof DoubleEvaluable) {
			 ret = (DoubleEvaluable)n;
		 } else {
			 throw new ParseError("non evaluable node " + n);
		 }
		} else {
			throw new ParseError("no node in operator? " + symbol + "left=" + left + "   right=" + right);
		}
		 return ret;
	 }
	
		
	public void evaluablize() throws ParseError {
		leftEvaluable = getEvaluable(left);
		rightEvaluable = getEvaluable(right);
		if (leftEvaluable != null) {
			leftEvaluable.evaluablize();
		}
		if (rightEvaluable != null) {
			rightEvaluable.evaluablize();
		}
	}
	

	public void setValues(HashMap<String, Valued> valHM) throws ContentError {
		if (leftEvaluable != null) {
			leftEvaluable.setValues(valHM);
		}
		if (rightEvaluable != null) {
			rightEvaluable.setValues(valHM);
		}
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
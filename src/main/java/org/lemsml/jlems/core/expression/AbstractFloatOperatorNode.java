package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.sim.ContentError;

public abstract class AbstractFloatOperatorNode extends AbstractOperatorNode {

	protected DoubleParseTreeNode leftEvaluable;
	protected DoubleParseTreeNode rightEvaluable;
	
	public AbstractFloatOperatorNode(String s) {
		super(s);
	}

	 
	
	protected void checkLeftRight() throws ContentError {
		if (leftEvaluable == null) {
			if (left instanceof DoubleParseTreeNode) {
				leftEvaluable = (DoubleParseTreeNode)left;
			} else {
				throw new ContentError("Wrong node type in float operator: " + left);
			}
		}
		
		if (rightEvaluable == null) {
			if (right instanceof DoubleParseTreeNode) {
				rightEvaluable = (DoubleParseTreeNode)right;
			} else {
				throw new ContentError("Wrong node type in float operator: " + right);
			}
		}
	}
	
	

	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		checkLeftRight();
		
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
	
	
	public void substituteVariables(HashMap<String, String> varHM) throws ContentError {
		checkLeftRight();
		leftEvaluable.substituteVariables(varHM);
		rightEvaluable.substituteVariables(varHM);
	}
	
	
	public String toExpression() throws ContentError {
		checkLeftRight();
		return "(" + leftEvaluable.toExpression() + " " + symbol + " " + rightEvaluable.toExpression() + ")";
	}
	
}
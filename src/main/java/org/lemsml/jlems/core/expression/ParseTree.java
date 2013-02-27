package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.BBase;
import org.lemsml.jlems.core.eval.BooleanEvaluator;
import org.lemsml.jlems.core.eval.DBase;
import org.lemsml.jlems.core.eval.DoubleEvaluator;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
 

public class ParseTree {
 
	ParseTreeNode root;
	
	public ParseTree(ParseTreeNode r) { 
		root = r;
	}
	
	public String toString() {
		return root.toString();
	}
	

	public boolean isFloat() {
		boolean ret = false;
		if (root instanceof DoubleParseTreeNode) {
			ret = true;
		}
		return ret;
	}
	
	
	public boolean isBoolean() {
		boolean ret = false;
		if (root instanceof BooleanParseTreeNode) {
			ret = true;
		}
		return ret;
	}

	public BooleanEvaluator makeBooleanEvaluator() throws ContentError {
		BBase ret = null;
		if (root instanceof BooleanParseTreeNode) {
			ret= new BBase(((BooleanParseTreeNode) root).makeEvaluable(null));
		}
		return ret;
	}

 
	
	public BooleanEvaluator makeBooleanFixedEvaluator(HashMap<String, Double> fixedHM) throws ContentError {
		BBase ret = null;
		if (root instanceof BooleanParseTreeNode) {
			ret = new BBase(((BooleanParseTreeNode) root).makeEvaluable(fixedHM));
		} else {
			throw new ContentError("Seeking a boolean evaluator, but not a boolean node? " + root);
		}
		return ret;
	}
	
	

	public DoubleEvaluator makeFloatEvaluator() throws ContentError {
		DBase ret = null;
		if (root instanceof DoubleParseTreeNode) {
			ret = new DBase(((DoubleParseTreeNode) root).makeEvaluable(null));
		}
		return ret;
	}
	
	
	public DoubleEvaluator makeFloatFixedEvaluator(HashMap<String, Double> fixedHM) throws ContentError {
		DBase ret = null;
		if (root instanceof DoubleParseTreeNode) {
			ret = new DBase(((DoubleParseTreeNode) root).makeEvaluable(fixedHM));
		}
		return ret;
	}
	
 
	
	
	public Dimensional evaluateDimensional(HashMap<String, Dimensional> adml) throws ContentError {
		return root.evaluateDimensional(adml);
	}

	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		 return root.getDimensionality(dimHM);
	}

	public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError {
 		if (root instanceof BooleanParseTreeNode) {
			((BooleanParseTreeNode)root).checkDimensions(dimHM);
		} else {
			E.error("Can't check dims of expression");
		}
	}

	public void substituteVariables(HashMap<String, String> varHM) throws ContentError {
		root.substituteVariables(varHM);
	}

	public String toExpression() throws ContentError {
		return root.toExpression();
	}

	public void visitAll(ExpressionVisitor ev) throws ContentError {
		root.doVisit(ev);
	}


}

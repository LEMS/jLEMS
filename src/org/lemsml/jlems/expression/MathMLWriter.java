package org.lemsml.jlems.expression;

import org.lemsml.jlems.sim.ContentError;

public class MathMLWriter implements ExpressionVisitor {

	String wkText;
	 
	
	public String generateMathML(ParseTree pt) throws ContentError {
		pt.visitAll(this);
		return wkText;
	}
	
	
	
	// TODO - work in progress
	public ExpressionVisitor visitNode(ExpressionVisitor evl, ParseTreeNode ptn, ExpressionVisitor evr) {
		String sl = getText(evl);
		String sr = getText(evr);
		
		String op = "";
		if (ptn instanceof DivideNode) {
			op = "/";
		} else if (ptn instanceof PlusNode) {
			op = "+";
		}
		
		MathMLWriter mmw = new MathMLWriter();
		mmw.wkText = "(" + sl + op + sr + ")";
		return mmw;
	}
	
	
	private String getText(ExpressionVisitor ev) {
		String ret = "";
		if (ev instanceof MathMLWriter) {
			ret = ((MathMLWriter)ev).wkText;
		}
		return ret;
	}
	
}

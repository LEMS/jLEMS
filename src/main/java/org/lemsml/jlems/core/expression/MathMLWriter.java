package org.lemsml.jlems.core.expression;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

public class MathMLWriter implements ExpressionVisitor {

	StringBuilder sb;
	 
	int depth = 0;
	 
	
	
	public String serialize(ParseTree pt) throws ContentError {
		MathMLWriter mw = new MathMLWriter();
		return mw.generateMathML(pt);
	}
	
	
	private String generateMathML(ParseTree pt) throws ContentError {
		sb = new StringBuilder();
		depth = 0;
		pt.visitAll(this);
		return sb.toString();
	}
	
	private String indent() {
		String ret = "";
		for (int i = 0; i < depth; i++) {
			ret += " ";
		}
		return ret;
	}
	
	
	@Override
	public void visitVariable(String svar) {
		sb.append(indent() + "<ci>" + svar + "</ci>\n");
	}


	@Override
	public void visitOrNode(OrNode orNode) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void visitFunctionNode(String fname, DoubleParseTreeNode argEvaluable) throws ContentError {
		sb.append(indent() + "<apply>\n");
		depth += 1;
		sb.append(indent() + "<" + fname + "/>\n");
		argEvaluable.doVisit(this);
		depth -= 1;
		sb.append(indent() + "</apply>\n");
	}


	@Override
	public void visitConstant(double dval) {
		sb.append(indent() + "<cn>" + dval + "</cn>\n");
	}


	private void visitOp(String opname, DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		sb.append(indent() + "<apply>\n");
		depth += 1;
		sb.append(indent() + "<" + opname + "/>\n");
		if (leftEvaluable != null) {
			leftEvaluable.doVisit(this);
		}
		 rightEvaluable.doVisit(this);
		depth -= 1;
		sb.append(indent() + "</apply>\n");
	}
	

	@Override
	public void visitPlusNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp("plus", leftEvaluable, rightEvaluable);
	}


	@Override
	public void visitTimesNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp("times", leftEvaluable, rightEvaluable);
	}


	@Override
	public void visitPowerNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp("power", leftEvaluable, rightEvaluable);
	}


	@Override
	public void visitMinusNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp("minus", leftEvaluable, rightEvaluable);
	}


	@Override
	public void visitUnaryMinusNode(DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp("minus", null, rightEvaluable);
	}


	@Override
	public void visitDivideNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp("divide", leftEvaluable, rightEvaluable);
	}
	
	@Override
	public void visitModuloNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp("modulo", leftEvaluable, rightEvaluable);
	}

	
	
	

	@Override
	public void visitNotEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		// TODO Auto-generated method stub
		E.missing();
	}

	@Override
	public void visitAndNode(BooleanParseTreeNode leftEvaluable, BooleanParseTreeNode rightEvaluable) {
		// TODO Auto-generated method stub	
		E.missing();
	}
	 

	@Override
	public void visitLessThanOrEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		// TODO Auto-generated method stub	
		E.missing();
	}
 
	@Override
	public void visitLessThanNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		// TODO Auto-generated method stub	
		E.missing();
	}

	@Override
	public void visitGreaterThanOrEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		// TODO Auto-generated method stub
		E.missing();
	}


	@Override
	public void visitGreaterThanNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		E.missing();
	}


	@Override
	public void visitEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		E.missing();
	}
	
}

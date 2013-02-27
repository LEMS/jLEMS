package org.lemsml.jlems.core.expression;

import org.lemsml.jlems.core.sim.ContentError;

public interface ExpressionVisitor {

	 
	void visitVariable(String svar);

	void visitOrNode(OrNode orNode);

	void visitFunctionNode(String fname, DoubleParseTreeNode argEvaluable) throws ContentError;

	void visitConstant(double dval);

	void visitAndNode(BooleanParseTreeNode leftEvaluable, BooleanParseTreeNode rightEvaluable);

	void visitTimesNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError;

	void visitPowerNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError;

	void visitMinusNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError;

	void visitUnaryMinusNode(DoubleParseTreeNode rightEvaluable) throws ContentError;

	void visitDivideNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError;

	void visitNotEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable);

	void visitModuloNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError;

	void visitLessThanOrEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable);

	void visitLessThanNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable);

	void visitPlusNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError;

	void visitGreaterThanOrEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable);

	void visitGreaterThanNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable);

	void visitEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable);
	
	
}

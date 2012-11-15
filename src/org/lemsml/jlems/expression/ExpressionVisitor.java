package org.lemsml.jlems.expression;

public interface ExpressionVisitor {

	
	
	public ExpressionVisitor visitNode(ExpressionVisitor evl, ParseTreeNode ptn, ExpressionVisitor evr);
	
	
}

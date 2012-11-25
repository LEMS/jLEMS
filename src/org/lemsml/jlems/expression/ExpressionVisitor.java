package org.lemsml.jlems.expression;

public interface ExpressionVisitor {

	
	
	ExpressionVisitor visitNode(ExpressionVisitor evl, ParseTreeNode ptn, ExpressionVisitor evr);
	
	
}

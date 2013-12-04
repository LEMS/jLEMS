package org.lemsml.jlems.core.type.dynamics;

import org.lemsml.jlems.core.expression.ParseTree;

public interface IVisitable{
	ParseTree getParseTree();
	String getValueExpression();
}
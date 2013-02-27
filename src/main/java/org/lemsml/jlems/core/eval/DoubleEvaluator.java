package org.lemsml.jlems.core.eval;

import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.jlems.core.run.DoublePointer;
import org.lemsml.jlems.core.run.RuntimeError;

public interface DoubleEvaluator {

	
	double evalD(HashMap<String, Double> valHM);

	double evalptr(HashMap<String, DoublePointer> varHM) throws RuntimeError;

	double evalptr(HashMap<String, DoublePointer> varHM, HashMap<String, DoublePointer> v2hm);

	DoubleEvaluator makePrefixedCopy(String pfx, HashSet<String> indHS);

	void substituteVariableWith(String vnm, String pth);

	boolean variablesIn(HashSet<String> known);

	String getExpressionString();
	
	
}

package org.lemsml.jlems.core.eval;

import java.util.HashMap;

import org.lemsml.jlems.core.run.DoublePointer;
import org.lemsml.jlems.core.run.RuntimeError;

public interface BooleanEvaluator {

	boolean evalB(HashMap<String, Double> valHM);

	BooleanEvaluator makeCopy();

	Boolean evalptr(HashMap<String, DoublePointer> varHM) throws RuntimeError;
	
	
	
}

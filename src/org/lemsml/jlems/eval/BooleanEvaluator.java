package org.lemsml.jlems.eval;

import java.util.HashMap;

import org.lemsml.jlems.run.DoublePointer;
import org.lemsml.jlems.run.RuntimeError;

public interface BooleanEvaluator {

	boolean evalB(HashMap<String, Double> valHM);

	BooleanEvaluator makeCopy();

	Boolean evalptr(HashMap<String, DoublePointer> varHM) throws RuntimeError;
	
	
	
}

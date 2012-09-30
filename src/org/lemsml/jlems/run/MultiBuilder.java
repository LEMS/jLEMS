package org.lemsml.jlems.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.eval.DBase;
import org.lemsml.jlems.expression.DoubleEvaluable;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.RuntimeError;

public class MultiBuilder extends AbstractChildBuilder {

	
	int number;
	ComponentBehavior componentBehavior;
	
	ArrayList<ExpressionDerivedVariable> edvAL = new ArrayList<ExpressionDerivedVariable>();
 	
	public MultiBuilder(int n, ComponentBehavior cb) {
		 number = n;
		 componentBehavior = cb;
 	}
	
	
	public void childInstantiate(StateInstance par) throws ContentError, ConnectionError, RuntimeError {
 		
		MultiInstance mi = new MultiInstance(componentBehavior.typeName, "");
		for (int i = 0; i < number; i++) {
			StateInstance sr = componentBehavior.newInstance();
 			sr.setNewVariable("index", i);
			
 			HashMap<String, DoublePointer> varHM = sr.getVariables();
 			HashMap<String, DoublePointer> svHM = par.getVariables();
 			
			for (ExpressionDerivedVariable edv: edvAL) {
				edv.augment(varHM, svHM);
			
				String ea = edv.getExposeAs();
				if (ea != null) {
					double d = edv.evalptr(varHM, svHM);
					sr.setNewVariable(ea, d);
				//	E.info("set new var " + ea + " " + d);
				}
			}
			
			mi.add(sr);
		}
		par.addMultiInstance(mi);
	}
	
	public boolean isInstantiator() {
		return true;
	}

 

	public void addAssignment(String property, DoubleEvaluable de) throws ContentError {
		ExpressionDerivedVariable edv = new ExpressionDerivedVariable(property, new DBase(de.makeFixed(null)));
		edvAL.add(edv);
	}

	public void addAssignment(String property, DoubleEvaluable de, String exposeAs) throws ContentError {
		ExpressionDerivedVariable edv = new ExpressionDerivedVariable(property, new DBase(de.makeFixed(null)));
		edv.setInstanceExposeAs(exposeAs);
		edvAL.add(edv);
	}

	public void setIndexVariable(String indexVariable) {
		// MUSTDO
	}


	@Override
	public void consolidateComponentBehaviors() {
		componentBehavior = componentBehavior.getConsolidatedComponentBehavior("(multi)");
	}
	
	
	
}

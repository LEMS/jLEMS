package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.eval.DBase;
import org.lemsml.jlems.core.eval.DoubleEvaluator;
import org.lemsml.jlems.core.expression.DoubleParseTreeNode;
import org.lemsml.jlems.core.sim.ContentError;

public class MultiBuilder extends AbstractChildBuilder {

	
	int number;
	StateType stateType;
	
	ArrayList<ExpressionDerivedVariable> edvAL = new ArrayList<ExpressionDerivedVariable>();
 	
	public MultiBuilder(int n, StateType cb) {
		super();
		number = n;
		 stateType = cb;
 	}
	
	
	public void childInstantiate(StateInstance par) throws ContentError, ConnectionError, RuntimeError {
 		
		String tnm = stateType.typeName;
		
		// MultiInstance mi = new MultiInstance(stateType.typeName, "");
		for (int i = 0; i < number; i++) {
			StateInstance sr = stateType.newInstance();
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
			par.addListChild(tnm, "", sr);
			
//			mi.add(sr);
		}
		// par.addMultiInstance(mi);
	}
	
	public boolean isInstantiator() {
		return true;
	}

 

	public void addAssignment(String property, DoubleParseTreeNode de) throws ContentError {
		ExpressionDerivedVariable edv = new ExpressionDerivedVariable(property, new DBase(de.makeEvaluable(null)));
		edvAL.add(edv);
	}

	public void addAssignment(String property, DoubleEvaluator de, String exposeAs) throws ContentError {
		// TODO copy de?
		ExpressionDerivedVariable edv = new ExpressionDerivedVariable(property, de);
		edv.setInstanceExposeAs(exposeAs);
		edvAL.add(edv);
	}

	public void setIndexVariable(String indexVariable) {
		// MUSTDO
	}


	@Override
	public void consolidateStateTypes() {
		stateType = stateType.getConsolidatedStateType("(multi)");
	}
	
	
	
}

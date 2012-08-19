package org.lemsml.run;

import java.util.ArrayList;

import org.lemsml.util.E;

public class Flattener {

	
	ArrayList<PathDerivedVariable> pdvA = new ArrayList<PathDerivedVariable>();
	ArrayList<ExpressionDerivedVariable> edvA = new ArrayList<ExpressionDerivedVariable>();
	ArrayList<VariableROC> rocA = new ArrayList<VariableROC>();
	ArrayList<String> svA = new ArrayList<String>();
	
	
	public void add(PathDerivedVariable pdv) {
		pdvA.add(pdv);
	}

	public void add(ExpressionDerivedVariable edv) {
		edvA.add(edv);
	}

	public void add(VariableROC vroc) {
		rocA.add(vroc);	
	}

	public void addStateVariable(String sv) {
		svA.add(sv);
	}

	public void resolvePaths() {
		E.warning("may need to resolve paths??");
	}

	public void exportTo(ComponentBehavior ret) {
 		for (String sv : svA) {
 			ret.addStateVariable(sv);
 		}
 		for (PathDerivedVariable pdv : pdvA) {
 			ret.addPathDerivedVariable(pdv);
 		}
 		for (ExpressionDerivedVariable edv : edvA) {
 			ret.addExpressionDerivedVariable(edv);
 		}
 		for (VariableROC vr : rocA) {
 			ret.addVariableROC(vr);
 		}
	}

	
	
	
	
}

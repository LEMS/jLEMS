package org.lemsml.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.eval.DBase;
import org.lemsml.util.E;
import org.lemsml.util.RuntimeError;

public class ComponentRegime {

	String name;
	String typeName;
	
	boolean initial = false;
	
	
	ArrayList<String> vars = new ArrayList<String>();
	
	ArrayList<String> indeps = new ArrayList<String>();
	
	ArrayList<FixedQuantity> fixeds = new ArrayList<FixedQuantity>();
	
	ArrayList<PathDerivedVariable> pathderiveds = new ArrayList<PathDerivedVariable>();
	
	ArrayList<ExpressionDerivedVariable> exderiveds = new ArrayList<ExpressionDerivedVariable>();
	
	ArrayList<VariableROC> rates = new ArrayList<VariableROC>();
	
	HashMap<String, ActionBlock> eventHM = new HashMap<String, ActionBlock>();
	
	ArrayList<ActionBlock> initBlocks = new ArrayList<ActionBlock>();
	
	ArrayList<ActionBlock> entryBlocks = new ArrayList<ActionBlock>();
	
	ArrayList<ConditionAction> conditionResponses = new ArrayList<ConditionAction>();
	
	ArrayList<String> outPorts = new ArrayList<String>();
 
	ArrayList<String> svars = new ArrayList<String>();
	
	ComponentBehavior parent;
	
	
	public ComponentRegime(ComponentBehavior cb, String snm, String tnm) {
		parent = cb;
		name = snm;
		typeName = tnm;
		vars.add("t"); // TODO should just have one DoublePointer to t 
	}
	
	
    @Override
	public String toString() {
		return " Component regime " + name + " in type " + typeName;
	}
	
	
	public String getName() {
		return name;
	}
	

	
	
	public RegimeStateInstance newInstance(StateInstance par) throws RuntimeError {
		
		RegimeStateInstance uin = new RegimeStateInstance(this, par);
	 	
		uin.setVariables(vars);
		
		uin.setIndependents(indeps);
		
		uin.setFixeds(fixeds);
		
		uin.setExpressionDerived(exderiveds);

		for (String s : eventHM.keySet()) {
			uin.addInputPort(s, eventHM.get(s));
		}
		
		for (String s : outPorts) {
			uin.addOutputPort(par.getOrMakeOutputPort(s));
		}

		 
	  
		for (PathDerivedVariable pdv : pathderiveds) {
		   E.missing("" + pdv);
		   /*
			if (pdv.isSimple()) {
				uin.addPathStateInstance(pdv.getPath(), pdv.getTargetState(uin));
			} else {
				uin.addPathStateArray(pdv.getPath(), pdv.getTargetArray(uin), pdv.getBits(), pdv.getIsSlice());
			}
		    */
		}
	  
		
		for (ActionBlock ab : initBlocks) {
			ab.run(uin);
		}
		return uin;
	}

	

	public void enter(RegimeStateInstance rsi) throws RuntimeError {
		for (ActionBlock eb : entryBlocks) {
			eb.run(rsi);
		}
	}

	
	public void advance(RegimeStateInstance rsi, StateRunnable parent, double t, double dt) throws RuntimeError {
	//  StateInstance psi = rsi.getParent();
		HashMap<String, DoublePointer> varHM = rsi.getVarHM();
	//	varHM.put("t", t);
		
		HashMap<String, DoublePointer> pvarHM = parent.getVariables();
		
		if (indeps != null) {
			for (String s : indeps) {
				varHM.get(s).set(parent.getVariable(s));
			}
		}
		
		
		for (PathDerivedVariable pdv : pathderiveds) {
			E.missing("" + pdv);
			//		varHM.put(pdv.varname, pdv.eval(rsi));
		}
		
		for (ExpressionDerivedVariable edv : exderiveds) {
			varHM.get(edv.varname).set(edv.evalptr(varHM));
		}
		
		for (VariableROC vroc : rates) {
			varHM.get(vroc.varname).set(varHM.get(vroc.varname).get() + dt * vroc.evalptr(varHM, pvarHM));
		}
	 
		for (ConditionAction ca : conditionResponses) {
			Boolean b = ca.evalptr(varHM);
			if (b) {
				ca.getAction().run(rsi);
			}
		}
	}
	

	public void addExpressionDerived(String snm, DBase db) {
		ExpressionDerivedVariable edv = new ExpressionDerivedVariable(snm, db);
		exderiveds.add(edv);
	}
	
	public void addPathDerived(String snm, String path, String rf, String oa) {
		PathDerivedVariable pdv = new PathDerivedVariable(snm, path, rf, oa);
		pathderiveds.add(pdv);
	}
	
	public void addFixed(String snm, double d) {
		fixeds.add(new FixedQuantity(snm, d));
	}
	
	
	public void addIndependentVariable(String vnm) {
		indeps.add(vnm);
	}

	public void addRate(String name, DBase db) {
		rates.add(new VariableROC(name, db));
	}

	public void addEventResponse(EventAction er) {
		eventHM.put(er.getPortName(), er.getAction());	
	}

	
	public void addConditionResponce(ConditionAction cr) {
		conditionResponses.add(cr);
	 	
	}

	public void addInitialization(ActionBlock ab) {
		initBlocks.add(ab);
	}
	
	public void addEntry(ActionBlock ab) {
		entryBlocks.add(ab);
	}
	
	public void fix() {
		
		HashSet<String> vHS = new HashSet<String>();
		for (VariableROC vroc : rates) {
			String vnm = vroc.getVarName();
			vars.add(vnm);
			vHS.add(vnm);
		}
		for (String s : svars) {
			if (!vHS.contains(s)) {
				vars.add(s);
				vHS.add(s);
			}
		}
 
		for (PathDerivedVariable pdv : pathderiveds) {
			String s = pdv.getVarName();
			if (!vHS.contains(s)) {
				vars.add(s);
				vHS.add(s);
			}
		}
	 
		for (ActionBlock ab : entryBlocks) {
			ab.addVarsTo(vars);
			ab.addPortsTo(outPorts);
		}
		
		for (ActionBlock ab : initBlocks) {
			ab.addVarsTo(vars);
			ab.addPortsTo(outPorts);
		}
		 
		
		for (String s : eventHM.keySet()) {
			ActionBlock ea = eventHM.get(s);
			ea.addVarsTo(vars);
			ea.addPortsTo(outPorts);
		}
		for (ConditionAction cr : conditionResponses) {
			ActionBlock ea = cr.getAction();
			ea.addVarsTo(vars);
			ea.addPortsTo(outPorts);
		}
		
	}


	public void setInitial(boolean b) {
		initial = b;
	}
	
	public boolean isInitial() {
		return initial;
	}

	public void addStateVariable(String name) {
		svars.add(name);
	}
	  
	

}

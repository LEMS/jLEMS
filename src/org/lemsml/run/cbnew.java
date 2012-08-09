package org.lemsml.run;

public class cbnew {

}
/*
package org.neuroml.lems.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.neuroml.lems.behavior.Build;
import org.neuroml.lems.eval.DBase;
import org.neuroml.lems.type.Component;
import org.neuroml.lems.util.E;

public class ComponentDynamics {

	String cptid;
	String typeName;
	
	ArrayList<String> vars = new ArrayList<String>();
	
	ArrayList<String> indeps = new ArrayList<String>();
	
	ArrayList<FixedQuantity> fixeds = new ArrayList<FixedQuantity>();
	
	ArrayList<PathDerivedVariable> pathderiveds = new ArrayList<PathDerivedVariable>();
	
	ArrayList<ExpressionDerivedVariable> exderiveds = new ArrayList<ExpressionDerivedVariable>();
	
	ArrayList<VariableROC> rates = new ArrayList<VariableROC>();
	
	// some state varaibles won't have a vroc entry, but we still want them as they may figure in 
	// an assignment or a regime 
	ArrayList<String> svars = new ArrayList<String>();
	
	HashMap<String, ActionBlock> eventHM = new HashMap<String, ActionBlock>();
	
	ArrayList<ActionBlock> initBlocks = new ArrayList<ActionBlock>();
	
	ArrayList<ConditionAction> conditionResponses = new ArrayList<ConditionAction>();
	
	ArrayList<String> outPorts = new ArrayList<String>();

	ArrayList<String> inPorts = new ArrayList<String>();
	
	ArrayList<KScheme> kschemes = new ArrayList<KScheme>();
	
	
	HashMap<String, ComponentDynamics> childHM = new HashMap<String, ComponentDynamics>();
	
	HashMap<String, MultiComponentDynamics> multiHM = new HashMap<String, MultiComponentDynamics>();
	
	HashMap<String, String> attSetHM = new HashMap<String, String>();

	HashMap<String, String> textParamHM = new HashMap<String, String>();
	
	boolean hasRegimes = false;
	HashMap<String, ComponentRegime> regimeHM = new HashMap<String, ComponentRegime>();
	
	
	RunConfig runConfig = null;
	
	
	ArrayList<BuilderElement> builders;
	
	boolean hasBuilds = false;
	
	
	
	public ComponentDynamics(String sid, String tnm) {
		cptid = sid;
		typeName = tnm;
		vars.add("t"); // TODO should just have one DoublePointer to t 
	}
	
	public String toString() {
		return " Component behavior " + cptid + " of type " + typeName;
	}
	
	public String getComponentID() {
		return cptid;
	}
	
	
	public StateRunnable newInstance() {
		StateRunnable ret = null;
		
	//	E.srcinfo("instantiating " + this + " hasBuilds=" + hasBuilds);
		
		if (hasBuilds) {
			for (BuilderElement be : builders) {
				if (be.isInstantiator()) {
					ret = be.buildInstance(getComponentID(), typeName);
				}
			}
		}
		if (ret == null) {
			ret = newDefaultInstance();
		}
		
		if (hasBuilds) {
			for (BuilderElement be : builders) {
				if (be.isInstantiator()) {
					 // already done its bit
				} else {
					ret.addPostBuilder(be);
				}
			}
		}
		
		
		return ret;
	}

	
	
	public StateInstance newDefaultInstance() {
		
		StateInstance uin = new StateInstance(this);
		
		uin.setVariables(vars);
		
		uin.setIndependents(indeps);
		
		uin.setFixeds(fixeds);
		
		uin.setExpressinDerived(exderiveds);

		for (String s : eventHM.keySet()) {
			uin.addInputPort(s, eventHM.get(s));
		}
		
		for (String s : outPorts) {
			uin.addOutputPort(s);
		}
	
		for (String s : inPorts) {
			uin.checkAddInputPort(s);
		}

		for (String s : childHM.keySet()) {
			uin.addChild(s, (StateInstance)(childHM.get(s).newInstance()));
		}
		
		for (String s : multiHM.keySet()) {
			uin.addMulti(s, multiHM.get(s).newInstance());
		}
		
		for (String s : attSetHM.keySet()) {
			uin.addMulti(s, new MultiInstance(attSetHM.get(s), "attachment"));
		}
		
		
		for (PathDerivedVariable pdv : pathderiveds) {
			if (pdv.isSimple()) {
				uin.addPathStateInstance(pdv.getPath(), pdv.getTargetState(uin));
			} else {
				uin.addPathStateArray(pdv.getPath(), pdv.getTargetArray(uin), pdv.getBits(), pdv.getIsSlice());
			}
		}
		
		for (KScheme ks : kschemes) {
			String ss = ks.getNodesName();
			String sr = ks.getEdgesName();
			
			MultiInstance smi = uin.getMultiInstance(ss);
			MultiInstance rmi = uin.getMultiInstance(sr);
			
			KSchemeInst ksi = ks.makeInstance(smi, rmi);
			
		    uin.addKSchemeInst(ksi);
		}
		
		
		if (hasRegimes) {
			for (String srn : regimeHM.keySet()) {
				ComponentRegime cr = regimeHM.get(srn);
				RegimeStateInstance rsi = cr.newInstance(uin);
				uin.addRegime(rsi);
			}
		}
		
		
		for (ActionBlock ab : initBlocks) {
			ab.run(uin);
		}
		
		if (hasRegimes) {
			uin.initRegime();
		}
		
		return uin;
	}

	
	
	
	public void advance(StateInstance uin, StateRunnable parent, double t, double dt) {
 		HashMap<String, DoublePointer> varHM = uin.getVarHM();
		varHM.get("t").set(t);
		
		if (indeps != null) {
			for (String s : indeps) {
				varHM.get(s).set(parent.getVariable(s));
			}
		}
		
		
		for (PathDerivedVariable pdv : pathderiveds) {
			varHM.get(pdv.varname).set(pdv.eval(uin));
		}
		
		for (ExpressionDerivedVariable edv : exderiveds) {
			varHM.get(edv.varname).set(edv.evalptr(varHM));
		}
		
		for (VariableROC vroc : rates) {
			varHM.get(vroc.varname).set(varHM.get(vroc.varname).get() + dt * vroc.evalptr(varHM));
		}
	 
		for (ConditionAction ca : conditionResponses) {
			Boolean b = ca.evalptr(varHM);
			if (b) {
				ca.getAction().run(uin);
			}
		}
	}
	

	public void addExpressionDerived(String snm, DBase db) {
		ExpressionDerivedVariable edv = new ExpressionDerivedVariable(snm, db);
		exderiveds.add(edv);
	}
	
	public void addPathDerived(String snm, String path) {
		PathDerivedVariable pdv = new PathDerivedVariable(snm, path);
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
		
		for (ActionBlock ab : initBlocks) {
			ab.addVarsTo(vars);
			ab.addPortsTo(outPorts);
		}
		
		
		for (String s : eventHM.keySet()) {
			ActionBlock ea = eventHM.get(s);
			if (ea == null) {
				// OK - null action block means there is no action in this state instance, but 
				// there could be in sub-regimes
			} else {
				ea.addVarsTo(vars);
				ea.addPortsTo(outPorts);
			}  
		}
		for (ConditionAction cr : conditionResponses) {
			ActionBlock ea = cr.getAction();
			ea.addVarsTo(vars);
			ea.addPortsTo(outPorts);
		}
		
	}

	public void addChildComponentDynamics(String s, ComponentDynamics chb) {
		childHM.put(s, chb);
	}

	public void addMultiComponentDynamics(String s, MultiComponentDynamics mcb) {
		multiHM.put(s , mcb);
		
	}

	public void addAttachmentSet(String name, String typeName) {
		attSetHM.put(name, typeName);
	}

	public void addKScheme(KScheme scheme) {
		kschemes.add(scheme);
	}

	public void addRunConfig(Component targetComponent, double step, double total) {
		runConfig = new RunConfig(targetComponent, step, total);
	}

	public RunConfig getRunConfig() {
		return runConfig;
	}

	public void addBuilder(BuilderElement b) {
		if (builders == null) {
			builders = new ArrayList<BuilderElement>();
		}
		builders.add(b);
		hasBuilds = true;
	}

 
	public void addComponentRegime(ComponentRegime crb) {
		hasRegimes = true;
		regimeHM.put(crb.getName(), crb);
		
	}
 
	public void addTextParam(String tnm, String value) {
		 textParamHM.put(tnm, value);
	}

	public void addInputPort(String name) {
		inPorts.add(name);
	}

	public void addStateVaraible(String name) {
		svars.add(name);
	}
 

}

*/

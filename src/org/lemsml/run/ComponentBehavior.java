package org.lemsml.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.lemsml.eval.BBase;
import org.lemsml.eval.DBase;
import org.lemsml.type.Component;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.RuntimeError;

public class ComponentBehavior {

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
	
	HashMap<String, ComponentBehavior> refHM = new HashMap<String, ComponentBehavior>();
	
	HashMap<String, ComponentBehavior> childHM = new HashMap<String, ComponentBehavior>();
	
	ArrayList<String> multiHMNames = new ArrayList<String>();
	HashMap<String, MultiComponentBehavior> multiHM = new HashMap<String, MultiComponentBehavior>();
	
	HashMap<String, String> attSetHM = new HashMap<String, String>();

	HashMap<String, String> textParamHM = new HashMap<String, String>();
	
	boolean hasRegimes = false;
	HashMap<String, ComponentRegime> regimeHM = new HashMap<String, ComponentRegime>();
	
	HashSet<String> exposedNames = new HashSet<String>();
        
	HashMap<String, String> exposedMap = new HashMap<String, String>();
	
	RunConfig runConfig = null;
	
	ArrayList<Builder> builders;
	
	boolean hasBuilds = false;
	
	ArrayList<String> isets;
	
	ArrayList<String> ipairsets;
	
	// true if type def says this should be consolidated
	boolean simultaneous = false;
	
	// set true once done
	boolean consolidated = false;
	
	
	ComponentBehavior consolidatedCB = null;
	
	
	HashMap<String, Double> der1, der2, der3, der4, vwk,  val1, val2, val3, val4;
	
	
	
	public ComponentBehavior(String sid, String tnm) {
		cptid = sid;
		typeName = tnm;
		vars.add("t"); // TODO should just have one DoublePointer to t 
	}
	
    @Override
	public String toString() {
		return "ComponentBehavior, id=" + cptid + ", Type=" + typeName;
	}
	
	public String getComponentID() {
		return cptid;
	}

    public ArrayList<VariableROC> getRates() {
        return rates;
    }

    public ArrayList<String> getVars() {
            return vars;
    }

    public ArrayList<String> getIndeps() {
            return indeps;
    }

    public ArrayList<String> getSvars() {
            return svars;
    }

    public HashMap<String, String> getAttachmentSet() {
        return attSetHM;
    }

    public ArrayList<ExpressionDerivedVariable> getExderiveds() {
            return exderiveds;
    }

    public ArrayList<PathDerivedVariable> getPathderiveds() {
            return pathderiveds;
    }
	
	public StateInstance newInstance() throws ContentError, ConnectionError {
	 
		StateInstance uin = new StateInstance(this);
		
		uin.setVariables(vars);
		
		uin.setExposedVariables(exposedNames);
		
		uin.setIndependents(indeps);
		
		uin.setFixeds(fixeds);
		
		uin.setExpressionDerived(exderiveds);

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
	 	
		
		for (String s : multiHMNames) {
			MultiInstance mi = multiHM.get(s).newInstance();
			mi.setKnownAs(s);
			uin.addMultiInstance(mi);
		}
		
		for (String s : attSetHM.keySet()) {
			uin.addAttachmentSet(s, new MultiInstance(attSetHM.get(s), s));
		}

		if (isets != null) {
			for (String s : isets) {
				uin.addInstanceSet(s);
			}
		}
		
		if (ipairsets != null) {
			for (String s : ipairsets) {
				uin.addInstancePairSet(s);
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
				try {
					ComponentRegime cr = regimeHM.get(srn);
					RegimeStateInstance rsi = cr.newInstance(uin);
					uin.addRegime(rsi);
				} catch (RuntimeError re) {
					throw new ContentError(re.getMessage());
				}
			}
		}
		
	 
	
	 
		return uin;
	}
	
	
	
	public void build(StateInstance uin) throws ContentError, ConnectionError {
		if (hasBuilds) {
			for (Builder be : builders) { 
				if (be.isChildInstantiator()) {
					be.childInstantiate(uin);
				}
				if (be.isPostBuilder()) {
					be.postBuild(uin);
				}
			}
		}
		uin.doneBuild();
	}
	
	 
	
	
	
	public void initialize(StateInstance uin, StateRunnable parent, boolean includeDerivedVariables) throws RuntimeError, ContentError {
 		HashMap<String, DoublePointer> varHM = uin.getVarHM();

        try {
		if (indeps != null) {
			for (String s : indeps) {
                //E.info("-- Got val of "+parent.getVariable(s)+" for "+s+" in parent "+parent.getID()+" of "+getComponentID());
				varHM.get(s).set(parent.getVariable(s));
			}
		}

        if (includeDerivedVariables) {
            
            for (PathDerivedVariable pdv : pathderiveds) {
                varHM.get(pdv.varname).set(pdv.eval(uin));
            }

            for (ExpressionDerivedVariable edv : exderiveds) {
                varHM.get(edv.varname).set(edv.evalptr(varHM));
            }
        }

		for (ActionBlock ab : initBlocks) {
			ab.run(uin);
		}
		
		if (hasRegimes) {
			uin.initRegime();
		}
        synchronizeExposures(uin);
		uin.doneInit();

        } catch (Exception e) {
            String err = "Error when initialising " + this + " " + e;
            E.error(err);
            e.printStackTrace();
            for(PathDerivedVariable pdv : pathderiveds) {
            	E.info("" + pdv);
            }
            for (ExpressionDerivedVariable exd : exderiveds) {
            	E.info("" + exd);
            }
           
            throw new RuntimeError(err, e);
        }

	}
	
	
	
	
	public void applyPathDerived(StateInstance uin) throws ContentError {
		for (PathDerivedVariable pdv : pathderiveds) {
			if (pdv.isSimple()) {
				uin.addPathStateInstance(pdv.getPath(), pdv.getTargetState(uin));
			} else {
				uin.addPathStateArray(pdv.getPath(), pdv.getTargetArray(uin));
			}
		}
		uin.donePaths();
	}

	
	
	public void evaluate(StateInstance uin, StateRunnable parent) throws RuntimeError, ContentError {
		HashMap<String, DoublePointer> varHM = uin.getVarHM();
		evalDerived(uin, varHM, parent);
		synchronizeExposures(uin);
	}
	
	
	private void evalDerived(StateInstance uin, HashMap<String, DoublePointer> varHM, StateRunnable parent) throws RuntimeError, ContentError {
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
	}
	
	
	public void eulerAdvance(StateInstance uin, StateRunnable parent, double t, double dt) throws RuntimeError, ContentError {
 		HashMap<String, DoublePointer> varHM = uin.getVarHM();
		varHM.get("t").set(t);
		
		evalDerived(uin, varHM, parent);
		
		for (VariableROC vroc : rates) {
			varHM.get(vroc.varname).set(varHM.get(vroc.varname).get() + dt * vroc.evalptr(varHM));
		}
	 
		for (ConditionAction ca : conditionResponses) {
			Boolean b = ca.evalptr(varHM);
			if (b) {
				ca.getAction().run(uin);
			}
		}
        
        synchronizeExposures(uin);
    }

	
	
	
	
	// NOTE this isn't an exact parallel to eulerAdvance because it only works
	// on consolidated (or "flattened") state instances: the derivatives mustn't 
	// depend on anything in child elements. So typically, the rk4Advance
	// will only be called on a _different_ dynamics object from the default one that 
	// works with euler advance. This consolidated dynamics brings expressions and 
	// state variables from child instances into the current instance. It is 
	// created by by getConsolidatedComponentDynamics, which generally 
	// absorbs some or all of the child objects within a new dynamics definition.
	public void rk4Advance(StateInstance uin, StateRunnable parent, double t, double dt) throws RuntimeError, ContentError {
 		HashMap<String, DoublePointer> varHM = uin.getVarHM();
		varHM.get("t").set(t);
		
		if (der1 == null) {
			
			der1 = new HashMap<String, Double>();
			der2 = new HashMap<String, Double>();
			der3 = new HashMap<String, Double>();
			der4 = new HashMap<String, Double>();
			
			vwk = new HashMap<String, Double>();
 			val1 = new HashMap<String, Double>();
			val2 = new HashMap<String, Double>();
			val3 = new HashMap<String, Double>();
			val4 = new HashMap<String, Double>();
		}
		
		
		if (indeps != null) {
			for (String s : indeps) {
				varHM.get(s).set(parent.getVariable(s));
			}
		}
		for (PathDerivedVariable pdv : pathderiveds) {
			varHM.get(pdv.varname).set(pdv.eval(uin));
		}
	 
		for (Entry<String, DoublePointer> me : varHM.entrySet()) {
			val1.put(me.getKey(), me.getValue().get());
		}
		
		for (ExpressionDerivedVariable edv : exderiveds) {
			double v = edv.eval(val1);
			// NB this is still needed here since some updates operate 
			// without equations, just by evaluating expressions in t say 
			// at each step
			varHM.get(edv.varname).set(v); 
			val1.put(edv.varname, v);
		 }
		 
		
	    evalDerivs(val1,  t,   der1);
	    
	    applyDerivs(val1, der1, t, 0.5 * dt, val2);
	    evalDerivs(val2,  t + 0.5 * dt, der2);
	    
	    applyDerivs(val1, der2, t, 0.5 * dt, val3);
	    evalDerivs(val3, t + 0.5 * dt,  der3);
	    
	    applyDerivs(val1, der3, t, dt, val4);
	    evalDerivs(val4,  t + dt, der4);
		  
		
	    for (VariableROC vroc : rates) {
	    	String sn = vroc.varname;
	    	double v0 = val1.get(sn);
	        double d = (der1.get(sn) + 2 * der2.get(sn) + 2 * der3.get(sn) + der4.get(sn)) / 6.;
	    	varHM.get(sn).set(v0 + dt * d);
	    }
	    
		
		for (ConditionAction ca : conditionResponses) {
			Boolean b = ca.evalptr(varHM);
			if (b) {
				ca.getAction().run(uin);
			}
		}
        
        synchronizeExposures(uin);
    }

	
	
	
	void evalDerivs(HashMap<String, Double> v0, double t, HashMap<String, Double> ret) throws ContentError {
		
		v0.put("t", t);
		for (ExpressionDerivedVariable edv : exderiveds) {
			v0.put(edv.varname, edv.eval(v0));
		}
	
		for (VariableROC vroc : rates) {
			ret.put(vroc.varname, vroc.eval(v0));
		}
	
	}
	
	
	void applyDerivs(HashMap<String, Double> v0, HashMap<String, Double> der, 
			double t, double delta, HashMap<String, Double> ret) {
		
		for (Entry<String, Double> me : v0.entrySet()) {
			String sk = me.getKey();
			ret.put(sk, v0.get(sk));
		}
		
		
		for (Entry<String, Double> me : der.entrySet()) {
			String sk = me.getKey();
			ret.put(sk, v0.get(sk) + delta * der.get(sk));
		}
		
	}
	

	
    private void synchronizeExposures(StateInstance uin) throws ContentError {
 		HashMap<String, DoublePointer> varHM = uin.getVarHM();
		HashMap<String, DoublePointer> expHM = uin.getExpHM();
        
		for (String s : exposedMap.keySet()) {
			String st = exposedMap.get(s);
			if (expHM.get(s) == null) {
				throw new ContentError("No such exposure: " + s);
			} else if (varHM.get(st) == null) {
				E.warning("Exposed variable '" + s + "' exposes '" + st + 
						"' but it is not defined - setting to zero");
				varHM.put(st, new DoublePointer(0));
				// throw new ContentError("No such variable " + st);
			}
			expHM.get(s).set(varHM.get(st).get());
		}
	
	}


	public void addExpressionDerived(String snm, DBase db) {
		ExpressionDerivedVariable edv = new ExpressionDerivedVariable(snm, db);
        //E.info("Adding: "+edv);
		exderiveds.add(edv);
	}

	public void addConditionDerived(String snm, DBase db, BBase cond, DBase ifFalse) {
		ConditionDerivedVariable cdv = new ConditionDerivedVariable(snm, db, cond, ifFalse);
        //E.info("Adding: "+cdv);
		exderiveds.add(cdv);
	} 
	
	public PathDerivedVariable addPathDerived(String snm, String path, String rf, String oa) {
		PathDerivedVariable pdv = new PathDerivedVariable(snm, path, rf, oa);
		pathderiveds.add(pdv);
        return pdv;
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
		addAction(er.getPortName(), er.getAction());	
	}

	public void addAction(String spn, ActionBlock a) {
		eventHM.put(spn, a);
	}
	 
	
	public void addConditionResponce(ConditionAction cr) {
		conditionResponses.add(cr);
	 	
	}

	public void addInitialization(ActionBlock ab) {
		initBlocks.add(ab);
	}

	public ArrayList<ActionBlock> getInitBlocks() {
		return initBlocks;
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

	public void addRefComponentBehavior(String s, ComponentBehavior chb) {
		refHM.put(s, chb);
	}
	
	public void addChildComponentBehavior(String s, ComponentBehavior chb) {
		childHM.put(s, chb);
	}

	public void addMultiComponentBehavior(String s, MultiComponentBehavior mcb) {
 		multiHMNames.add(s);
		multiHM.put(s, mcb);
		if (s == null) {
			(new Exception()).printStackTrace();
		}
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
	
	public void addRunConfig(RunConfig rc) {
		runConfig = rc;
	}
	

	public RunConfig getRunConfig() {
		return runConfig;
	}

	public void addBuilder(Builder b) {
		if (builders == null) {
			builders = new ArrayList<Builder>();
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

	public void addExposedVariable(String name) {
		exposedNames.add(name);
	}

	public void addExposureMapping(String from, String to) {
		if (!exposedNames.contains(to)) {
			exposedNames.add(to);
		}
		exposedMap.put(to, from);
	}

	public void addInstanceSet(String name) {
		if (isets == null) {
			isets = new ArrayList<String>();
		}
		isets.add(name);
	}
	
	
	public void addInstancePairSet(String name) {
		if (ipairsets == null) {
			ipairsets = new ArrayList<String>();
		}
		ipairsets.add(name);
	}
	

	public void setSimultaneous(boolean b) {
		simultaneous = b;
		if (b) {
			E.info("Behavior marked for consolidation " + this);
		}
		
	}

	 
	
	public ComponentBehavior getConsolidatedComponentBehavior() {
		if (consolidatedCB == null) {
			consolidatedCB = makeConsolidatedBehavior();
		}
		return consolidatedCB;
	}
	
	
	public ComponentBehavior makeConsolidatedBehavior() {
		ComponentBehavior ret = null;
		if (simultaneous) {
			E.info("Flattening " + cptid);
			ret = makeRootConsolidated();
		} else {
			ret = makeChildConsolidated();
		}
		return ret;
	}
	
	
	public ComponentBehavior makeChildConsolidated() {
		ComponentBehavior ret = makeShallowCopy();
		ret.consolidateChildren();
		return ret;
	}
	
		
	private void consolidateChildren() {
		E.info("Consolidating children in " + cptid);
		for (String sch : childHM.keySet()) {
			E.info("Child: " + sch);
			ComponentBehavior cbch = childHM.get(sch);
			ComponentBehavior fcbch = cbch.getConsolidatedComponentBehavior();
			childHM.put(sch,  fcbch);
		}
		
		for (String sm : multiHM.keySet()) {
			E.info("multi child " + sm);
			MultiComponentBehavior mcb = multiHM.get(sm);
			ArrayList<ComponentBehavior> af = new ArrayList<ComponentBehavior>();
			for (ComponentBehavior cbv : mcb.getCBs()) {
				af.add(cbv.getConsolidatedComponentBehavior());
			}
			
			MultiComponentBehavior fmcb = new MultiComponentBehavior(af);
			multiHM.put(sm, fmcb);
		}
		
		if (builders != null) {
			for (Builder b : builders) {
				b.consolidateComponentBehaviors();
			}
		}
		
	}
	
	
	
	public ComponentBehavior makeRootConsolidated() {	
		Flattener flattener = new Flattener();
		addToFlattener(null, flattener);
		
		for (String sch : childHM.keySet()) {
			ComponentBehavior cbch = childHM.get(sch);
			
			ComponentBehavior cbchflat = cbch.getConsolidatedComponentBehavior();
			cbchflat.addToFlattener(sch, flattener);
			
		}
		
		flattener.resolvePaths();
		
		ComponentBehavior ret = new ComponentBehavior(cptid, typeName);
		ret.consolidated = true;
		
		flattener.exportTo(ret);
		
		return ret;
	}

	
	
	private void addToFlattener(String pfx, Flattener fl) {
		String fullpfx = "";
		if (pfx != null && pfx.length() > 0) {
			fullpfx = pfx + "_";
		}
			
		for (String s : svars) {
			fl.addStateVariable(fullpfx + s);
		}
		for (PathDerivedVariable pdv : pathderiveds) {
			fl.add(pdv.makeFlat(fullpfx));
		}
		for (ExpressionDerivedVariable edv : exderiveds) {
			fl.add(edv.makeFlat(fullpfx));
		}
		for (VariableROC vroc : rates) {
			fl.add(vroc.makeFlat(fullpfx));
		}
	}
	
	
	public void addStateVariable(String s) {
		svars.add(s);
	}
	
	public void addPathDerivedVariable(PathDerivedVariable pdv) {
		pathderiveds.add(pdv);
	}
	
	public void addExpressionDerivedVariable(ExpressionDerivedVariable edv) {
		exderiveds.add(edv);
	}
	
	public void addVariableROC(VariableROC vroc) {
		rates.add(vroc);
	}
	
	
	public boolean hasPropertyString(String sp) {
        boolean ret = false;
		if (refHM.containsKey(sp)) {
            if (refHM.get(sp)!=null && refHM.get(sp).getComponentID()!=null)
                ret = true;
		} else if (textParamHM.containsKey(sp)) {
			ret = textParamHM.get(sp)!=null;
		}
        return ret;
	}

	public String getPropertyStringValue(String sp) throws ContentError {
		String ret = null;
		if (refHM.containsKey(sp)) {
			ret = refHM.get(sp).getComponentID();
		} else if (textParamHM.containsKey(sp)) {
			ret = textParamHM.get(sp);
		} else {
			throw new ContentError("Can't get property " + sp + " on " + this);
		}
		return ret;
	}

	
	// OR: could do recursive consolidation from the bottom so there's only ever one layer to consolidate here?



	
	public ComponentBehavior makeShallowCopy() {
		ComponentBehavior ret = new ComponentBehavior(cptid, typeName);
	
		for (String s : childHM.keySet()) {
			ret.addChildComponentBehavior(s, childHM.get(s)); //.makeShallowCopy());
		}
		
		// TODO cloning refs?
		for (String s : refHM.keySet()) {
			ret.addRefComponentBehavior(s, refHM.get(s)); // .makeShallowCopy());
		}
		
		for (String s : multiHM.keySet()) {
			ret.addMultiComponentBehavior(s, multiHM.get(s)); // .makeCopy());
		}
		
		
		for (VariableROC vroc : rates) {
			ret.addVariableROC(vroc.makeCopy());
		}
		for (String s : svars) {
			ret.addStateVariable(s);
		}
		
		for (PathDerivedVariable pdv : pathderiveds) {
			ret.addPathDerivedVariable(pdv);
		}
		
		for (ExpressionDerivedVariable edv : exderiveds) {
			ret.addExpressionDerivedVariable(edv);
		}
		
		for (String s : eventHM.keySet()) {
			ret.addAction(s, eventHM.get(s).makeCopy());
		}
		
		for (ActionBlock ab : initBlocks) {
			ret.addInitialization(ab.makeCopy());
		}
		
		for (FixedQuantity fq : fixeds) {
			ret.addFixed(fq.name, fq.value);
		}
		
		if (runConfig != null) {
			ret.addRunConfig(runConfig.makeCopy());
		}
		 
		if (builders != null) {
			for (Builder b : builders) {
				ret.addBuilder(b);
			}
		}
		
		ret.fix();
	
		return ret;
	}



}

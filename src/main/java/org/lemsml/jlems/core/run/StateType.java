package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.lemsml.jlems.core.eval.DoubleEvaluator;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.StateTypeVisitor;
import org.lemsml.jlems.core.type.Component;

public class StateType implements RuntimeType {

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
	
	HashMap<String, StateType> refHM = new HashMap<String, StateType>();
	
	HashMap<String, StateType> childHM = new HashMap<String, StateType>();
	
	
	ArrayList<ListChild> listChildren = new ArrayList<ListChild>();
 
	
	// TODO get rid of these entirely
	HashMap<String, MultiStateType> multiHM = new HashMap<String, MultiStateType>();
	
	HashMap<String, String> attSetHM = new HashMap<String, String>();

	HashMap<String, String> textParamHM = new HashMap<String, String>();
	
	boolean hasRegimes = false;
	HashMap<String, ComponentRegime> regimeHM = new HashMap<String, ComponentRegime>();
	
	HashSet<String> exposedNames = new HashSet<String>();
	HashMap<String, String> exposedMap = new HashMap<String, String>();
	
	ArrayList<RuntimeDisplay> runtimeDisplays = new ArrayList<RuntimeDisplay>();
	
	ArrayList<RuntimeOutput> runtimeOutputs = new ArrayList<RuntimeOutput>();
	
	ArrayList<RuntimeRecorder> runtimeRecorders = new ArrayList<RuntimeRecorder>();
	
	RunConfig runConfig = null;
	
	ArrayList<Builder> builders;
	
	boolean hasBuilds = false;
	
	ArrayList<String> isets;
	
	ArrayList<String> ipairsets;
	
	// true if type def says this should be consolidated
	boolean simultaneous = false;
	
	// set true once done
	boolean consolidated = false;
	
	// set if we're completely flat
	boolean flattened = false;
	
	// flattenedCB is genuinely flat - it could be the root flattened one, or one of its children, in the process 
	// of flattening the root.
	StateType flattenedCB = null;

	// consolidated is a behavior tree in which any behaviors that are marked for flattening have been flattened
	// but others remain as before
	StateType consolidatedCB = null;
	
	
	HashMap<String, Double> der1, der2, der3, der4, vwk,  val1, val2, val3, val4;
	
	
	boolean trackTime = false;
	long timeCounter = 0;
	long wkTime;
	
	
	public StateType(String sid, String tnm) {
		cptid = sid;
		typeName = tnm;
		vars.add("t"); // TODO should just have one DoublePointer to t 
 
	}
	
    @Override
	public String toString() {
		return "StateType, id=" + cptid + ", Type=" + typeName;
	}
	
	public String getComponentID() {
		return cptid;
	}

	public void enableTiming() {
		trackTime = true;
	}
	
	public void startClock() {
		wkTime = System.nanoTime();
	}
	
	public void stopClock() {
		timeCounter += (System.nanoTime() - wkTime);
	}
	
	public long getTotalTime() {
		return timeCounter;
	}
	
    public ArrayList<VariableROC> getRates() {
        return rates;
    }

    public ArrayList<FixedQuantity> getFixed() {
    	return fixeds;
    }
    
    public ArrayList<String> getVars() {
            return vars;
    }

    public ArrayList<String> getIndeps() {
            return indeps;
    }

    public ArrayList<String> getStateVariables() {
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
	
    
    
    public HashSet<String> getAllIndeps() {
    	HashSet<String> ret = new HashSet<String>();
    	ret.addAll(getIndeps());
    	for (ListChild lc : listChildren) {
    		ret.addAll(lc.getStateType().getAllIndeps());
    	}
    	for (String s : childHM.keySet()) {
    		ret.addAll(childHM.get(s).getAllIndeps());
    	}
    	
    	return ret;
    }
    
    
    
    public StateRunnable newStateRunnable() throws ContentError, ConnectionError, RuntimeError {
    	StateInstance si = newInstance();
    	return si;
    }
    
    
	public StateInstance newInstance() throws ContentError, ConnectionError, RuntimeError {
	 
		StateInstance uin = new StateInstance(this);
		// E.info("Creating new state instance of " + cptid + " " + typeName);
		
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
	 	
		
		
		for (ListChild lc : listChildren) {
			StateInstance si = lc.getStateType().newInstance();
			uin.addListChild(lc.getTypeName(), lc.getListName(), si);
		}
		
		/*
		for (String s : multiHMNames) {
			MultiInstance mi = multiHM.get(s).newInstance();
			mi.setKnownAs(s);
			uin.addMultiInstance(mi);
		}
		*/
		
		
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
				ComponentRegime cr = regimeHM.get(srn);
				RegimeStateInstance rsi = cr.newInstance(uin);
				uin.addRegime(rsi); 
			}
		}
		
	 
	
	 
		return uin;
	}
	
	
	
	public void build(StateInstance uin) throws ContentError, ConnectionError, RuntimeError {
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
            	if (!varHM.containsKey(pdv.varname)) {
            		throw new ContentError("No such pd variable " + pdv.varname + " in variables map: " + varHM);
            	}
                varHM.get(pdv.varname).set(pdv.eval(uin));
            }

            for (ExpressionDerivedVariable edv : exderiveds) {
            	if (!varHM.containsKey(edv.varname)) {
            		throw new ContentError("No such ed variable " + edv.varname + " in variables map: " + varHM);
            	}
            	
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
            E.report(err, e);
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
				try {
					StateRunnable si = pdv.getTargetState(uin);
					uin.addPathStateInstance(pdv.getPath(), si);
				} catch (ContentError ce) {
					if (pdv.isRequired()) {
						E.info("Rethrowing ce...");
						throw ce;
					} else {
						E.info("Optional path variable is absent " + pdv);
					}
				}
				
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
		//for (PathDerivedVariable pdv : pathderiveds) {
		//	v0.put(pdv.varname, pdv.eval(v0));
		//}
		
		for (ExpressionDerivedVariable edv : exderiveds) {
			v0.put(edv.varname, edv.eval(v0));
		}
	
		for (VariableROC vroc : rates) {
			ret.put(vroc.varname, vroc.eval(v0));
		}
	
	}
	
	
	void applyDerivs(HashMap<String, Double> v0, HashMap<String, Double> der, 
			double t, double delta, HashMap<String, Double> ret) {
		
		for (String sk : v0.keySet()) {
			ret.put(sk, v0.get(sk));
		}
		
		
		for (String sk : der.keySet()) {
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


	public void addExpressionDerived(String snm, DoubleEvaluator db) {
		ExpressionDerivedVariable edv = new ExpressionDerivedVariable(snm, db);
        //E.info("Adding: "+edv);
		exderiveds.add(edv);
	}

	 
	
	public PathDerivedVariable addPathDerived(String snm, String path, String rf, 
			boolean reqd, String reduce) {
		PathDerivedVariable pdv = new PathDerivedVariable(snm, path, rf, reqd, reduce);
		pathderiveds.add(pdv);
        return pdv;
	}
	
	public void addFixed(String snm, double d) {
		fixeds.add(new FixedQuantity(snm, d));
	}
	
	
	public void addIndependentVariable(String vnm) {
		if (indeps.contains(vnm)) {
			E.warning("Added the an independent variable again? " + vnm + " " + this);
		} else {
			indeps.add(vnm);
		}
 	}

	public void addRate(String name, DoubleEvaluator de) {
		rates.add(new VariableROC(name, de));
	}

	public void addEventResponse(EventAction er) {
		addAction(er.getPortName(), er.getAction());	
	}

	public void addAction(String spn, ActionBlock a) {
		eventHM.put(spn, a);
	}
	 
	
	public void addConditionResponse(ConditionAction cr) {
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
			String vnm = vroc.getVariableName();
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
			String s = pdv.getVariableName();
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

	
	public void addRefStateType(String s, StateType chb) {
		refHM.put(s, chb);
	}
	
	public void addChildStateType(String s, StateType chb) {
		childHM.put(s, chb);
	}

	
	/*
	public void addMultiComponentBehavior(String s, MultiStateType mcb) {
		E.info("Adding multhm name " + s + " " + mcb);
		
 		multiHMNames.add(s);
		multiHM.put(s, mcb);
		if (s == null) {
			E.stackTrace();
		}
	}
*/
	
	public void addListStateType(String s, StateType cb) {
		listChildren.add(new ListChild(s, cb));
		addMulti(s, cb);
	}
	
	
	private void clearMultis() {
		multiHM = new HashMap<String, MultiStateType>();
	}
	
		
	private void addMulti(String s, StateType cb) {	
		if (multiHM.containsKey(s)) {
			multiHM.get(s).add(cb);
		} else {
			MultiStateType mcb = new MultiStateType();
			mcb.add(cb);
			multiHM.put(s, mcb);
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

	public void addInPorts(ArrayList<String> pa) {
		for (String s : pa) {
			addInputPort(s);
		}
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
			E.info("Dynamics marked for consolidation " + this);
		}
		
	}

	 
	
	
	
	
	public StateType getConsolidatedStateType(String knownas) {
		if (consolidatedCB == null) {
			consolidatedCB = makeConsolidatedStateType(knownas);
		}
		return consolidatedCB;
	}
	
	
	public StateType getFlattenedStateType(String knownas) {
		if (flattenedCB == null) {
			flattenedCB = makeFlattened(knownas);
		}
		return flattenedCB;
	}
	
	 
	
	public StateType makeConsolidatedStateType(String knownas) {
		StateType ret = null;
		if (simultaneous) {
			E.info("********* Flattening " + knownas + " (id=" + cptid + ")");
			ret = getFlattenedStateType(knownas);
		} else {
			ret = makeChildConsolidated();
		}
		return ret;
	}
	
	
	public StateType makeChildConsolidated() {
		StateType ret = makeShallowCopy();
		ret.consolidateChildren();
		return ret;
	}
	
	
	public HashMap<String, StateType> getChildHM() {
		return childHM;
	}
	

	public HashMap<String, StateType> getRefHM() {
		return refHM;
	}
	
	public HashMap<String, MultiStateType> getMultiHM() {
		return multiHM;
	}
		
	
	
	private void consolidateChildren() {
		// E.info("Consolidating children in " + knownas + (cptid != null ? "(id=" + cptid + ")" : ""));
		for (String sch : childHM.keySet()) {
			// E.info("Child: " + sch);
			StateType cbch = childHM.get(sch);
			StateType fcbch = cbch.getConsolidatedStateType(sch);
			childHM.put(sch,  fcbch);
		}
		
		
		
		clearMultis();
		for (ListChild lc : listChildren) {
			String lnm = lc.getListName();
			addMulti(lnm, lc.getStateType().getConsolidatedStateType(lnm));
		}
		
		/*
		for (String sm : multiHM.keySet()) {
			// E.info("multi child " + sm);
			MultiStateType mcb = multiHM.get(sm);
			ArrayList<StateType> af = new ArrayList<StateType>();
			for (StateType cbv : mcb.getCBs()) {
				af.add(cbv.getConsolidatedComponentBehavior(sm));
			}
			
			MultiStateType fmcb = new MultiStateType(af);
			multiHM.put(sm, fmcb);
		}
		*/
		
		
		for (String sm : refHM.keySet()) {
 			StateType cbr = refHM.get(sm);
			StateType fcbr = cbr.getConsolidatedStateType(sm);
			refHM.put(sm,  fcbr);
		}
		
		
		if (builders != null) {
			for (Builder b : builders) {
				b.consolidateStateTypes();
			}
		}
		
	}
	
	
	
	public StateType makeFlattened(String knownas) {	
		Flattener flattener = new Flattener();

		// E.info("FLAT making flattened of " + typeName + " " + cptid + " " + indeps);
		
		addToFlattener(null, flattener);
		
		for (String sch : childHM.keySet()) {
			StateType cbch = childHM.get(sch);
			StateType cbchflat = cbch.getFlattenedStateType(sch);
			cbchflat.addToFlattener(sch, flattener);	
		}
		
		for (String sm : multiHM.keySet()) {
			E.info("multi child " + sm);
			MultiStateType mcb = multiHM.get(sm);
 			int ictr = 0;
			for (StateType cbv : mcb.getCBs()) {
				StateType mcf = cbv.getFlattenedStateType(sm);
				 mcf.addToFlattener(sm + ictr, flattener);
				ictr += 1;
			}	 
		}
		
		for (String sm : refHM.keySet()) {
 			StateType cbr = refHM.get(sm);
			StateType fcbr = cbr.getFlattenedStateType(sm);
			fcbr.addToFlattener(sm, flattener);
		}
 		
		flattener.resolvePaths();
		
		StateType ret = new StateType(cptid, typeName);
		ret.flattened = true;
		
		flattener.exportTo(ret);
		
		ret.fix();
		
		return ret;
	}

	
	
	private void addToFlattener(String pfx, Flattener fl) {
		String fullpfx = "";
		if (pfx != null && pfx.length() > 0) {
			fullpfx = pfx + "_";
		}
			
		HashSet<String> indHS = new HashSet<String>();
		indHS.addAll(indeps);
		
		for (String s : indeps) {
			fl.addIndependentVariable(s);
		}
		
		for (String s : svars) {
			fl.addStateVariable(fullpfx + s);
		}
		for (PathDerivedVariable pdv : pathderiveds) {
			fl.add(pdv.makeFlat(fullpfx));
		}
		for (ExpressionDerivedVariable edv : exderiveds) {
			fl.add(edv.makeFlat(fullpfx, indHS));
		}
		for (VariableROC vroc : rates) {
			fl.add(vroc.makeFlat(fullpfx, indHS));
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
            // TODO check these cases how .get(sp) can be null?
			if (refHM.get(sp) != null && refHM.get(sp).getComponentID() != null) {
                ret = true;
            }
            
		} else if (textParamHM.containsKey(sp)) {
			ret = true;
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



	
	public StateType makeShallowCopy() {
		StateType ret = new StateType(cptid, typeName);
	
		
		for (String s : indeps) {
 			ret.addIndependentVariable(s);
		}
		
		for (String s : childHM.keySet()) {
			ret.addChildStateType(s, childHM.get(s)); //.makeShallowCopy());
		}
		
		// TODO cloning refs?
		for (String s : refHM.keySet()) {
			ret.addRefStateType(s, refHM.get(s)); // .makeShallowCopy());
		}
		
	 
		for (ListChild lc : listChildren) {
			ret.addListStateType(lc.getListName(), lc.getStateType());
		}
		
		//for (String s : multiHM.keySet()) {
		//	ret.addMultiComponentBehavior(s, multiHM.get(s)); // .makeCopy());
		//}
		
		for (String s : exposedMap.keySet()) {
			ret.addExposureMapping(s, exposedMap.get(s));
		}
		
		for (String s : attSetHM.keySet()) {
			ret.addAttachmentSet(s, attSetHM.get(s));
		}
		
		for (KScheme ks : kschemes) {
			ret.addKScheme(ks.makeCopy());
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
			// a null action block is OK - it could be a state instance containing regimes,
			// so the actual action block will be in one or more of the regimes, but we need
			// and action on the parent to pass it on.
			ActionBlock eab = eventHM.get(s);
			if (eab != null) {
				ret.addAction(s, eab.makeCopy());
			} else {
				ret.addAction(s, null);
			}
		}
		
		for (ActionBlock ab : initBlocks) {
			ret.addInitialization(ab.makeCopy());
		}
		
		for (FixedQuantity fq : fixeds) {
			ret.addFixed(fq.name, fq.value);
		}
		
		ret.addInPorts(inPorts);
		
		for (ConditionAction ca : conditionResponses) {
			ret.addConditionResponse(ca.makeCopy());
		}
		
		for (ActionBlock ab : initBlocks) {
			ret.addInitialization(ab.makeCopy());
		}
		
		for (String s : eventHM.keySet()) {
			ActionBlock ab = eventHM.get(s);
			if (ab != null) {
				ret.addEventResponse(new EventAction(s, ab.makeCopy()));
			} else {
				ret.addEventResponse(new EventAction(s, null));
			}
		}
	 
		if (hasRegimes) {
			ret.hasRegimes = true;
			for (String s : regimeHM.keySet()) {
				ret.addComponentRegime(regimeHM.get(s).makeCopy(ret));
			}
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

	public HashMap<String, String> getExposureMap() {
		return exposedMap;
	}

	public void addRuntimeDisplay(RuntimeDisplay ro) {
		runtimeDisplays.add(ro);
	}

	public void addRuntimeOutput(RuntimeOutput rw) {
		runtimeOutputs.add(rw);
	}
	
	public void addRecorder(String id, String q, double tsc, double ysc, String col, String display) {
		runtimeRecorders.add(new RuntimeRecorder(id, q, tsc, ysc, col, display));
	}
	
	
	public void visitAll(StateTypeVisitor v) {
		v.visit(this);
		
		for (String s : childHM.keySet()) {
			childHM.get(s).visitAll(v);
		}
		for (String s : multiHM.keySet()) {
			multiHM.get(s).visitAll(v);
		}
	}

	
	public ArrayList<RuntimeDisplay> getRuntimeDisplays() {
		return runtimeDisplays;
	}

	public ArrayList<RuntimeOutput> getRuntimeOutputs() {
		return runtimeOutputs;
	}
	
	public ArrayList<RuntimeRecorder> getRuntimeRecorders() {
		return runtimeRecorders;
	}

	public String getID() {
		return cptid;
	}

	public String getTypeName() {
		return typeName;
	}

	public HashSet<String> getRequirements() {
		HashSet<String> allReq = new HashSet<String>();
		allReq.addAll(indeps);
		for (ListChild lc : listChildren) {
			allReq.addAll(lc.getStateType().getRequirements());
		}
		
		 return allReq;
	}
}

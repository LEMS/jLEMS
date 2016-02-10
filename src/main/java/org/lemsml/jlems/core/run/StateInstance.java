package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.jlems.api.interfaces.ILEMSStateInstance;
import org.lemsml.jlems.core.display.LineDisplay;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.InstanceProperty;


@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
public class StateInstance implements StateRunnable, ILEMSStateInstance {

	StateType stateType;
	String id;

	// private HashMap<String, DoublePointer> localHM;
	
	 
	private HashMap<String, DoublePointer> varHM;
	private HashMap<String, DoublePointer> expHM;

	// TODO only use these if there is more than one;
	HashMap<String, InPort> inPortHM = null; 
	HashMap<String, OutPort> outPortHM = null;
	OutPort firstOut;
	InPort firstIn;
	boolean hasChildren = false;

	ArrayList<StateRunnable> childA;
	HashMap<String, StateRunnable> childHM;
	
	
	ArrayList<StateListChild> stateListChildren;
	
	boolean hasMulti = false;
	ArrayList<MultiInstance> multiA;
	
	HashMap<String, MultiInstance> multiHM;
	boolean singleAMI = false;
	MultiInstance onlyAMI = null;
	
	boolean singleIS = false;
	InstanceSet<StateRunnable> onlyIS = null;
	boolean resolvedPaths = false;

	HashMap<String, StateRunnable> pathSIHM;
	HashMap<String, ArrayList<StateRunnable>> pathAHM;

	HashMap<String, StateRunnable> idSIHM;
	boolean hasSchemes = false;
	ArrayList<KSchemeInst> schemeA;
	HashMap<String, KSchemeInst> schemeHM;
	boolean hasRegimes = false;
	HashMap<String, RegimeStateInstance> regimeHM;

	RegimeStateInstance activeRegime = null;
	HashMap<String, InstanceSet<StateRunnable>> instanceSetHM;
	HashMap<String, InstancePairSet<StateRunnable>> instancePairSetHM;
	ArrayList<DestinationMap> dmaps;
	ArrayList<Builder> builders;
	StateRunnable parent;
	Object work; // just a a cache - TODO not a good solution
	boolean built = false;
	boolean initialized = false;
	double currentTime = Double.NaN;

	boolean debug = false;
	//boolean debug = true;

	boolean bList;
	String listName;
	 
	// EventManager eventManager;
	
	
	public StateInstance() {
		// for standalone instances that aren't related to a Dynamics - see
		// PairFilter for usage
	}

	public StateInstance(StateType uc) {
		stateType = uc;
		id = uc.getComponentID();
		String uct = uc.getTypeName();
		if (uct.equals("Instance")) {
			E.trace();
	}
	}

    @Override
	public String getID() {
		return id;
	}
    
    
	public String getInfo() {
        String parentInfo = getParent()!=null ? ", parent: "+ getParent().getID() : "";
        return "StateInstance: " + getID() + " [StateType: " +stateType.getTypeName() + parentInfo +"]";
	}

    @Override
	public void setParent(StateRunnable p) {
		parent = p;
	}

    @Override
	public StateRunnable getParent() {
		return parent;
	}

    @Override
	public boolean isBuilt() {
		return built;
	}

	@Override
	public String toString() {
		String ret;
		if (stateType == null) {
			ret = "Dummy state instance " + getID();
		} else {
			ret = stateType.getID() + "[" + stateType.getTypeName() + "]";
		}
		return ret;
	}

    @Override
	public void setList(String s) {
		bList = true;
		listName = s;
	}
	
	public boolean isList() {
		return bList;
	}
	
	
	public double getCurrentTime() {
		return currentTime;
		}

	

    @Override
	public void initialize(StateRunnable parent) throws RuntimeError, ContentError {
	 
		
		currentTime = 0;
		stateType.initialize(this, parent, false, false);
		if (debug) {
			E.info("\n\n   1 Post init " + this + " has vars: " + this.varHM + " and exps " + this.expHM);
		}

		if (hasChildren) {
			for (StateRunnable ch : childA) {
				ch.initialize(this);
			}
		}
		if (hasMulti) {
			for (MultiInstance mi : multiA) {
				mi.initialize(this);
			}
		}

		if (hasSchemes) {
			for (KSchemeInst ksi : schemeA) {
				ksi.initialize(this);
			}
		}
        if (debug) {
			E.info("   1.5 Post CHILDREN init " + this + " has vars: " + this.varHM + " and exps " + this.expHM + "\n");
		}
		stateType.initialize(this, parent, true, false);
		if (debug) {
			E.info("   2 Post CHILDREN init " + this + " has vars: " + this.varHM + " and exps " + this.expHM + "\n");
		}

		// Once more
		if (hasChildren) {
			for (StateRunnable ch : childA) {
				ch.initialize(this);
			}
		}
		if (hasMulti) {
			for (MultiInstance mi : multiA) {
				mi.initialize(this);
			}
		}

		if (hasSchemes) {
			for (KSchemeInst ksi : schemeA) {
				ksi.initialize(this);
			}
		}

		stateType.initialize(this, parent, true, false);
		if (debug) {
			E.info("    3 Post CHILDREN init " + this + " has vars: " + this.varHM + " and exps " + this.expHM + "\n");
		}
	}

	
	
    @Override
	public void evaluate(StateRunnable parent) throws RuntimeError, ContentError {
		if (!built) {
			throw new RuntimeError("advance() called before build on " + this);
		}
		if (!initialized) {
			this.initialize(parent);
		}

		if (hasChildren) {
			for (StateRunnable ch : childA) {
				ch.evaluate(this);
			}
		}
		if (hasMulti) {
			for (MultiInstance mi : multiA) {
				mi.evaluate(this);
			}
		}

		if (hasSchemes) {
			for (KSchemeInst ksi : schemeA) {
				ksi.evaluate(this);
			}
		}
		
		
		
		stateType.evaluate(this,  parent);

		if (hasRegimes) {
			activeRegime.evaluate(this);
		}
	}
	
	
	
	
	
	
	
    @Override
	public void advance(StateRunnable parent, double t, double dt) throws RuntimeError, ContentError {
		if (!built) {
			throw new RuntimeError("advance() called before build on " + this);
		}
		
		currentTime = t;

		
		if (!initialized) {
			this.initialize(parent);
		}
		
		if (stateType.trackTime) {
			stateType.startClock();
		}

		if (hasChildren) {
			for (StateRunnable ch : childA) {
				ch.advance(this, t, dt);
			}
		}
		if (hasMulti) {
			for (MultiInstance mi : multiA) {
				mi.advance(this, t, dt);
			}
		}

		if (hasSchemes) {
			for (KSchemeInst ksi : schemeA) {
				ksi.advance(this, t, dt);
			}
		}

		if (RUN.method == RUN.RK4 || RUN.method == RUN.EULER) {

			if (stateType.flattened && RUN.method == RUN.RK4) {
				stateType.rk4Advance(this, parent, t, dt);
			} else {
				stateType.eulerAdvance(this, parent, t, dt);
			}
		}

		if (hasRegimes) {
			activeRegime.advance(this, t, dt);
		}
		
		if (stateType.trackTime) {
			stateType.stopClock();
		}
	}
	

	public void transitionTo(String rnm) throws RuntimeError {
		activeRegime = regimeHM.get(rnm);
		activeRegime.enter();
	}

	public void doneBuild() {
		built = true;
	}

	public void doneInit() {
		initialized = true;
	}

	public void initRegime() throws RuntimeError {
		if (activeRegime == null) {
			activeRegime = regimeHM.get(regimeHM.keySet().iterator().next());
			// TODO just picks random regime
		}
		activeRegime.enter();
	}

	public void setExposedVariables(HashSet<String> vars) {
		expHM = new HashMap<String, DoublePointer>();
		for (String s : vars) {
			expHM.put(s, new DoublePointer(0.));
		}
	}

	public void setVariables(ArrayList<String> vars) {
		varHM = new HashMap<String, DoublePointer>();
		for (String s : vars) {
			varHM.put(s, new DoublePointer(0.));
		}
	}

	public void setIndependents(ArrayList<String> vars) {
		for (String s : vars) {
			varHM.put(s, new DoublePointer(0.));
		}
	}

	public void setExpressionDerived(ArrayList<ExpressionDerivedVariable> exderiveds) {
		for (ExpressionDerivedVariable edv : exderiveds) {
			varHM.put(edv.getVariableName(), new DoublePointer(0.));
		}
	}

	public void setFixeds(ArrayList<FixedQuantity> fqs) {
		for (FixedQuantity fq : fqs) {
			varHM.put(fq.getName(), new DoublePointer(fq.getValue()));
		}
	}

	public void setInstanceProperties(ArrayList<InstanceProperty> ips) {
		for (InstanceProperty ip : ips) {
			varHM.put(ip.getName(), new DoublePointer(ip.getValue()));
		}
	}

	public void setLocalValues(LocalValues lpvals) {
	 	for (String s : lpvals.keySet()) {
			double v = lpvals.getValue(s);
			if (varHM.containsKey(s)) {
				varHM.get(s).set(v);
			//	E.info("Set local " + s + " " + v);
 			} else {
				varHM.put(s, new DoublePointer(v));
				E.warning("setLocals is assigning a value for varialbe '" + s + "' that wasn't previously known.");
			}
		}
	}
	
	
	public HashMap<String, DoublePointer> getVarHM() {
		return varHM;
	}

	public HashMap<String, DoublePointer> getExpHM() {
		return expHM;
	}

	public void addInputPort(String s, ActionBlock actionBlock) {
		InPort inp = new InPort(this, s, actionBlock);
		if (firstIn == null) {
			firstIn = inp;
		}
		if (inPortHM == null) {
			inPortHM = new HashMap<String, InPort>();
		}
		inPortHM.put(s, inp);
	}

	
	public void checkAddInputPort(String s) {
		if (inPortHM != null && inPortHM.containsKey(s)) {
			// fine - there's an action block for it already
		} else {
			// no action block, but we still need the port: presumably for an
			// action within a regime
			addInputPort(s, null);
		}
	}

	public void addOutputPort(String s) {
		OutPort op = new OutPort(s);
		if (firstOut == null) {
			firstOut = op;
		}
		if (outPortHM == null) {
			outPortHM = new HashMap<String, OutPort>();
		}
		outPortHM.put(s, op);
	}

    
    
	public void sendFromPort(String sop) throws RuntimeError {
		outPortHM.get(sop).send();
	}

	public OutPort getOrMakeOutputPort(String s) {
		if (outPortHM != null && outPortHM.containsKey(s)) {
			// nothing more to do. This is called by regimes to connect to the
			// main state instance ports
		} else {
			addOutputPort(s);
		}
		return outPortHM.get(s);
	}

    @Override
	public InPort getFirstInPort() throws ConnectionError {
		if (firstIn == null) {
			throw new ConnectionError("No input ports on " + this);
		}
		return firstIn;
	}

    @Override
	public InPort getInPort(String portId) throws ConnectionError {
		InPort ret = null;
		if (inPortHM != null && inPortHM.containsKey(portId)) {
			ret = inPortHM.get(portId);
		} else {
			throw new ConnectionError("No such port '" + portId + "' on " + this);
	}
		return ret;
	}

    @Override
	public String stateString() {
		return varHM.toString();
	}

    @Override
	public void exportState(String pfx, double t, LineDisplay ld) {
		for (String s : varHM.keySet()) {
			ld.addPoint(pfx + s, t, varHM.get(s).get());
		}
	}

    @Override
	public HashMap<String, DoublePointer> getVariables() {
		return varHM;
	}

    @Override
	public StateWrapper getWrapper(String snm) {
		StateWrapper ret = null;
		if (varHM.containsKey(snm)) {
			ret = new StateWrapper(this, snm);
		}
		return ret;
	}

    @Override
	public StateRunnable getChild(String snm) throws ConnectionError {
		StateRunnable ret = null;

		if (snm.startsWith("[") && singleAMI) {
			int idx = Integer.parseInt(snm.substring(1, snm.length() - 1));
			ret = onlyAMI.getInstance(idx);

		} else if (childHM != null && childHM.containsKey(snm)) {
			ret = childHM.get(snm);

		} else {

			// TODO this is rather adhoc for resolving paths - should be
			// external
			if (childA != null) {
				for (StateRunnable si : childA) {
					if (snm.equals(si.getComponentID())) {
						ret = si;
						break;
					}
				}
			}
			if (ret == null && multiA != null) {
				for (MultiInstance mi : multiA) {
					for (StateRunnable sr : mi.getStateInstances()) {
						if (sr == null) {
							throw new ConnectionError("null sr in multi instance?");

						} else if (sr.getID() == null) {
							// throw new ConnectionError("null id in sr " + sr);
							// not a problem - can have children without ids if
							// we don't ever need to access them
						} else if (sr.getID().equals(snm)) {
							ret = sr;
							break;
						}
					}
				}
			}
		}

		if (ret == null) {
			String err = "No such child element or variable " + snm + " in " + this + "\n";
			err += "childHM= " + childHM + "\n"; 
			err += "childA= " + childA;
			throw new ConnectionError(err);
		}
		return ret;
	}

    @Override
	public double getVariable(String varname) throws RuntimeError {
		// System.out.println("varHM ("+varHM+"), expHM ("+expHM+") for state: "+this.toString()+", varname: "+varname);
		double ret = Double.NaN;

		// now only exposing variables that are in expHM, not stuff in varHM.
		if (expHM != null && expHM.containsKey(varname)) {
			ret = expHM.get(varname).get();
			checkReturn(ret, varname);
		 
		} else {
 			if (varHM.containsKey(varname)) {
			ret = varHM.get(varname).get();
 				checkReturn(ret, varname);
			
		} else {
			if (parent != null) {
				ret = parent.getVariable(varname);
 			    checkReturn(ret, varname);
			}
		}
		}
		return ret;
	}

	
	private void checkReturn(double ret, String varname)  throws RuntimeError {
		if (Double.isNaN(ret) || Double.isInfinite(ret)) {
            
			StringBuilder err = new StringBuilder();
            
			err.append("\nProblem while trying to return a value for variable "+varname+": "+ret);
			
			err.append("\n\nCurrent StateInstance:\n"+getSummary("  ", "| "));
			
			err.append("\n\nCurrent StateInstance's parent:\n"+((StateInstance)this.getParent()).getSummary("  ", "| "));
			
			throw new RuntimeError(err.toString());
		}
	}
   
    
        
	
    @Override
	public String getChildSummary() {
		StringBuilder err = new StringBuilder();
        if (childA != null) {
            for (StateRunnable si : childA) {
				err.append("Child: " + si + ", vars: " + si.getVariables() + "\n");
            }
        } else {
			err.append("childA is null\n");
        }
        if (childHM != null) {
            for (String k : childHM.keySet()) {
                StateRunnable si = childHM.get(k);
				err.append("Child " + k + ": " + si + ", vars: " + si.getVariables() + "\n");
            }
        } else {
			err.append("childHM is null\n");
        }
		return err.toString();
	}
    
    public String getSummary(String indent, String prefix) {
        StringBuilder info = new StringBuilder();
        String pre = indent + prefix + " ";
        String line = "+-------------------------------------------------";
        for (int i=0; i + indent.length()<40; i++)
            line = line +'-';
        info.append(indent + line);
        //info.append("    >>> i: ["+indent+"]  pref: ["+prefix+"]  p: ["+pre+"]");
        info.append("\n");
        info.append(pre + getInfo()+ " \n");
        
        info.append(pre +     "  Variables: {");
        int count = 0;
        for(String key: getVariables().keySet()) {
            if (count==5) {
                info.append(",\n"+pre+"              ");
                count=0;
            }
            else if (count>0) {
                info.append(", ");
            }
            info.append(key+" = "+getVariables().get(key).getValue());
            
            count++;
        }
        info.append("}\n");
        
        info.append(pre +     "  Exposures: {");
        count = 0;
        for(String key: getExpHM().keySet()) {
            if (count==5) {
                info.append(",\n"+pre+"              ");
                count=0;
            }
            else if (count>0) {
                info.append(", ");
            }
            info.append(key+" = "+getExpHM().get(key).getValue());
            
            count++;
        }
        info.append("}\n");
        
        ArrayList<String> shown = new ArrayList<String>();
        /*
        if (childA != null) {
            for (StateRunnable sr : childA) {
                StateInstance si = (StateInstance) sr;
                info.append(si.getSummary(indent+prefix+" c ", prefix)+"\n");
            }
        }*/
        
        if (childHM != null) {
            for (String k : childHM.keySet()) {
                StateRunnable sr = childHM.get(k);
                StateInstance si = (StateInstance) sr;
                shown.add(si.getInfo());
                info.append(si.getSummary(indent+prefix+"   ", prefix)+"\n");
            }
        }
        /*
        if (multiA != null) {
            for (MultiInstance mi : multiA) {
                info.append(pre + mi + "\n");
            }
        }
*/
        if (multiHM != null) {
            for (String mi : multiHM.keySet()) {
                //info.append(pre + "+++ (" + mi + ")" + multiHM.get(mi) + "\n");
                int index = 0;
                for (StateRunnable sr: multiHM.get(mi).getStateInstances()) {
                    StateInstance si = (StateInstance) sr;
                    // TODO: find a better way to prevent repetition... 
                    if (!shown.contains(si.getInfo())) {
                        info.append(si.getSummary(indent+prefix+"   | "+mi+"["+index+"] ", prefix)+"\n");
                    }
                    index+=1;
                }
                
            }
            info.append(""+indent+prefix+"   " + line+"\n");
        }
        
        info.append(""+indent + line);
        return info.toString();
    }
 
	
	public int getChildCount() {
		int ret= 0;
		if (childA != null) {
			ret = childA.size();
            }
		return ret;
        }
        

	
	public void addChild(String s, StateRunnable newInstance) {
		if (newInstance == null) {
			E.warning("adding a null child instance to " + this);
		} else {
			newInstance.setParent(this);

			if (!hasChildren) {
				hasChildren = true;
				childA = new ArrayList<StateRunnable>();
			}
			if (childHM == null) {
				childHM = new HashMap<String, StateRunnable>();
			}
			childA.add(newInstance);
			childHM.put(s, newInstance);
		}
	}
	
	
	public void addRefChild(String s, StateRunnable sr) {
		// rcName = s;
		if (childHM == null) {
			childHM = new HashMap<String, StateRunnable>();
		}
		childHM.put(s, sr);
	}
	
	
	// TODO - not sure we need type name tnm here?
	public void addListChild(String tnm, String sid, StateInstance newInstance) {
		if (stateListChildren == null) {
			stateListChildren = new ArrayList<StateListChild>();
		}
		newInstance.setParent(this);
		stateListChildren.add(new StateListChild(sid, newInstance));
 		
		if (!hasMulti) {		 
			hasMulti = true;
			multiA = new ArrayList<MultiInstance>();
			multiHM = new HashMap<String, MultiInstance>();
		}
		if (multiHM.containsKey(sid)) {
			multiHM.get(sid).add(newInstance);
		} else {
			MultiInstance mi = new MultiInstance(tnm, sid);
			mi.setParent(this);
			mi.add(newInstance);
			multiA.add(mi);
			multiHM.put(sid,  mi);
		}
		
		
		// TODO - in child map or idSIHM?
		String cid = newInstance.getID();
		if (cid != null) {
			if (childHM == null) {
				childHM = new HashMap<String, StateRunnable>();
			}
			
			childHM.put(cid,  newInstance);
		}
		
		countMIs();
	}
	
	private void countMIs() {
		singleAMI = false;
		onlyAMI = null;
		if (multiA.size() == 1) {
			onlyAMI = multiA.get(0);
			singleAMI = true;
		}
	}
	
 
	private void addMultiInstance(MultiInstance mi) {
//		String msg = ("adding mi " + mi + " to " + this);
		
		if (!hasMulti) {		 
			hasMulti = true;
			multiA = new ArrayList<MultiInstance>();
			multiHM = new HashMap<String, MultiInstance>();
		}
		multiA.add(mi);
		multiHM.put(mi.getKnownAs(), mi);
		mi.setParent(this);

		countMIs();
		}

    @Override

    public StateRunnable getChildInstance(String snm) throws ContentError {
// errors because we used to turn ComponentRefs into children, but we
// don't always need that
// now they go in refHM and don't automaticlly get instances added as
// children
		 
        StateRunnable ret = null;
        if (hasChildInstance(snm)) {
            ret = childHM.get(snm);
		
        } else {
            throw new ContentError("seeking child instance " + snm + " in " + this + " but there are no children");
        }
        return ret;
    }

	
    @Override
	public boolean hasChildInstance(String snm) throws ContentError {
		boolean ret = false;
		
		if (!built) {
			E.error("seeking child instance " + snm + " before state instance is built: " + this);
		}
			
		if (childHM != null && childHM.containsKey(snm)) {	
			ret = true;
		}
		return ret;
	}

	
	public boolean hasIDInstance(String s) {
		if (idSIHM == null) {
			makeIDSIHM();
		}
		boolean ret = false;
		if (idSIHM.containsKey(id)) {
			ret = true;
		}
		return ret;
	}

	public StateRunnable getIDInstance(String s) {
		return idSIHM.get(s);
	}
	
	
	
	public boolean hasMultiInstance(String snm) {
		boolean ret=  false;
		if (multiHM != null && multiHM.containsKey(snm)) {
			ret = true;
		}
		return ret;
	}
	
	public MultiInstance getMultiInstance(String snm) {
		MultiInstance mi = multiHM.get(snm);
		return mi;
	}

	public void addPathStateInstance(String pth, StateRunnable pl) {
		if (pathSIHM == null) {
			pathSIHM = new HashMap<String, StateRunnable>();
		}
		pathSIHM.put(pth, pl);
	}

    @Override
	public StateRunnable getPathStateInstance(String pth) throws ContentError {
		if (!resolvedPaths) {
			stateType.applyPathDerived(this);
		}
		return pathSIHM.get(pth);
	}

	
	public ArrayList<StateRunnable> getListItems() {
		ArrayList<StateRunnable> ret = null;
		if (multiA.size() == 1) {
			ret = multiA.get(0).getInstances();
		}
		return ret;
	}
	
	
    @Override
	public StateRunnable getScopeInstance(String id) {
		StateRunnable ret = null;

		if (idSIHM == null) {
			makeIDSIHM();
		}

		if (idSIHM.containsKey(id)) {
			ret = idSIHM.get(id);

		} else if (parent != null) {
			ret = parent.getScopeInstance(id);
		}
		return ret;
	}

	void makeIDSIHM() {
		idSIHM = new HashMap<String, StateRunnable>();
		if (childA != null) {
			for (StateRunnable si : childA) {
				if (si.getID() != null) {
					idSIHM.put(si.getID(), si);
					// E.info("added child " + si.getID());
				}
			}
		}
		if (multiA != null) {
			for (MultiInstance mi : multiA) {
				for (StateRunnable si : mi.getInstances()) {
					if (si.getID() != null) {
						idSIHM.put(si.getID(), si);
						// E.info("added child " + si.getID());
					}
				}
			}
		}  
		
	}
		
		
    @Override
		public String getPathStringValue(String path, double fac, double off) throws ContentError, RuntimeError {
			String ret;

	        StateRunnable wkinst = this;
	        String[] bits = path.split("/");
	        for (int i = 0; i < bits.length - 1; i++) {
	            wkinst = wkinst.getChildInstance(bits[i]);
	        }
	        String lbit= bits[bits.length - 1];
	        
	        if (wkinst != null) {
	        	if (lbit.equals("name")) {
	        		ret = stateType.getComponentID();
	        		
	        	} else if (lbit.equals("id")) {
	        		ret = stateType.getComponentID();
	        		
	        	} else {
	        		ret= "" + Out.formatDouble(fac * wkinst.getVariable(lbit) - off);
	        	}
	        } else {
	        	ret = "(ERR:" + lbit + ")";
	        }
	        return ret;
	    }
	
	
	
	

	public void addPathStateArray(String pth, ArrayList<StateRunnable> pla) throws ContentError {
		// E.info("adding psa " + pth + " " + pla);

		if (pathAHM == null) {
			pathAHM = new HashMap<String, ArrayList<StateRunnable>>();
		}

		pathAHM.put(pth, pla);
		DestinationMap dm = new DestinationMap(pth, pla);
		if (dmaps == null) {
			dmaps = new ArrayList<DestinationMap>();
		}
		dmaps.add(dm);
	}

	public ArrayList<StateRunnable> getPathStateArray(String pth) throws ContentError {
		if (!resolvedPaths) {
			// E.info("resolving psas in getPSA");
			stateType.applyPathDerived(this);
		}

		return pathAHM.get(pth);
	}

	public void donePaths() {
		resolvedPaths = true;
	}

	public void addAttachmentSet(String s, MultiInstance inas) {
		inas.setKnownAs(s);
		addMultiInstance(inas);

	}

    @Override
	public void addAttachment(StateInstance inst) throws ConnectionError, ContentError, RuntimeError {
		addAttachment(null, inst);
	}
		
    @Override
	public void addAttachment(String s, StateInstance inst) throws ConnectionError, ContentError, RuntimeError {
		String snm = s;
		MultiInstance mi = null;
		if (snm == null) {
			if (singleAMI) {
				mi = onlyAMI;
				snm = mi.getKnownAs(); // what s would have been if we'd known
				// it at the time
			} else {
				if (onlyAMI == null) {
					throw new ConnectionError("No attachments list in " + this);
				} else {
					throw new ConnectionError("Must specify destination attachments list since the target has multiple sets");
				}
			}

		} else {
			mi = multiHM.get(s);
		}
		mi.add(inst);
		inst.setParent(this);

		if (built) {
			// the child is added too late to come on with the default
			// checkBuilt
			inst.checkBuilt();

		}

		// this is fine for the advance() step, but paths that use it have
		// already been
		// populated so we need to see if this state should go in any of the
		// path state arrays

		if (dmaps != null && snm != null) {
			for (DestinationMap dm : dmaps) {
				dm.checkInsert(snm, inst);
			}
		}

	}

	public void addKSchemeInst(KSchemeInst ksi) {
		hasSchemes = true;
		if (schemeA == null) {
			schemeA = new ArrayList<KSchemeInst>();
			schemeHM = new HashMap<String, KSchemeInst>();
		}
		schemeA.add(ksi);
		schemeHM.put(ksi.getName(), ksi);

	}

    @Override
	public void setVariable(String vnm, double pval) {
		varHM.get(vnm).set(pval);
	}

	// TODO this sets component level variables - should probably keep them
	// separate
    @Override
	public void setNewVariable(String vnm, double pval) {
		if (Double.isNaN(pval) || Double.isInfinite(pval)) {
			E.error("Nan for " + vnm);
		}
		varHM.put(vnm, new DoublePointer(pval));
	}

	public StateType getStateType() {
		return stateType;
	}
    
    @Override
	public OutPort getFirstOutPort() throws ConnectionError {
		if (firstOut == null) {
			throw new ConnectionError("No output port on " + this);
		}
		return firstOut;
	}

    @Override
	public OutPort getOutPort(String sop) {
		return outPortHM.get(sop);
	}

	public void addRegime(RegimeStateInstance rsi) {
		hasRegimes = true;
		if (regimeHM == null) {
			regimeHM = new HashMap<String, RegimeStateInstance>();
		}
		regimeHM.put(rsi.getID(), rsi);
		// E.info("state instance added regime " + rsi.getID());
		if (rsi.isInitial()) {
			activeRegime = rsi;
		}

	}

	public void receiveRegimeEvent(String name) throws RuntimeError {
		activeRegime.receiveEvent(name);
	}

	public boolean hasVariable(String s) {
		return varHM.containsKey(s);
	}

	public DoublePointer getVariablePtr(String s) {
		return varHM.get(s);
	}

    @Override
	public ArrayList<StateRunnable> getStateInstances(String path) throws ConnectionError, ContentError, RuntimeError {
		//E.info("Getting instances: " + path + " relative to " + this);
		ArrayList<StateRunnable> ret = quietGetStateInstances(path);
		if (ret == null) {
			throw new ConnectionError("No such set of instances " + path + " relative to " + this + " or its ancestors");
		}
		return ret;
	}

    @Override
	public ArrayList<StateRunnable> quietGetStateInstances(String path) throws ConnectionError, ContentError, RuntimeError {
 		
		ArrayList<StateRunnable> ret = null;
		if (hasChildren && childHM.containsKey(path)) {
			E.info("QUERY - using path twice?");
			ret = childHM.get(path).quietGetStateInstances(path);

		} else if (hasMulti && multiHM.containsKey(path)) {
			ret = multiHM.get(path).getStateInstances();
			
		} else {
			if (multiA != null) {
				for (MultiInstance mi : multiA) {
 					if (mi.hasID(path)) {
						ret = mi.getChildByID(path).getStateInstances();
 					}
					if (ret != null) {
						break;
					}
				}
			}
		}

		if (ret == null && parent != null) {
 			ret = parent.quietGetStateInstances(path);
		}  
		return ret;
	}

    @Override
	public ArrayList<StateRunnable> getStateInstances() throws ConnectionError, ContentError, RuntimeError {
		checkBuilt();
		ArrayList<StateRunnable> ret = null;
 		if (singleAMI) {
			ret = onlyAMI.getStateInstances();
 
		} else if (singleIS) {
			ret = onlyIS.getItems();

		} else {
			throw new ConnectionError("Can't get anon instances on " + this);
		}
		return ret;
	}

    @Override
	public void checkBuilt() throws ConnectionError, ContentError, RuntimeError {
		// E.info("building " + this);
 		if (!built) {
 			stateType.build(this);
		}

		if (childA != null) {
			for (StateRunnable csi : childA) {
				csi.checkBuilt();
			}
		}

		
		if (stateListChildren != null) {
 			for (StateListChild slc : stateListChildren) {
 				slc.getInstance().checkBuilt();
			}
		}
		
		/*
		if (multiA != null) {
 			for (MultiInstance mi : multiA) {
 				for (StateInstance si : mi.getStateInstances()) {
					si.checkBuilt();
				}
			}

		}
		*/
	}

	public int getMultiInstanceCount() {
		int ret = 0;
		if (instanceSetHM != null) {
			ret = instanceSetHM.size();
		}
		return ret;
	}
	
    @Override
	public boolean hasSingleMI() {
		return singleAMI;
	}

    @Override
	public MultiInstance getSingleMI() {
		return onlyAMI;
	}

	public void addInstanceSet(String s) {
		InstanceSet<StateRunnable> newIS = new InstanceSet<StateRunnable>(s, this);
		addInstanceSet(newIS);
	}

	public void addInstanceSet(InstanceSet<StateRunnable> newIS) {
		if (instanceSetHM == null) {
			instanceSetHM = new HashMap<String, InstanceSet<StateRunnable>>();
		}
		instanceSetHM.put(newIS.getName(), newIS);
		if (onlyIS == null) {
			onlyIS = newIS;
			singleIS = true;
		} else {
			singleIS = false;
		}
	}

    @Override
	public InstanceSet<StateRunnable> getInstanceSet(String col) {
		return instanceSetHM.get(col);
	}

	public void addInstancePairSet(String s) {
		InstancePairSet<StateRunnable> newIS = new InstancePairSet<StateRunnable>(s, this);
		addInstancePairSet(newIS);
	}

	public void addInstancePairSet(InstancePairSet<StateRunnable> newIS) {
		if (instancePairSetHM == null) {
			instancePairSetHM = new HashMap<String, InstancePairSet<StateRunnable>>();
		}
		instancePairSetHM.put(newIS.getName(), newIS);
	}

	public InstancePairSet<StateRunnable> getInstancePairSet(String col) {
		return instancePairSetHM.get(col);
	}

	public void coCopy(StateInstance psi) {
		for (String s : psi.varHM.keySet()) {
			varHM.put(s, new DoublePointer(psi.varHM.get(s).get()));
		}

	}

    @Override
	public InstanceSet<StateRunnable> getUniqueInstanceSet() throws ContentError {
		InstanceSet<StateRunnable> ret = null;
		if (singleIS) {
			ret = onlyIS;
		} else if (singleAMI) {
			InstanceSet<StateRunnable> is = onlyAMI.getInstanceSet(this);
			addInstanceSet(is);
			ret = is;
		}
		if (ret == null) {
			String msg = " singleIS=" + singleIS + ", singleAMI=" + singleAMI;
			throw new ContentError("Can't get single instance set from " + this + " " + msg);
		}
		return ret;
	}

	// used by path expressions to match a single section of the path.
	// Expressions and this method should supplant
	// all the other matching methods here
    @Override
	public ArrayList<StateRunnable> getPathInstances(String sel) throws ContentError, ConnectionError, RuntimeError {
		ArrayList<StateRunnable> ret = null;
		if (instanceSetHM != null && instanceSetHM.containsKey(sel)) {
			ret = instanceSetHM.get(sel).getItems();

		} else if (hasChildren && childHM.containsKey(sel)) {
			ret = new ArrayList<StateRunnable>();
			ret.add(childHM.get(sel));
			// POSERR 7 mar 2011
			// ret = childHM.get(sel).getStateInstances();

		} else if (hasMulti && multiHM.containsKey(sel)) {
			ret = multiHM.get(sel).getStateInstances();

		} else if (multiA != null) {
			for (MultiInstance mi : multiA) {
				if (mi.hasID(sel)) {
					ret = mi.getChildByID(sel).getStateInstances();
					break;
				}
			}
		}
		if (ret == null && parent != null) {
			ret = parent.getPathInstances(sel);
		}

		if (ret == null) {
			throw new ContentError("cant find '" + sel + "' in " + this);
		}
		return ret;
	}

    @Override
	public double getFloatProperty(String sel) throws ContentError {

		// TODO this isn't quite right: varHM contains the fixed params (from
		// uclass) as well as
		// private local variables. Here we just want the uclass ones, and any
		// instance properties

		double ret = quietGetFloatProperty(sel);
		if (Double.isNaN(ret) || Double.isInfinite(ret)) {
			ret = parent.quietGetFloatProperty(sel);
		}

		if (Double.isNaN(ret) || Double.isInfinite(ret)) {
			throw new ContentError("no such property " + sel + " in " + this);
		}
		return ret;
	}

    @Override
	public double quietGetFloatProperty(String sel) throws ContentError {
		double ret = Double.NaN;
		if (varHM != null && varHM.containsKey(sel)) {
			ret = varHM.get(sel).get();
		}
		return ret;
	}

	public void startArray(String snm) {
		InstanceSet<StateRunnable> iset = new InstanceSet<StateRunnable>(snm, this);
		addInstanceSet(iset);
	}

	public void addToArray(String snm, StateRunnable pc) {
		getInstanceSet(snm).add(pc);

	}

	public void setWork(String string, Object wk) {
		work = wk;
	}

    @Override
	public Object getWork() {
		return work;
	}

	public String getTypeParam(String satt) throws ContentError {
		return stateType.getPropertyStringValue(satt);
	}

	public boolean hasTypeParam(String satt) {
		return stateType.hasPropertyString(satt);
	}

	public HashMap<String, MultiInstance> getMultiHM() {
		return multiHM;
	}

	@Override
	public Object getComponentID() {
		return stateType.getComponentID();
	}
	

    @Override
	public String getDimensionString(String fld) throws ContentError {
		return stateType.getDimensionString(fld);
	}
	
	
}

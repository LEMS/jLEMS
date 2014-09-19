package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.display.LineDisplay;
import org.lemsml.jlems.core.logging.E;
 

public class RegimeStateInstance {

	ComponentRegime uclass;
	
	String id;
	
	HashMap<String, DoublePointer> varHM;
 	
	// TODO only use these if there is more than one;
 
	  
	HashMap<String, RegimeStateInstance> pathSIHM;
	HashMap<String, ArrayList<RegimeStateInstance>> pathAHM;
	 
	HashMap<String, ActionBlock> eventInHM = null;
	
	
	ArrayList<DestinationMap> dmaps; 
	
	StateInstance parent;
	 
	public RegimeStateInstance(ComponentRegime uc, StateInstance par) {
		uclass = uc;
		id = uc.getName(); // TODO id or name?
		parent = par;
	}
	
	public StateInstance getParent() {
		return parent;
	}
	
	public String getID() {
		return id;
	}
	
	
    @Override
	public String toString() {
		return "Instance of " + uclass.toString();
	}
	
	public void advance(StateRunnable parent, double t, double dt) throws RuntimeError {
		uclass.advance(this, parent, t, dt);
	}
	
	public void enter() throws RuntimeError {
		uclass.enter(this);	
	}

	public void setVariables(ArrayList<String> vars) {
		varHM = new HashMap<String, DoublePointer>();
		for (String s : vars) {
			checkPut(s, 0.);
		}
	}
	
	private void checkPut(String snm, double dval) {
		if (parent.hasVariable(snm)) {	
		//	E.info("XXX regime state instance usiong variable from parent " + snm);
			varHM.put(snm, parent.getVariablePtr(snm));
		} else {
		//	E.info(("YYY rsi got local var " + snm));
			varHM.put(snm, new DoublePointer(dval));
		}
	}
	
	
	public void setIndependents(ArrayList<String> vars) {
		for (String s : vars) {
			checkPut(s, 0.); 
		}
	}
	
	public void setExpressionDerived(ArrayList<ExpressionDerivedVariable> exderiveds) {
		for (ExpressionDerivedVariable edv : exderiveds) {
			checkPut(edv.getVariableName(), 0.0);
		}
	}
	
	
	public void setFixeds(ArrayList<FixedQuantity> fqs) {
		for (FixedQuantity fq : fqs) {
			checkPut(fq.getName(), fq.getValue());
		}
	}
	

	public HashMap<String, DoublePointer> getVarHM() {
		return varHM;
	}

  

	public void sendFromPort(String sop) throws RuntimeError {
		 parent.getOutPort(sop).send();
	}


	 
	public String stateString() {
		return varHM.toString();
	}

	public void exportState(String pfx, double t, LineDisplay ld) {
		for (String s : varHM.keySet()) {
			ld.addPoint(pfx + s, t, varHM.get(s).get());
		}
	}

	public StateWrapper getWrapper(String snm) {
		StateWrapper ret = null;
		if (varHM.containsKey(snm)) {
			ret = new StateWrapper(this, snm);
		}
		return ret;
	}
			
	 

	public double getVariable(String varname) {
		return varHM.get(varname).get();
	}

	 
	 
	
	public void addPathStateInstance(String pth, RegimeStateInstance pl) {
		if (pathSIHM == null) {
			pathSIHM = new HashMap<String, RegimeStateInstance>();
		}
		pathSIHM.put(pth, pl);
	}


	public RegimeStateInstance getPathStateInstance(String pth) {
		return pathSIHM.get(pth);
	}

	 
	

	public ArrayList<RegimeStateInstance> getPathStateArray(String pth) {
		return pathAHM.get(pth);
	}

	 

	public void setVariable(String vnm, double pval) {
		varHM.get(vnm).set(pval);
	}
 

	public void postBuild(RegimeStateInstance root) throws ConnectionError {
		E.missing();
	}

	public ComponentRegime getComponentRegime() {
		return uclass;
	}

 
	public void addPostBuilder(AbstractPostBuilder be) {
		E.missing();
	}

 
	public StateRunnable getChild(String snm) throws ConnectionError {
		E.missing();
		return null;
	}

	 
	public void postBuild(StateInstance root) throws ConnectionError {
		E.missing();
	}

	public boolean isInitial() {
		return uclass.isInitial();
	}

	public void transitionTo(String newregime) throws RuntimeError {
		 parent.transitionTo(newregime);
		
	}

	public void addInputPort(String s, ActionBlock actionBlock) {
		if (eventInHM == null) {
			eventInHM = new HashMap<String, ActionBlock>();
		}
		eventInHM.put(s, actionBlock);	
	}


	public InPort getFirstInPort() {
		// TODO shouldn't need this in the interface?
		E.missing();
		return null;
	}


	public InPort getInPort(String portId) {
		// TODO shouldn't need this in the interface?
		E.missing();
		return null;
	}

	
	public void receiveEvent(String s) throws RuntimeError {
		if (eventInHM != null && eventInHM.containsKey(s)) {
			ActionBlock ab = eventInHM.get(s); 
			ab.run(this);
		} else {
			// E.info("regime " + uclass.getName() + " ignoring event " + s);
		}
	}

	
	public void addOutputPort(OutPort orMakeOutputPort) {
		// TODO we don't actually use this: events are sent from the parent 
		// with sendFromPort. 
		// better to give the port ref directly to the event response element
	}

	public String getName() {
		return uclass.getName();
	}

	public void evaluate(StateInstance stateInstance) {
		E.missing();
	}

	 

	
	
}

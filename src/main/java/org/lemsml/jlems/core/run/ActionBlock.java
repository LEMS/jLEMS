package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.eval.DoubleEvaluator;
import org.lemsml.jlems.core.sim.ContentError;

public class ActionBlock {
	
	private final ArrayList<VariableAssignment> assignments = new ArrayList<VariableAssignment>();
	 
	private final ArrayList<String> outPorts = new ArrayList<String>();
	
	
	public boolean doesTransition = false;
	public String transitionTo;
	
	
	public void addAssignment(String name, DoubleEvaluator das) {	
		assignments.add(new VariableAssignment(name, das));	
	}

	
	public void addVariableAssignment(VariableAssignment va) {
		assignments.add(va);
	}
	
	public void addEventOut(String portName) {
		outPorts.add(portName);	
	}

	// MUSTDO - call this
	public void setTransitionTo(String st) {
		transitionTo = st;
		doesTransition = true;
	}

	public ArrayList<VariableAssignment> getAssignments() {
		return assignments;
    }

	public void addVarsTo(ArrayList<String> vars) {
		for (VariableAssignment va : assignments) {
			String svn = va.getVarName();
			if (vars.contains(svn)) {
				// nothing to do
			} else {
				vars.add(svn);
			}
		}
	}


	public void addPortsTo(ArrayList<String> opa) {
		 opa.addAll(outPorts);
		
	}

    public void run(StateInstance uin) throws RuntimeError {
        //E.info("Running action blocks for "+ uin.getID());
        HashMap<String, DoublePointer> varHM = uin.getVarHM();
        for (VariableAssignment vass : assignments) {
            double v = vass.evalptr(varHM);
            varHM.get(vass.varname).set(v);
        }
        for (String sop : outPorts) {
            uin.sendFromPort(sop);
        }
    }

	
	public void run(RegimeStateInstance rsi) throws RuntimeError {
		HashMap<String, DoublePointer> varHM = rsi.getVarHM();
		for (VariableAssignment vass : assignments) {
			varHM.get(vass.varname).set(vass.evalptr(varHM));
		}
		for (String sop : outPorts) {
			rsi.sendFromPort(sop);
		}
		if (doesTransition) {
			rsi.transitionTo(transitionTo);
		}
	}

	public void addTransition(String regime) throws ContentError {
		if (doesTransition) {
			throw new ContentError("multiple transitions defined in an action. Only one is allowed.");
		} else {
			doesTransition = true;
			transitionTo = regime;
		}
	}

	public ActionBlock makeCopy() {
		ActionBlock ret = new ActionBlock();
		ret.doesTransition = doesTransition;
		ret.transitionTo = transitionTo;
		for (String s : outPorts) {
			ret.addEventOut(s);
		}
		for (VariableAssignment a : assignments) {
			ret.addVariableAssignment(a.makeCopy());
		}
		return ret;
		
	}
	
}

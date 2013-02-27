package org.lemsml.jlems.core.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.DoubleEvaluator;
import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.run.ActionBlock;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.LemsCollection;

public class PointResponse {
	
	
	public LemsCollection<StateAssignment> stateAssignments = new LemsCollection<StateAssignment>();
	 
	public LemsCollection<EventOut> eventOuts = new LemsCollection<EventOut>();
	 
	public LemsCollection<Transition> transitions = new LemsCollection<Transition>();
	 
	public void supResolve(Dynamics bhv, LemsCollection<StateVariable> stateVariables, HashMap<String, Valued> valHM, Parser parser) throws ContentError, ParseError {
	 
		for (StateAssignment sa : stateAssignments) {
			sa.resolve(stateVariables, valHM, parser);
		}
		for (EventOut eo : eventOuts) {
			eo.resolve(bhv.getComponentType());
		}
		
		for (Transition t : transitions) {
			t.resolve(bhv);
		}
	
	}
 
	public void addStateAssignment(String vnm, String val) {
		StateAssignment sa = new StateAssignment();
		sa.setVariable(vnm);
		sa.setValue(val);
	}
	
	
	public LemsCollection<StateAssignment> getStateAssignments() {
		return stateAssignments;
	}

    public LemsCollection<EventOut> getEventOuts() {
            return eventOuts;
    }

    public LemsCollection<Transition> getTransitions() {
        return transitions;
    }

    
    public void addTransition(Transition t) {
    	transitions.add(t);
    }

	
	public ActionBlock makeEventAction(HashMap<String, Double> fixedHM) throws ContentError {
		 ActionBlock ret = new ActionBlock();
		 for (StateAssignment sa : stateAssignments) {
			 
			 ParseTree pt = sa.getParseTree();
			 
			 DoubleEvaluator dase = pt.makeFloatFixedEvaluator(fixedHM); 
			 
			 ret.addAssignment(sa.getStateVariable().getName(), dase);
		 } 
		 for (EventOut eout : eventOuts) {
			 ret.addEventOut(eout.getPortName());
		 }
		 
		 for (Transition t : transitions) {
			 ret.addTransition(t.getRegime());
		 }
		 
		return ret;
	}


	public void checkEquations(HashMap<String, Dimensional> dimHM) throws ContentError {
		 for (StateAssignment sa : stateAssignments) {
			 sa.checkDimensions(dimHM);
		 }
		
	}

	public void copyInto(PointResponse ret) {
		 for (StateAssignment sa : stateAssignments) {
			 ret.addStateAssignment(sa.makeCopy());
		 }
		 for (Transition t : transitions) {
			 ret.addTransition(t.makeCopy());
		 }
		 for (EventOut eo : eventOuts) {
			 ret.addEventOut(eo.makeCopy());
		 }
	}

	
	private void addEventOut(EventOut eo) {
		eventOuts.add(eo);
	}

	private void addStateAssignment(StateAssignment sa) {
		stateAssignments.add(sa);
	}
}

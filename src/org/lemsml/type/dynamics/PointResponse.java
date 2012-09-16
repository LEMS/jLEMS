package org.lemsml.type.dynamics;

import java.util.HashMap;

import org.lemsml.eval.DBase;
import org.lemsml.expression.Dimensional;
import org.lemsml.expression.DoubleEvaluable;
import org.lemsml.expression.ParseError;
import org.lemsml.expression.Parser;
import org.lemsml.expression.Valued;
import org.lemsml.run.ActionBlock;
import org.lemsml.type.LemsCollection;
import org.lemsml.util.ContentError;

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
	
	
	public LemsCollection<StateAssignment> getStateAssignments() {
		return stateAssignments;
	}

    public LemsCollection<EventOut> getEventOuts() {
            return eventOuts;
    }

    public LemsCollection<Transition> getTransitions() {
        return transitions;
    }

    

	
	public ActionBlock makeEventAction(HashMap<String, Double> fixedHM) throws ContentError {
		 ActionBlock ret = new ActionBlock();
		 for (StateAssignment sa : stateAssignments) {
			 DoubleEvaluable dase = sa.getEvaluable();
			 DBase das = new DBase(dase.makeFixed(fixedHM));
			 ret.addAssignment(sa.getStateVariable().getName(), das);
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
}

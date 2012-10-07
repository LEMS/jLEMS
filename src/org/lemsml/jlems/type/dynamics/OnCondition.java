package org.lemsml.jlems.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.expression.BooleanEvaluable;
import org.lemsml.jlems.expression.Dimensional;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.expression.Valued;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.LemsCollection;

public class OnCondition extends PointResponse   {
	
	public String test; 

	BooleanEvaluable condition;

	public Trigger trigger;
	
	
    public OnCondition() {
    }

    public OnCondition(String test) {
        this.test = test;

    }
 
	
	public void resolve(Dynamics bhv, LemsCollection<StateVariable> stateVariables, HashMap<String, Valued> valHM, Parser parser) throws ContentError, ParseError {
		if (trigger != null) {
			test = trigger.getCondition();
		}
		
		try {
			condition = parser.parseCondition(test);
			condition.setValues(valHM);
		} catch (ContentError ce) {
			E.error("error processing condition: " + ce.getMessage() + "  test condition: " + test);
		}
		supResolve(bhv, stateVariables, valHM, parser);
	}


	public BooleanEvaluable getEvaluable() {
		return condition;
	}
	
	
	public void checkConditionDimensions(HashMap<String, Dimensional> dimHM) throws ContentError {
		condition.checkDimensions(dimHM);
	}

	
	public void addStateAssignment(StateAssignment sa) {
		 stateAssignments.add(sa);
	}

	
	public void addEventOut(EventOut eo) {
		eventOuts.add(eo);
	}
	
}

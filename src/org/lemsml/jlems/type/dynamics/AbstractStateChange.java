package org.lemsml.jlems.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.expression.Dimensional;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.ParseTree;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.expression.Valued;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Dimension;
import org.lemsml.jlems.type.LemsCollection;

public abstract class AbstractStateChange extends ExpressionValued {

	public String variable;
	
	private StateVariable r_variable;
	 
	ParseTree parseTree; 
 
	
	public AbstractStateChange() {
		super();
	}
	
   public AbstractStateChange(String vnm) {
	   super();
	   variable = vnm;
   }
   
   
   public void setVariable(String s) {
	   variable = s;
   }
   
   public String getVariable() {
	   return variable;
   }
   
	
	public void resolve(LemsCollection<StateVariable> stateVariables, HashMap<String, Valued> valHM, Parser parser) throws ContentError, ParseError {
		super.extract();
		
		if (stateVariables.hasName(variable)) {
			r_variable = stateVariables.getByName(variable);
		} else {
			StringBuilder error = new StringBuilder("Can't find variable " + variable+" for state variable "+ r_variable+"\nState variables:");
			for (StateVariable sv:stateVariables) {
				error.append("\n    "+sv.getName()+": "+sv);
			}
			throw new ContentError(error.toString());
		}
		parseTree = parser.parseExpression(value);
	}
	
	
	public StateVariable getStateVariable() {
		return r_variable;
	}
	 
 
	public ParseTree getParseTree() {
		 return parseTree;
	}
	
    public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError {
       	 
            Dimensional drhs = parseTree.getDimensionality(dimHM);
            if (drhs.isAny()) {
                // fine - zero can be assigned to anything
            } else {
                Dimensional dsv = r_variable.getDimension();
                Dimensional dlf = getStateVariableDimensionMultiplier();

                Dimensional dl = dsv.getTimes(dlf);
                Dimensional dres = drhs.getDivideBy(dl);

                if (dres.isDimensionless()) {
                    // OK
                } else {
                    E.oneLineError("Dimension mismatch in equation: " + variable + " = " + value + ". Residual dimension: " + dres);
                    E.info("Dimension of " + variable + ": " + dsv + ", multiplier=" + dlf + ", left=" + dl + ", rhs=" + drhs);
                    E.info("All:" + dimHM);
                }
            }
       

    }

	
	public abstract Dimension getStateVariableDimensionMultiplier();
	
	
}

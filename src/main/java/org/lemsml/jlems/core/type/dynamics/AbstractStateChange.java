package org.lemsml.jlems.core.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Dimension;
import org.lemsml.jlems.core.type.LemsCollection;

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
			StringBuilder sb = new StringBuilder();
			sb.append("Can't find state variable " + variable + " in " + this + "\n");
			sb.append("Known state variables:\n");
			for (StateVariable sv:stateVariables) {
				sb.append("    " + sv.getName() + ": " + sv + "\n");
			}
			throw new ContentError(sb.toString());
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

	public void copyInto(AbstractStateChange ret) {
		 ret.variable = variable;
		super.copyInto(ret);
	}
	
	
}

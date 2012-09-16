package org.lemsml.type.dynamics;

import java.util.HashMap;

import org.lemsml.annotation.Mat;
import org.lemsml.expression.BooleanEvaluable;
import org.lemsml.expression.DoubleEvaluable;
import org.lemsml.expression.ParseError;
import org.lemsml.type.Lems;
import org.lemsml.util.E;

 
import org.lemsml.annotation.Mel;
import org.lemsml.expression.Dimensional;
 
import org.lemsml.expression.Parser;
import org.lemsml.expression.Valued;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Dimension;
import org.lemsml.type.Exposure;
import org.lemsml.type.FillableFrom;
 
import org.lemsml.type.LemsCollection;
import org.lemsml.util.ContentError;
 


public class ConditionalDerivedVariable extends DerivedVariable {

	@Mat(info = "An optional boolean condition. If true, variable is set to \"value\", otherwise \"valueIfFalse\"")
	public String valueCondition;
	@Mat(info = "A fallback value for the variable if \"valueCondition\" is false, otherwise \"value\"")
	public String valueIfFalse;
	BooleanEvaluable evaluableCondition;
	DoubleEvaluable evaluableIfFalse;
	@Mat(info = "A value to be used if the path at select is not found")
	public String onAbsent;

	public String getOnAbsent() {
	    return onAbsent;
	}

	public DoubleEvaluable getEvaluableIfFalse() {
		 return evaluableIfFalse;
	}

	public BooleanEvaluable getEvaluableCondition() {
		 return evaluableCondition;
	}

	
	
	public String getEvalString() {
		StringBuilder sb = new StringBuilder();
		if (value != null) {
			if (valueCondition != null) {
				sb.append("IF (" + valueCondition + ") THEN ");
			}
			sb.append("" + value + "");
			if (valueCondition != null) {
				sb.append(") ELSE " + valueIfFalse + ")");
			}
		} else {
			sb.append(select);
			if (reduce != null) {
				sb.append("  (REDUCE: " + reduce + ")");
			}
		}
		return sb.toString();
	}
  
	
	public void resolve(Lems lems, LemsCollection<Dimension> dimensions, ComponentType type, HashMap<String, Valued> valHM, Parser parser) throws ContentError, ParseError {
		super.resolve(lems, dimensions, type, valHM, parser);
	
        if (value != null) {
       
                if (valueCondition!=null && valueIfFalse!=null){
                    evaluableCondition = parser.parseCondition(valueCondition);
                    evaluableCondition.setValues(valHM);

                    evaluableIfFalse = parser.parseExpression(valueIfFalse);
                    evaluableIfFalse.setValues(valHM);
                }
               
                evaluable = parser.parseExpression(value);
                evaluable.setValues(valHM);
      
        }

        
        if (onAbsent != null || valueCondition != null || valueIfFalse != null) {
        	// a peer to the MathInline element should do all this

        	if (E.getDebug()) {
        		// PG: to hide this warning in libNeuroML...
        		E.oneOffOneLineWarning("Using deprecated constructs in DerivedVariable: (onAbsent, valueCondition, valueIfFalse)");
        	}
        }
	}

 
}

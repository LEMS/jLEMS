package org.lemsml.jlems.core.type;

import java.util.HashMap;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info="A constant quantity: like a parameter for which the value is supplied in the class definition itself rather " +
		"than when a component is defined.")
public class Constant implements Named, Valued  {
	@ModelProperty(info="A readable name for the constant.")
	public String name;
	
	public String description;
	
	@ModelProperty(info="The symbol used in expressions to refer to this constant.")
	public String symbol;
	
	@ModelProperty(info="The value of a constant must be a plain number (no units) giving the SI " +
			"magnitude of the quantity or an expression involving only plain numbers or other constants.")
	public String value;

	@ModelProperty(info="")
	public String dimension;

	public Dimension r_dimension;

	private double p_value;
	
    

    public String toString() {
    	String ret = "Constant: " + name + " dimension=" + dimension + " value=" + value + " resolvedto=" + r_dimension;
    	return ret;
    }
  
	public void resolve(LemsCollection<Dimension> dimensions, LemsCollection<Unit> units, Parser parser, HashMap<String, Double> valHM) throws ContentError, ParseError {
		Dimension d = dimensions.getByName(dimension);
		if (d != null) {
			r_dimension = d;
		//	E.info("resolved param " + name);
		} else {
			throw new ContentError("no such dimension: " + dimension);
		}		
		
		if (value.indexOf("*") > 0 || value.indexOf("/") > 0) {
			ParseTree pt = parser.parseExpression(value);
			p_value = pt.makeFloatEvaluator().evalD(valHM);
			
		} else {
			if (units != null) {
				DimensionalQuantity dq = QuantityReader.parseValue(value, units);
				p_value = dq.getValue();
			} else {
				p_value = Double.parseDouble(value);
			}
		}
	}
	
	public String getStringValue() {
		return value;
	}
	
	public double getValue() {
		return p_value;
	}
	
	public String getName() {
		return name;
	}

	public Dimension getDimension() {
		return r_dimension;
	}
 
	public String getSymbol() {
		 return symbol;
	}
	
}

package org.lemsml.jlems.type;

import java.util.HashMap;

import org.lemsml.jlems.annotation.ModelProperty;
import org.lemsml.jlems.annotation.ModelElement;
import org.lemsml.jlems.expression.DoubleEvaluable;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.expression.Valued;
import org.lemsml.jlems.sim.ContentError;

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
	
    public Constant() {
        
    }

    public Constant(String name, Dimension dimension, String value) {
        this.name = name;
        this.dimension = dimension.getName();
        this.r_dimension = dimension;
        this.value = value;
    }

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
			DoubleEvaluable evaluable = parser.parseExpression(value);
			p_value = evaluable.evalD(valHM);
			
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

	@Override
	public boolean isFixed() {
		return true;
	}

	public String getSymbol() {
		 return symbol;
	}
	
}

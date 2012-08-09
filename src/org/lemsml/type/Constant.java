package org.lemsml.type;

import java.util.HashMap;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.expression.DoubleEvaluable;
import org.lemsml.expression.ParseError;
import org.lemsml.expression.Parser;
import org.lemsml.expression.Valued;
import org.lemsml.util.ContentError;

@Mel(info="A constant quantity: like a parameter for which the value is supplied in the class definition itself rather " +
		"than when a component is defined.")
public class Constant implements Named, Valued  {
	@Mat(info="")
	public String name;
	
	public String description;
	
	@Mat(info="")
	public String symbol;
	
	@Mat(info="")
	public String value;

	@Mat(info="")
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

package org.lemsml.jlems.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.annotation.ModelElement;
import org.lemsml.jlems.annotation.ModelProperty;
import org.lemsml.jlems.eval.BooleanEvaluator;
import org.lemsml.jlems.eval.ConditionalDBase;
import org.lemsml.jlems.eval.DoubleEvaluator;
import org.lemsml.jlems.expression.Dimensional;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.ParseTree;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.expression.Valued;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Dimension;
import org.lemsml.jlems.type.Exposure;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.LemsCollection;
  
public class ConditionalDerivedVariable implements Valued {

	@ModelProperty(info="")
	public String name;

	 
	@ModelProperty(info="")
	public String dimension;
	public Dimension r_dimension;
  
	@ModelProperty(info="")
	public String exposure;   
	public Exposure r_exposure;
 
	
	public boolean required = true;
	
	
	public LemsCollection<Case> cases = new LemsCollection<Case>();
	
	
	public ConditionalDerivedVariable() {
		super();
    }

    public ConditionalDerivedVariable(String s) {
    	super();
    	name = s;
    }
    
     
	
	public String getName() {
		return name;
	}
	
	 
	
        @Override
	public String toString() {
		return "ConditionalDerivedVariable " + name;
	}

 
	
	public void resolve(Lems lems, LemsCollection<Dimension> dimensions, ComponentType type, HashMap<String, Valued> valHM, Parser parser) throws ContentError, ParseError {
	 
        if (dimension != null) {
            Dimension d = dimensions.getByName(dimension);
            if (d != null) {
                r_dimension = d;
                //	E.info("resolved param " + name);
            } else {
                throw new ContentError("no such dimension: " + dimension);
            }
        }

        for (Case c : cases) {
        	c.resolve(lems, dimensions, parser);
        }
        

        if (exposure != null) {
        	r_exposure = type.getExposure(exposure);
        }
     
	}

 
	public double getValue() {
		return Double.NaN;
	}

	 
	public boolean isFixed() {
		return false;
	}

    public Dimension getDimension() {
        return r_dimension;
    }

    
 


	public void fillFrom(Object obj) {
		ConditionalDerivedVariable dsrc = (ConditionalDerivedVariable)obj;
		if (dimension == null) {
			dimension = dsrc.dimension;
		}
		if (r_dimension == null) {
			r_dimension = dsrc.r_dimension;
		}
	}

	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		Dimensional ret = null;
		
		if (cases.size() > 0) {
			ret = cases.get(0).getDimensionality(dimHM);

		} else if (r_dimension != null) {
			ret = r_dimension;
		} else {
			throw new ContentError("derived variable has no dimension: " + name);
		}
		return ret;
	}
	
	 

	public boolean hasExposure() {
		boolean ret = false;
		if (r_exposure != null) {
			ret = true;
		}
		return ret;
	}
	
	public Exposure getExposure() {
		return r_exposure;
	}

	public boolean isRequired() {
		return required;
	}

	public void setName(String nm) {
		name = nm;
	}
	
	public void setDimension(String dn) {
		dimension = dn;
	}

	 

	public void setExposure(String s) {
		exposure = s;
	}
 

	public ConditionalDerivedVariable makeCopy() {
		ConditionalDerivedVariable ret = new ConditionalDerivedVariable();
		ret.name = name;
	 
		ret.exposure = exposure;
		ret.dimension = dimension;
	
		for (Case c : cases) {
			ret.addCase(c.makeCopy());
		}
		
		return ret;
	}

	private void addCase(Case c) {
		cases.add(c);
	}

	public DoubleEvaluator makeFloatFixedEvaluator(HashMap<String, Double> fixedHM) throws ContentError {
		ConditionalDBase ret = new ConditionalDBase();
		for (Case c : cases) {
			BooleanEvaluator bev = c.conditionParseTree.makeBooleanFixedEvaluator(fixedHM);
			DoubleEvaluator dev = c.valueParseTree.makeFloatFixedEvaluator(fixedHM);
			
			if (bev == null || dev == null) {
				throw new ContentError("Null evaluators in condition: " + bev + " " +dev);
			}
			
			ret.addCondition(bev, dev);
		}
		return ret;
	}

	 

}

package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info="A Unit asociates a symbol with a dimension and a power of ten. For non-metric units a " +
		"scale can be provided, as in '1 inch = 0.0254 m'. In this case there is a degeneracy between " +
		"the power and the scale which is best resolved by not using the two together. The offset " +
		"parameter is available for units which are not zero-offset, such as farenheit.")
public class Unit implements PseudoNamed, Summaried, DataMatchable {

    public static final String NO_UNIT = "none";
    
    @ModelProperty(info="As with constants, units are only referred to within expressions using their symbols, " +
    		"so the name is just for readability.")
    public String name;
    
    @ModelProperty(info="The symbol is used to refer to this unit inside compound expressions coutaining a number and " +
    		"a unit symbol. Such expressions can only occur on the right hand side of assignments statements.")
    public String symbol;

    @ModelProperty(info="Reference to the dimension for this unit")
    public String dimension;
    private Dimension r_dimension;

    @ModelProperty(info="Power of ten")
    public int power = 0;
    
    @ModelProperty(info="Scale, only to be used for scales which are not powers of ten")
    public double scale = 1;
    
    @ModelProperty(info="Offset for non zero-offset units")
    public double offset = 0;

    
    
    
    public Unit() {
    	// TODO - only one
    }

    public Unit(String nm, String sb, Dimension dim) {
    	name = nm;
        symbol = sb;
        r_dimension = dim;
        dimension = dim.getName();
    }

   
    public void setOffset(double d) {
    	offset = d;
    }
    
    public void setScaleFactor(double f) {
    	scale = f;
    }
    
    public void setPower(int p) {
    	power = p;
    }
    
    
    public boolean dataMatches(Object obj) {
        boolean ret = false;
        if (obj instanceof Unit) {
            Unit u = (Unit) obj;
            if (symbol.equals(u.symbol) && power == u.power && dimension.equals(u.dimension)) {
                if (Math.abs(scale - u.scale) / (scale + u.scale) < 1.e-9) {
                    if (offset == 0 && u.offset == 0) {
                        ret = true;
                    }
                    else if(Math.abs(offset - u.offset) / (offset + u.offset) < 1.e-9) {
                        ret = true;
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return "Unit name=" + summary();
    }

    public String summary() {
        return name + ": " + symbol + ", dimension=" + dimension + ", scale=" + scale + ", power=" + power+", offset="+offset;
    }

    

    public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {  	
    	if (name == null) {
            name = symbol;
        }
        Dimension d = dimensions.getByName(dimension);
        if (d != null) {
            r_dimension = d;
            //	E.info("resolved unit " + name + " " + symbol);
        } else {
            throw new ContentError("no such dimension: " + dimension);
        }

    }

    public Dimension getDimension() {
        return r_dimension;
    }

    /*
    public double getMultiplier() {
        return scale * Math.pow(10, powTen);
    }*/

    public double getAbsoluteValue(double val) {
        return ((val + offset) * scale * Math.pow(10, power));
    }

    
    public double getLocalValue(double siVal) {
        return siVal / (scale * Math.pow(10, power)) - offset;
    }

    
    public double getLocalizingFactor() {
    	return 1. / (scale * Math.pow(10, power));
    }
    
    public double getLocalizingOffset() {
    	return offset;
    }
    

    public String getPseudoName() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public int getPowTen() {
        return power;
    }

    public double getScale() {
        return scale;
    }

    public String getSymbol() {
        return symbol;
    }


}

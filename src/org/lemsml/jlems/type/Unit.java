package org.lemsml.jlems.type;

import org.lemsml.jlems.util.ContentError;


public class Unit implements PseudoNamed, Summaried, DataMatchable {

    public static final String NO_UNIT = "none";

    public String name;
    
    public String symbol;
    
    public String dimension;
    private Dimension r_dimension;
    
    @Deprecated
    public int powTen = 0;
    
    public int power = 0;
    
    public double scale = 1;
    
    public double offset = 0;

    
    
    
    public Unit() {
    }

    public Unit(String name, String symbol, Dimension dimension) {
        this(name, symbol, dimension, 0, 1);
    }

    public Unit(String name, String symbol, Dimension dimension, int powTen, double scale) {
    	this(name, symbol, dimension, powTen, scale, 0);
    }
    
    
    public Unit(String name, String symbol, Dimension dimension, int pt, double scale, double offset) {
        this.name = name;
        this.symbol = symbol;
        this.dimension = dimension.getName();
        this.r_dimension= dimension;
        this.powTen = pt;
        this.power = pt;
        this.scale = scale;
        this.offset = offset;
    }

    
    
    
    public boolean dataMatches(Object obj) {
        boolean ret = false;
        if (obj instanceof Unit) {
            Unit u = (Unit) obj;
            if (symbol.equals(u.symbol) && power == u.power && dimension.equals(u.dimension)) {
                if (Math.abs(scale - u.scale) / (scale + u.scale) < 1.e-9) {
                    if (offset == 0 && u.offset == 0) {
                        ret =true;
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
        return name + ": " + symbol + ", dimension=" + dimension + ", scale=" + scale + ", powTen=" + powTen+", offset="+offset;
    }

    

    public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
    	if (powTen != 0) {
    		power = powTen;
    	}
    	
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

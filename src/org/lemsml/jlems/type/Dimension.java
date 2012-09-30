package org.lemsml.jlems.type;

import org.lemsml.jlems.expression.Dimensional;
import org.lemsml.jlems.util.E;

public class Dimension implements Named, Summaried, DataMatchable, Dimensional {

    public static final String NO_DIMENSION = "none";

    public String name;
    public int m;  // Mass
    public int l;  // Length
    public int t;  // Time
    public int i;  // Current
    public int k;  // Temperature
    public int n;  // Amount of substance
    
    @Deprecated
    public int c;  // old form for amount of substance - still existsin NeuroML xml 
    
    private double dval = Double.NaN; // bit messy, just for constant powers

    
    
    public final static Dimension TIME_DIMENSION = new Dimension("time", 0, 0, 1, 0);
    
    
    
    public Dimension() {
    }

    public Dimension(String sn) {
        name = sn;
    }

    public Dimension(String sn, int am, int al, int at, int ai) {
        name = sn;
        m = am;
        l = al;
        t = at;
        i = ai;
        k = 0;
        n = 0;
    }

    public Dimension(String sn, int am, int al, int at, int ai, int ak, int ac) {
        name = sn;
        m = am;
        l = al;
        t = at;
        i = ai;
        k = ak;
        n = ac;
    }

    
    public void checkDep() {
    	if (c != 0) {
    		n = c;
    		c = 0;    		
    		E.warning("Dimension " + this + " expressed with deprecated attribute 'c': use 'n' instead");
    	}
    }
    
    
    
    public boolean dataMatches(Object obj) {
    	checkDep();
    	
        boolean ret = false;
        if (obj instanceof Dimension) {
            Dimension d = (Dimension) obj;
            if (m == d.m && l == d.l && t == d.t && i == d.i && k == d.k && n == d.n) {
                ret = true;
            }
        }
        return ret;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Dimension[" + summary()+"]";
    }

    public String summary() {
    	checkDep();
    	String ret = (name != null ? name : " (unnamed) ");
    	int[] vals = {m, l, t, i, k, n};
    	String[] names= {"m", "l", "t", "i", "k", "n"};
    	int nnz = 0;
    	for (int i = 0; i < vals.length; i++) {
    		if (vals[i] != 0) {
    			ret += " " + names[i] + "=" + vals[i];
    			nnz += 1;
    		}
    	}
    	if (nnz == 0) {
    		ret += " dimensionless";
    	}
    	return ret;
    }

   

    public boolean matches(Dimension d) {
    	checkDep();
    	
        boolean ret = false;
        if (this.equals(d)) {
            ret = true;

        } else if (m == d.m && l == d.l && t == d.t && i == d.i && k == d.k && n == d.n) {
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }

    public Dimension getTimes(Dimensional d) {
    	checkDep();
    	
    	Dimension ret = new Dimension("");
        ret.m = m + d.getM();
        ret.l = l + d.getL();
        ret.t = t + d.getT();
        ret.i = i + d.getI();
        ret.k = k + d.getK();
        ret.n = n + d.getN();
        return ret;
    }

    public Dimensional getDivideBy(Dimensional d) {
    	checkDep();
    	
    	Dimension ret = new Dimension("");
        ret.m = m - d.getM();
        ret.l = l - d.getL();
        ret.t = t - d.getT();
        ret.i = i - d.getI();
        ret.k = k - d.getK();
        ret.n = n - d.getN();
        return ret;
    }

    public boolean isDimensionless() {
    	checkDep();
    	
        boolean ret = false;
        if (m == 0 && l == 0 && t == 0 && i == 0 && k == 0 && n == 0) {
            ret = true;
        }
        return ret;
    }

    public int getI() {
        return i;
    }

    public int getL() {
        return l;
    }

    public int getM() {
        return m;
    }

    public int getT() {
        return t;
    }

    public int getK() {
        return k;
    }

    public int getN() {
    	checkDep();
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setL(int l) {
        this.l = l;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setT(int t) {
        this.t = t;
    }

    

    public boolean matches(Dimensional d) {
    	checkDep();
    	
        boolean ret = false;
        if (m == d.getM() && l == d.getL() && t == d.getT() && i == d.getI() && k == d.getK() && n == d.getN()) {
            ret = true;
        }
        return ret;
    }

    public Dimensional power(double dbl) {
    	checkDep();
    	
        Dimensional ret = null;
        if (dbl - Math.round(dbl) < 1.e-6) {
            int ifac = (int) (Math.round(dbl));
            ret = new Dimension("", ifac * m, ifac * l, ifac * t, ifac * i, ifac * k, ifac * n);

        } else {
            E.missing("Can't work with fractional dimensions yet");
        }
        return ret;
    }

    public boolean isAny() {
        return false;
    }

    public void setDoubleValue(double d) {
        dval = d;
    }

    public double getDoubleValue() {
        return dval;
    }

  
   
}

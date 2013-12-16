package org.lemsml.jlems.core.expression;

import org.lemsml.jlems.core.logging.E;

public class ExprDimensional implements Dimensional {

	int m;
	int l;
	int t;
	int i;
	int k;
	int n;
	int j;
	
	boolean isZero = false;
	
	double doubleValue = Double.NaN; // bit of a hack to get powers through in the case of dimensionless constants
	
	public ExprDimensional() {
		m = 0; 
		l = 0;
		t = 0;
		i = 0;
		k = 0;
		n = 0;
		j = 0;
	}

 
    @Override
	public String toString() {
    	String[] lbls = {"m", "l", "t", "i", "k", "n", "j"};
    	int[] vals = {m, l, t, i, k, n, j};
     	String sd = "";
    	for (int i = 0; i < lbls.length; i++) {
    		if (vals[i] != 0) {
    			if (sd.length() > 0) {
    				sd += ", ";
    			}
    			sd += lbls[i] + "=" + vals[i];
    		}
    	}
    	String ret = "ExprDimensional[" + (sd.length() > 0 ? sd : "dimensionless") + "]";
    	return ret;
    }
     
	
    @Override
	public Dimensional getDivideBy(Dimensional d) {
		ExprDimensional ret = new ExprDimensional();
		ret.m = m - d.getM();
		ret.l = l - d.getL();
		ret.t = t - d.getT();
		ret.i = i - d.getI();
		ret.k = k - d.getK();
		ret.n = n - d.getN();
		ret.j = j - d.getJ();
		return ret;
	}

 
    @Override
	public int getI() {
		return i;
	}
 
    @Override
	public int getL() {
		return l;
	}

 
    @Override
	public int getM() {
		return m;
	}

	 
    @Override
	public int getT() {
		return t;
	}

    @Override
	public int getK() {
		return k;
	}

    @Override
	public int getN() {
		return n;
	}

    @Override
	public int getJ() {
		return j;
	}

 
    @Override
	public Dimensional getTimes(Dimensional d) {
		ExprDimensional ret = new ExprDimensional();
		ret.m = m + d.getM();
		ret.l = l + d.getL();
		ret.t = t + d.getT();
		ret.i = i + d.getI();
		ret.k = k + d.getK();
		ret.n = n + d.getN();
		ret.j = j + d.getJ();
		return ret;
	}
    
    @Override
	public boolean isDimensionless() {
		boolean ret = false;
		if (m == 0 && l == 0 && t == 0 && i == 0 && k == 0 && n == 0 && j == 0) {
			ret = true;
		}
		return ret;
	}

 
    @Override
	public boolean matches(Dimensional d) {
		boolean ret = false;
		if (m == d.getM() && l == d.getL() && t == d.getT() && i == d.getI() && k == d.getK() && n == d.getN() && j == d.getJ()) {
			ret = true;
		}
		return ret;
	}

 
    @Override
	public Dimensional power(double dbl) {
		Dimensional ret = null;
		if (dbl - Math.round(dbl) < 1.e-6) {
			int id = (int)(Math.round(dbl));
			ExprDimensional re = new ExprDimensional();
			re.m = id * m;
			re.l = id * l;
			re.t = id * t;
			re.i = id * i;
			re.k = id * k;
			re.n = id * n;
			re.j = id * j;
			ret = re;
		} else {
			E.missing("Can't work with fractional dimensions yet");
		}
		return ret;
	}

	public void setZero() {
		isZero = true;
		
	}

    @Override
	public boolean isAny() {
		// the constant zero can be of any dimension
		return isZero;
	}

	public void setDoubleValue(double dval) {
		doubleValue = dval;	
	}
    
    @Override
	public double getDoubleValue() {
		return doubleValue;
	}

	public void setT(int j) {
		t = j;
	}
	
}

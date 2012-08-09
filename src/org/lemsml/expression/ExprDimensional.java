package org.lemsml.expression;

import org.lemsml.util.E;

public class ExprDimensional implements Dimensional {

	int m;
	int l;
	int t;
	int i;
	int k;
	int c;
	
	boolean isZero = false;
	
	double doubleValue = Double.NaN; // bit of a hack to get powers through in the case of dimensionless constants
	
	public ExprDimensional() {
		m = 0; 
		l = 0;
		t = 0;
		i = 0;
		k = 0;
		c = 0;
	}

	public ExprDimensional(int am, int al, int at, int ai) {
		m = am;
		l = al;
		t = at;
		i = ai;
        k = 0;
        c = 0;
	}

	public ExprDimensional(int am, int al, int at, int ai, int ak, int ac) {
		m = am;
		l = al;
		t = at;
		i = ai;
		k = ak;
		c = ac;
	}
	
    @Override
	public String toString() {
		return "ExprDim["+ (m != 0 ? " m=" + m : "") + (l != 0 ? " l=" + l : "") + (t != 0 ? " t=" + t : "")
                + (i != 0 ? " i=" + i : "") + (k != 0 ? " k=" + k : "") + (c != 0 ? " c=" + c : "")+ 
                (m==0&&l==0&&t==0&&i==0&&k==0&&c== 0 ? " dimensionless" : "")+"]";
	}
	
	
	public Dimensional getDivideBy(Dimensional d) {
		return new ExprDimensional(m - d.getM(), l - d.getL(), t - d.getT(), i - d.getI(), k - d.getK(), c - d.getN());
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
		return c;
	}

 
	public Dimensional getTimes(Dimensional d) {
		return new ExprDimensional(m + d.getM(), l + d.getL(), t + d.getT(), i + d.getI(), k + d.getK(), c + d.getN());
	}

	 
	public boolean isDimensionless() {
		boolean ret = false;
		if (m == 0 && l == 0 && t == 0 && i == 0 && k == 0 && c == 0) {
			ret = true;
		}
		return ret;
	}

 
	public boolean matches(Dimensional d) {
		boolean ret = false;
		if (m == d.getM() && l == d.getL() && t == d.getT() && i == d.getI() && k == d.getK() && c == d.getN()) {
			ret = true;
		}
		return ret;
	}

 
	public Dimensional power(double dbl) {
		Dimensional ret = null;
		if (dbl - Math.round(dbl) < 1.e-6) {
			int id = (int)(Math.round(dbl));
			ret = new ExprDimensional(id * m, id * l, id * t, id * i, id * k, id * c);
			
		} else {
			E.missing("Can't work with fractional dimensions yet");
		}
		return ret;
	}

	public void setZero() {
		isZero = true;
		
	}

	public boolean isAny() {
		// the constant zero can be of any dimension
		return isZero;
	}

	public void setDoubleValue(double dval) {
		doubleValue = dval;	
	}
	public double getDoubleValue() {
		return doubleValue;
	}
	
}

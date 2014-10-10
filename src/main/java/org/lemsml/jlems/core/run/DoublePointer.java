package org.lemsml.jlems.core.run;

public class DoublePointer {

	double value;
	
	boolean unassigned = false;
	
	public DoublePointer(double d) {
		value = d;
	}

        @Override
        public String toString() {
            String val = value+"";
            if (val.endsWith(".0"))
                val = val.substring(0,val.length()-2);
            return "DP(" + val + ")";
        }
        
	
	public double get() {
		return value;
	}
	
	public double getValue() {
		return value;
	}
	
	
	public void set(double d) {
		value = d;
	}

	public void setUnassigned() {
		unassigned = true;
	}
}

package org.lemsml.run;

public class DoublePointer {

	double value;
	
	public DoublePointer(double d) {
		value = d;
	}

        @Override
        public String toString() {
            return "DP(" + value+")";
        }
        
	
	public double get() {
		return value;
	}
	
	public void set(double d) {
		value = d;
	}
}

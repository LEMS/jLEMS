package org.lemsml.jlems.type;

// NB this is for NineML compatibility - i suspect it is likely to change
// a sending AnalogPort is just an exposure, a reducing one feels like too much overloading 
// and may change in future... Also, reduce_op only applies if mode=reduce which isn't very nice.
public class AnalogPort {

	public String name;
	public String mode;
	public String dimension;
	public String reduce_op;
	

	public String toString() {
		String ret = "AnalogPort: name=" + name + " mode=" + mode + " dimension=" + dimension + " reduce_op=" + reduce_op;
		return ret;
	}
}

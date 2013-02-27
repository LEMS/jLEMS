package org.lemsml.jlems.core.api;

public class BuildExample {

	
	 
	
	
	public void buildExample1() {
		LemsBuilder lb = new LemsBuilder();
		
		// create a new dimension value, set its components in terms of the base dimensions
		DimensionValue dv = new DimensionValue();
		dv.add(BaseDimension.MASS, 1);
		dv.add(BaseDimension.LENGTH, 1);
		dv.add(BaseDimension.TIME, -2);
		
		// then add to the lems builder, with a name for the dimension
		lb.addDimension("acceleration", dv);
		
	
		
	}
	
	
	
	
	
	public static void main(String[] argv) {
		BuildExample be = new BuildExample();
		be.buildExample1();
		
	}
	
	
	
}

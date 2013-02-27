package org.lemsml.jlems.core.expression;


public interface Dimensional {

	int getM();
	int getL();
	int getT();
	int getI();
	
	int getK();
	
	int getN();
	
	Dimensional getTimes(Dimensional d);
	Dimensional getDivideBy(Dimensional d);
	
	 
	
	boolean matches(Dimensional dr);
	boolean isDimensionless();
	Dimensional power(double doubleValue);
	boolean isAny();
	double getDoubleValue();
}

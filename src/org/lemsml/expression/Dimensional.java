package org.lemsml.expression;


public interface Dimensional {

	public int getM();
	public int getL();
	public int getT();
	public int getI();
	
	public int getK();
	
	public int getN();
	
	public Dimensional getTimes(Dimensional d);
	public Dimensional getDivideBy(Dimensional d);
	
	 
	
	public boolean matches(Dimensional dr);
	public boolean isDimensionless();
	public Dimensional power(double doubleValue);
	public boolean isAny();
	public double getDoubleValue();
}

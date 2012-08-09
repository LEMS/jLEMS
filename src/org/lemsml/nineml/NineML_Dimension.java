package org.lemsml.nineml;

import org.lemsml.io.IOFace;
import org.lemsml.type.Dimension;

public class NineML_Dimension implements IOFace {

	  public String name;
	  public int m;  // Mass
	  public int l;  // Length
	  public int t;  // Time
	  public int i;  // Current
	  public int k;  // Temperature
	  public int n;  // Amount of substance
	
	  
	  
	  // the CM dimension element is an exact equivalent to the internal LEMS one
	  public Object getInternal() {
		  Dimension d = new Dimension(name);
		  d.m = m;
		  d.l = l;
		  d.t = t;
		  d.i = i;
		  d.k = k;
		  d.n = n;
		  return d;
	  }
	  
}

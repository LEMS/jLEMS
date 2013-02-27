package org.lemsml.jlems.core.type.geometry;

import org.lemsml.jlems.core.type.LemsCollection;

public class Skeleton {


	public String name;
	
	public String solid;
	
	public LemsCollection<ScalarField> scalarFields = new LemsCollection<ScalarField>();
 
	
}

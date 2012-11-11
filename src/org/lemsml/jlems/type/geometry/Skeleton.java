package org.lemsml.jlems.type.geometry;

import org.lemsml.jlems.type.LemsCollection;

public class Skeleton {


	public String name;
	
	public String solid;
	
	public LemsCollection<ScalarField> scalarFields = new LemsCollection<ScalarField>();
	
	public Skeleton() {
		
	}
	
	
}

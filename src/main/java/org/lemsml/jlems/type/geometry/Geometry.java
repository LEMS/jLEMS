package org.lemsml.jlems.type.geometry;

import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.annotation.ModelElement;

@ModelElement(info="Specifies the geometrical interpretation of the properties of components realizing this ComponentType.")
public class Geometry {

	public String name;
 		
	public LemsCollection<Frustum> frustums = new LemsCollection<Frustum>();
	
	public LemsCollection<Solid> solids = new LemsCollection<Solid>();
 	
	public LemsCollection<Skeleton> skeletons = new LemsCollection<Skeleton>();
	 
	
	
}
package org.lemsml.jlems.type.dynamics;

import org.lemsml.jlems.annotation.ModelProperty;
import org.lemsml.jlems.expression.Valued;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Children;
import org.lemsml.jlems.type.ComponentReference;
import org.lemsml.jlems.type.ComponentType;



public class Edges {

	@ModelProperty(info="The element that provides the transitions for the scheme")
	public String children;

	@ModelProperty(info="The name of the attribute in the rate element that defines the source of the transition")
	public String sourceNodeName;
	
	@ModelProperty(info="")
	public String targetNodeName;
	
	@ModelProperty(info="")
	public String forwardRate;

	@ModelProperty(info="")
	public String reverseRate;
	
	
	ComponentReference r_srcRef;
	ComponentReference r_tgtRef;
	
	Valued frv;
	Valued rrv;
	
	
	public void resolve(Children r_edges) throws ContentError {
		ComponentType tedge = r_edges.getComponentType();
		
		r_srcRef = tedge.getComponentRef(sourceNodeName);
		r_tgtRef = tedge.getComponentRef(sourceNodeName);
		
		/*
	 	Dynamics b = tedge.getBehavior();
		frv = b.getValued(forwardRate);
		rrv = b.getValued(reverseRate);
		*/
	}
	
}

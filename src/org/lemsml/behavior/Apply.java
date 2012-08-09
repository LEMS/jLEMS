package org.lemsml.behavior;

import java.util.ArrayList;

import org.lemsml.expression.ParseError;
import org.lemsml.run.ApplyBuilder;
import org.lemsml.run.BuilderElement;
import org.lemsml.run.ComponentBehavior;
import org.lemsml.type.Children;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Lems;
import org.lemsml.util.ContentError;

public class Apply extends BuildElement {

	public String components;
 
	public Children r_children;
 	
	
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {
		ArrayList<ComponentBehavior> acb = new ArrayList<ComponentBehavior>();
		
		ArrayList<Component> cs = cpt.getChildrenAL(components);
		for (Component c : cs) {
			acb.add(c.getComponentBehavior());
		}
		
		ApplyBuilder ret = new ApplyBuilder(components, acb);
		super.makeChildBuilders(cpt, ret);
		return ret;
	}		

	
	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		r_children = ct.getChildrenByName(components);
		if (r_children == null) {
			throw new ContentError("Children " + components + " not found in " + ct);
		} 
		
		
	
	}
	
	
}

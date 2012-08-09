package org.lemsml.type;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.canonical.CanonicalElement;
import org.lemsml.expression.ParseError;
import org.lemsml.expression.Parser;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;


@Mel(info="A reference to another component. The target component can be accessed with path expressions in the " +
		"same way as a child component, but can be defined independently")
public class ComponentReference implements Named  {

	@Mat(info="")
	public String name;
	@Mat(info="Target type")
	public String type;
	
	public ComponentType r_type;
	
	public boolean isAny = false;
	
	public String compClass;
	
	
	public ComponentReference() {
		
	}
	
	
	public ComponentReference(String sn, String st, ComponentType t) {
		name = sn;
		type = st;
		r_type = t;
	}


   
	
	
	
	public void resolve(Lems lems, Parser p) throws ContentError, ParseError {
		LemsCollection<ComponentType> types = lems.getComponentTypes();
		
		if (type == null && compClass != null) {
			type = compClass;
		}
		
		if (type == null) {
			E.error("no type specified in component ref " + name);
		} else if (type.equals("Component")) {
			isAny = true;
			
		} else {
			ComponentType t = types.getByName(type);
		if (t != null) {
			r_type = t;
			r_type.checkResolve(lems, p);
			
		} else {
			throw new ContentError("ComponentRef: No such typer: " + type + ", used by " + getName());
		}
		}
		
	}


	public String getName() {
		return name;
	}


	public ComponentType getComponentType() {
		return r_type;
	}
	
	
	public String getTargetType() {
		return type;
	}
	
	


	public ComponentReference makeCopy() {
		 return new ComponentReference(name, type, r_type);
	}


	public boolean isLocal() {
		return false;
	}


	public CanonicalElement makeCanonical() {
		CanonicalElement ret = new CanonicalElement("ComponentRef");
		ret.add(new CanonicalElement("name", name));
		ret.add(new CanonicalElement("type", type));
		return ret; 
	}

}

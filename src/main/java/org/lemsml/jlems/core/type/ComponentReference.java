package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;


@ModelElement(info="A reference to another component. The target component can be accessed with path expressions in the " +
		"same way as a child component, but can be defined independently")
public class ComponentReference implements Named  {

	@ModelProperty(info="")
	public String name;

	@ModelProperty(info="Target type")
	public String type;
	public ComponentType r_type;
	
	public String root;
	
	public boolean isAny = false;

	
	public boolean local = false;
	
	public boolean required = true;
	
	public String defaultComponent;
	
	private boolean inResolve = false;
	
	public ComponentReference() {
		// maybe only one constructor?
	}
	
	
	public ComponentReference(String sn, String st, ComponentType t) {
		name = sn;
		type = st;
		r_type = t;
	}


	
	
	public void resolve(Lems lems, Parser p) throws ContentError, ParseError {
		inResolve = true;
		
		LemsCollection<ComponentType> types = lems.getComponentTypes();
		
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
		inResolve = false;
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
		ComponentReference ret = new ComponentReference();
		ret.name = name;
		ret.type = type;
		ret.local = local;
		ret.required = required;
		ret.r_type = r_type;
		if (defaultComponent != null) {
			ret.defaultComponent= defaultComponent;
	}
		return ret; 
 	}


	public boolean isLocal() {
		return local;
	}


	public boolean isRequired() {
		boolean ret = true;
		if (!required) {
			ret = false;
		}
		return ret;
	}

	public boolean resolving() {
		return inResolve;
	}


 

	public String getDefaultComponent() {
		return defaultComponent;
    }


 
}

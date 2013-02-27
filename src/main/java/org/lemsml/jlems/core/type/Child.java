package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info="Specifies that a component can have a child of a particular type. The name supplied here can be used in " +
		"path expressions to access the component. This is useful, for example, where a component can have multiple " +
		"children of the same type but with different roles, such as the forward and reverse transition rates in a channel.")
public class Child implements Named {

	@ModelProperty(info="")
    public String name;
    
	@ModelProperty(info="Reference to a component class, the value should be the name of the target class.")
	public String type;
    public ComponentType r_type;
	
    public String substitute;
    

    @Override
    public String toString() {
        return "Child {" + name + ", type=" + type + "}";
    }

   
    
    public void resolve(Lems lems, Parser p) throws ContentError, ParseError {
        LemsCollection<ComponentType> types = lems.getComponentTypes();
         
        
        ComponentType t = types.getByName(type);
        if (t != null) {
            r_type = t;
            r_type.checkResolve(lems, p);
  
        } else {
            throw new ContentError("No such type: " + type + " when resolving " + this);
        }


    }

    public String getName() {
        return name;
    }

    public ComponentType getComponentType() {
        return r_type;
    }

	public Child makeCopy() {
		 Child ret = new Child();
		 ret.name = name;
		 ret.type = type;
		 ret.r_type = r_type;
		 return ret;
	}

  
}

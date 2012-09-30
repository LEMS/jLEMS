package org.lemsml.jlems.type;

import org.lemsml.jlems.annotation.Mat;
import org.lemsml.jlems.annotation.Mel;
import org.lemsml.jlems.canonical.CanonicalElement;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.util.ContentError;

@Mel(info="Specifies that a component can have a child of a particular type. The name supplied here can be used in " +
		"path expressions to access the component. This is useful, for example, where a component can have multiple " +
		"children of the same type but with different roles, such as the forward and reverse transition rates in a channel.")
public class Child implements Named {

	@Mat(info="")
    public String name;
    
	@Mat(info="Reference to a component class, the value should be the name of the target class.")
	public String type;
    public ComponentType r_type;
	
   

    @Override
    public String toString() {
        return "Child {" + name + ", type=" + type + "}";
    }

    public Child() {
    }

    public Child(String name, ComponentType r_type) {
        this.name = name;
        this.r_type = r_type;
        this.type = r_type.getName();
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

    public CanonicalElement makeCanonical() {
        CanonicalElement ret = new CanonicalElement("Child");
        ret.add(new CanonicalElement("name", name));
        ret.add(new CanonicalElement("type", type));
        return ret;
    }
}

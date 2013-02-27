package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.sim.ContentError;
 

@ModelElement(info="Specifies that a component can have children of a particular class. The class may refer to an extended" +
		"type, in which case components of any class that extends the specified target class should be valid as " +
		"child components")
public class Children implements Named  {

	@ModelProperty(info="")
    public String name;
    
	@ModelProperty(info="The class of component allowed as children.")
	public String type;
    public ComponentType r_type;
      
  
// as yet, these are never used - no need?    
//    @Mat(info="Minimum number of children")
//    public int min = 0;
//    @Mat(info="Maximum number of children")
//    public int max = Integer.MAX_VALUE;

   
     
    @Override
    public String toString() {
        return "Children {" + name + ", type=" + type + "}";
    }

    public void resolve(Lems lems, Parser p) throws ContentError, ParseError {
        LemsCollection<ComponentType> classes = lems.getComponentTypes();
          
        ComponentType t = classes.getByName(type);
        if (t != null) {
            r_type = t;
            r_type.checkResolve(lems, p);
        } else {
            throw new ContentError("no such type: " + type);
        }
        if (name == null) {
            name = type.substring(0, 1).toLowerCase() + type.substring(1, type.length()) + "s";
        }
    }

    public String getName() {
        return name;
    }

    public ComponentType getComponentType() {
        return r_type;
    }

	public Children makeCopy() {
		Children ret = new Children();
		ret.name = name;
		ret.type = type;
		ret.r_type = r_type;
		return ret;
	}
 
}

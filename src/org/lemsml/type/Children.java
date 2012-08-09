package org.lemsml.type;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.canonical.CanonicalElement;
import org.lemsml.expression.ParseError;
import org.lemsml.expression.Parser;
import org.lemsml.util.ContentError;
 

@Mel(info="Specifies that a component can have children of a particular class. The class may refer to an extended" +
		"type, in which case components of any class that extends the specified target class should be valid as " +
		"child components")
public class Children implements Named  {

	@Mat(info="")
    public String name;
    
	@Mat(info="The class of component allowed as children.")
	public String type;
    public ComponentType r_type;
      
  
// as yet, these are never used - no need?    
//    @Mat(info="Minimum number of children")
//    public int min = 0;
//    @Mat(info="Maximum number of children")
//    public int max = Integer.MAX_VALUE;

    
    public Children() {
    }

    public Children(String name, ComponentType type) {
        this.name = name;
        this.r_type = type;
        this.type = type.getName();
    }

   

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

    public CanonicalElement makeCanonical() {
        CanonicalElement ret = new CanonicalElement("Children");
        ret.add(new CanonicalElement("name", name));
        ret.add(new CanonicalElement("type", type));
        return ret;
    }
}

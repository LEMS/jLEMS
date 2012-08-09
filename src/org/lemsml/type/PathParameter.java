package org.lemsml.type;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.canonical.CanonicalElement;
import org.lemsml.util.ContentError;

@Mel(info="A parameter of which the value is a path expression. When a ComponentType declares a PathParameter, " +
		"a corresponding Component definition should have an attibute with that name whose value is a path expression " +
		"that evaluates within the instance tree of the built model. This is used, for example, in the definition of a " +
		"group component class, where the coresponding component specifies a path over the instance tree which selectes" +
		"the items that should go in the group.")
public class PathParameter implements Named {
	
	@Mat(info="Name of the parameter")
	public String name;
	 
	 
	
	public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
	 
	}


	public String getName() {
		return name;
	}

 
	public CanonicalElement makeCanonical() {
		CanonicalElement ret = new CanonicalElement("PathParameter");
		ret.add(new CanonicalElement("name", name));
		return ret;
	}

 
}

package org.lemsml.type;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.canonical.CanonicalElement;
import org.lemsml.expression.ParseError;
import org.lemsml.expression.Parser;
import org.lemsml.util.ContentError;

@Mel(info="This is used in conjunction with PathParameter elements to specify the target class of selections " +
		"defined within components operating over the instance tree.")
public class ComponentTypeRef implements Named {

	@Mat(info="")
	public String name;
  	
	public ComponentTypeRef() {
		
	}
	
	
	public ComponentTypeRef(String sn) {
	 
	}
	
	
	public void resolve(Lems lems, Parser p) throws ContentError, ParseError {
	 
	}


	public String getName() {
		return name;
	}

 
	public ComponentTypeRef makeCopy() {
		 return new ComponentTypeRef(name);
	}

 

	public CanonicalElement makeCanonical() {
		CanonicalElement ret = new CanonicalElement("ComponentTypeRef");
		ret.add(new CanonicalElement("name", name));
		return ret; 
	}

}

package org.lemsml.jlems.type;

import org.lemsml.jlems.annotation.Mat;
import org.lemsml.jlems.annotation.Mel;
import org.lemsml.jlems.canonical.CanonicalElement;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.util.ContentError;

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

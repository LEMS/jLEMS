package org.lemsml.jlems.core.template;

import java.util.HashMap;

import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
 

public class FixedTemplateElement extends AbstractTemplateElement {

	
	public String val;
	
	
	public FixedTemplateElement(String sv) {
		super();
		val = sv;
	}
	
	public String toString() {
		return "Fixed: " + val;
	}
	
	 
	public String eval(HashMap<String, String> vmap) {
		return val;
	}

	@Override
	public String eval(StateRunnable so, HashMap<String, StateRunnable> context) {
		return val;
	}
 

}

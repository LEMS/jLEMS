package org.lemsml.jlems.template;

import java.util.HashMap;

import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.RuntimeError;
 

public abstract class TemplateElement {

	
	 
	public abstract String eval(HashMap<String, String> vmap) throws TemplateException;

	public abstract String eval(StateInstance so, HashMap<String, StateInstance> context) throws ContentError, RuntimeError;
	 

}

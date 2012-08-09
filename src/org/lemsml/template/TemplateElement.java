package org.lemsml.template;

import java.util.HashMap;

import org.lemsml.run.StateInstance;
import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;
 

public abstract class TemplateElement {

	
	 
	public abstract String eval(HashMap<String, String> vmap) throws TemplateException;

	public abstract String eval(StateInstance so, HashMap<String, StateInstance> context) throws ContentError, RuntimeError;
	 

}

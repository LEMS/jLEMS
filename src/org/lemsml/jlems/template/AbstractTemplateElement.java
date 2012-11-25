package org.lemsml.jlems.template;

import java.util.HashMap;

import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.sim.ContentError;
 

public abstract class AbstractTemplateElement {

	
	 
	public abstract String eval(HashMap<String, String> vmap) throws TemplateException;

	public abstract String eval(StateInstance so, HashMap<String, StateInstance> context) throws ContentError, RuntimeError;
	 

}

package org.lemsml.jlems.template;

import java.util.HashMap;

import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.run.StateRunnable;
import org.lemsml.jlems.sim.ContentError;
 

public abstract class AbstractTemplateElement {

	
	 
	public abstract String eval(HashMap<String, String> vmap) throws TemplateException;

	public abstract String eval(StateRunnable so, HashMap<String, StateRunnable> context) throws ContentError, RuntimeError;
	 

}

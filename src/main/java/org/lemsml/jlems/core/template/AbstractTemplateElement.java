package org.lemsml.jlems.core.template;

import java.util.HashMap;

import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
import org.lemsml.jlems.core.sim.ContentError;
 

public abstract class AbstractTemplateElement {

	
	 
	public abstract String eval(HashMap<String, String> vmap) throws TemplateException;

	public abstract String eval(StateRunnable so, HashMap<String, StateRunnable> context) throws ContentError, RuntimeError;
	 

}

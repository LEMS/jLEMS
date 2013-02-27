package org.lemsml.jlems.core.run;

import java.util.HashMap;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.template.StringTemplate;
import org.lemsml.jlems.core.type.Lems;

public class ExecutablePrint extends ExecutableStatement {

	String template;
	
	StringTemplate tpl;
	
	public ExecutablePrint(String tem, Lems lems) throws ContentError {
		super();
		template = tem;
		tpl = new StringTemplate(tem);
		tpl.parse(lems);
	}


	@Override
	public void execute(StateRunnable so, HashMap<String, StateRunnable> context)
			throws RuntimeError, ContentError, ConnectionError {
		
		String s = tpl.eval(so, context);
		E.info(s);
		
	}
	
	
}

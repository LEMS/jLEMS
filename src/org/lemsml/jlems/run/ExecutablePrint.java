package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.template.StringTemplate;
import org.lemsml.jlems.type.Lems;

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
	public void execute(StateInstance so, HashMap<String, StateInstance> context)
			throws RuntimeError, ContentError, ConnectionError {
		
		String s = tpl.eval(so, context);
		E.info(s);
		
	}
	
	
}

package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.template.StringTemplate;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.util.RuntimeError;

public class ExecutablePrint extends ExecutableStatement {

	String template;
	
	StringTemplate tpl;
	
	public ExecutablePrint(String tem, Lems lems) throws ContentError {
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

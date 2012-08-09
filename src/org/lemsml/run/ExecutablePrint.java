package org.lemsml.run;

import java.util.HashMap;

import org.lemsml.template.StringTemplate;
import org.lemsml.type.Lems;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.RuntimeError;

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

package org.lemsml.jlems.core.template;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Lems;
 

public class StringTemplate {

	String srcExpression;
	
	ArrayList<AbstractTemplateElement> elements = null;
	
	
	
	public StringTemplate(String se) {
		srcExpression = se;
	}

	
	public void print() {
		if (elements == null) {
			E.info("No template elements");
		} else {
			int idx = 0;
			for (AbstractTemplateElement te : elements) {
				
				E.info("elt: " + idx + " "  + te);
				idx += 1;
			}
		}
	}
	
	
	public void parse(Lems lems) throws ContentError {
		elements = new ArrayList<AbstractTemplateElement>();
		
		String rest = srcExpression;
		while (rest.indexOf("${") >= 0) {
			int ia = rest.indexOf("${");
			if (ia > 0) {
				elements.add(new FixedTemplateElement(rest.substring(0, ia)));
			}
			int ib = rest.indexOf("}", ia+1);
			if (ib > 0) {
				elements.add(makeVariableElement(rest.substring(ia + 2, ib), lems));
			} else {
				break;
			}
			rest = rest.substring(ib+1, rest.length());
		}
		if (rest.length() > 0) {
			elements.add(new FixedTemplateElement(rest));
		}
 		
	}

	
	
	private AbstractTemplateElement makeVariableElement(String str, Lems lems) throws ContentError {
		AbstractTemplateElement ret = new FixedTemplateElement("Error");
		
		int ia = str.indexOf("[");
		if (ia > 0) {
			String lnm = str.substring(0, ia);
			int ib = str.indexOf("]", ia);
			if (ib > ia) {
				String fv = str.substring(ia+1, ib);
				
				 
				if (fv.indexOf(".") > 0) {
					int idot = fv.indexOf(".");
					String b0 = fv.substring(0, idot);
					String b1 = fv.substring(idot + 1, fv.length());
					ret = new TupleSliceTemplateElement(lnm, b0, b1);
				}
			} else {
				E.missing();
			}
				
		} else {
 			ret = new FieldTemplateElement(str, lems);
		}
		return ret;
	}



	public String eval(HashMap<String, String> vmap) throws TemplateException {
		 
		String ret = "";
		for (AbstractTemplateElement te : elements) {
			ret += te.eval(vmap);
		}
		return ret;
	}


	public String eval(StateRunnable so, HashMap<String, StateRunnable> context) throws ContentError, RuntimeError {
		 
		String ret = "";
		for (AbstractTemplateElement te : elements) {
			ret += te.eval(so, context);
		}
		return ret;
	}
	
	 
 
 
	
}

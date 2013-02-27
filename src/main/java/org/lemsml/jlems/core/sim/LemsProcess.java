package org.lemsml.jlems.core.sim;
  
import java.util.ArrayList;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.reader.LemsFactory;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.EventManager;
import org.lemsml.jlems.core.run.ExecutableProcedure;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.RuntimeType;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.type.BuildException;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.Target;
import org.lemsml.jlems.core.type.procedure.Procedure;
import org.lemsml.jlems.core.xml.XMLElement;
import org.lemsml.jlems.core.xml.XMLElementReader;
import org.lemsml.jlems.core.xml.XMLException;
 

public class LemsProcess {

	protected Class<?> root;

	protected String srcfnm;
 
	protected String srcStr;

	protected Lems lems;

	 

	boolean allowConsolidation = true;

	  
    ArrayList<RuntimeType> substitutions = new ArrayList<RuntimeType>();
    

	public LemsProcess(String str) {
		srcStr = str;
 	}

	
	public void addSubstitutionType(RuntimeType rt) {
		substitutions.add(rt);
	}
	 

	public void setNoConsolidation() {
		allowConsolidation = false;
	}

	public void readModel() throws ContentError, ParseError, ParseException, BuildException,
			XMLException {
		String stxt = getSourceText();
	 	
		boolean loose = true;
 	
		// TODO tmp - make reader cope without extra spaces
		XMLElementReader exmlr = new XMLElementReader(stxt + "    ");

		XMLElement xel = exmlr.getRootElement();

		
		// E.info("read xml " + xel.toXMLString(""));

	
		LemsFactory lf = new LemsFactory();
		lems = lf.buildLemsFromXMLElement(xel);

		
		
		if (loose) {
			lems.setResolveModeLoose();
		}

		lems.deduplicate();
		lems.resolve();

		lems.evaluateStatic();		
	}

	public String getSourceText() throws ContentError {
		String stxt = "";
	
	 		stxt = srcStr.trim();

			if (stxt.startsWith("<?xml")) {
				int index = stxt.indexOf(">");
				stxt = stxt.substring(index + 1).trim();
			}
 
		return stxt;
	}

	public void print() {
		E.info("Model:\n" + lems.textSummary());
	}

	 

	public Lems getLems() {
		return lems;
	}

	
	public void applySubstitutions() throws ContentError {
		if (substitutions != null) {
			for (RuntimeType rt : substitutions) {
				String sid = rt.getID();
				Component cpt = lems.getComponent(sid);
				cpt.setRuntimeType(rt);
				E.info("Replaced runtime type in " + cpt);
			}
		}
	}
	
	
	public void process() throws ContentError, ConnectionError, ParseError, RuntimeError {
		for (Target dr : lems.getTargets()) {

			Component cpt = dr.getComponent();

			ComponentType ct = cpt.getComponentType();

			for (Procedure pr : ct.getProcedures()) {

				ExecutableProcedure ep = pr.makeExecutableProcedure(lems);

				// E.info("Running procedure: " + pr + " on " + cpt);

			 
				StateInstance so = lems.build(cpt.getStateType(), EventManager.getInstance());

				ep.execute(so);
			}

		}

	}

}

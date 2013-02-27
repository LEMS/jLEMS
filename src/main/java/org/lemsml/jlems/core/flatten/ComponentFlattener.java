package org.lemsml.jlems.core.flatten;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Child;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentBuilder;
import org.lemsml.jlems.core.type.ComponentReference;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.ComponentTypeBuilder;
import org.lemsml.jlems.core.type.EventPort;
import org.lemsml.jlems.core.type.Exposure;
import org.lemsml.jlems.core.type.FinalParam;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.ParamValue;
import org.lemsml.jlems.core.type.Requirement;
import org.lemsml.jlems.core.type.Text;
import org.lemsml.jlems.core.type.dynamics.DerivedVariable;
import org.lemsml.jlems.core.type.dynamics.Dynamics;
import org.lemsml.jlems.core.type.dynamics.OnCondition;
import org.lemsml.jlems.core.type.dynamics.OnEvent;
import org.lemsml.jlems.core.type.dynamics.OnStart;
import org.lemsml.jlems.core.type.dynamics.StateAssignment;
import org.lemsml.jlems.core.type.dynamics.StateVariable;
import org.lemsml.jlems.core.type.dynamics.TimeDerivative;
import org.lemsml.jlems.core.util.StringUtil;

public class ComponentFlattener {

	Lems lems;
	Parser parser;
	
	Component srcComponent;
	
	ComponentBuilder cbuilder;
	ComponentTypeBuilder typeB;

	public ComponentFlattener(Lems l, Component c) {
		lems = l;
		srcComponent = c;
	
	}
	
	
	public void checkBuilt() throws ContentError, ParseError, ConnectionError {
		parser = lems.getParser();
		if (cbuilder == null) {
			buildFlat();
		}
	}
	
	
	public Component getFlatComponent() throws ContentError, ParseError, ConnectionError {
		checkBuilt();
		return cbuilder.getTarget();
	}
	
	public ComponentType getFlatType() throws ContentError, ParseError, ConnectionError {
		checkBuilt();
		return typeB.getTarget();
	}
	
	

	public void buildFlat() throws ContentError, ParseError, ConnectionError {
		ComponentType srcCt = srcComponent.getComponentType();
	 
		typeB = new ComponentTypeBuilder();
		String typeName = srcCt.getName() + "_flat";
		typeB.setName(typeName);

		cbuilder = new ComponentBuilder();
		cbuilder.setID(srcComponent.getID() + "_flat");
		cbuilder.setType(typeName);
		
		importFlattened(srcComponent, "");
	}
	
 
	
	
	
	
	private void importFlattened(Component cpt, String prefix) throws ContentError, ParseError, ConnectionError {

		HashMap<String, String> varHM = new HashMap<String, String>();

		ComponentType typ = cpt.getComponentType();

	 	
		for (Text t : typ.getTexts()) {
			String newText = flatName(t.getName(), prefix);
			typeB.addText(newText);
		}

		for (FinalParam p : typ.getFinalParams()) {
	 		String fname = flatName(p.getName(), prefix, varHM);
			typeB.addParameter(fname, p.getDimension());
		}

		for (Exposure ex : typ.getExposures()) {
			String fname = flatName(ex.getName(), prefix);
			typeB.addExposure(fname, ex.getDimension());
		}

		for (Requirement req : typ.getRequirements()) {
			typeB.ensureHasRequirement(req.getName(), req.getDimension());
		}

		
		Dynamics dyn = typ.getDynamics();
		for (StateVariable sv : dyn.getStateVariables()) {
			String fname = flatName(sv.getName(), prefix, varHM);
			typeB.addStateVariable(fname, sv.getDimension());

			if (sv.getExposure() != null) {
				String enm = flatName(sv.getExposureName(), prefix);
				typeB.setStateExposure(fname, enm);
			}
		}

		for (OnEvent oe : dyn.getOnEvents()) {
			typeB.addOnEvent(oe.makeCopy());
		}

		for (EventPort ep : typ.getEventPorts()) {
			typeB.addEventPort(ep.makeCopy());
		}

		for (OnCondition oc : dyn.getOnConditions()) {
			typeB.addOnCondition(oc.makeCopy());
		}

		for (ParamValue pv : cpt.getParamValues()) {
	 		String fname = flatName(pv.getName(), prefix);
			// TODO
			String val = cpt.getAttributes().getByName(pv.getName()).getValue();
			cbuilder.addParameter(fname, val);
		}

		
		for (Component child : cpt.getAllChildren()) {
			String cid = "";
			if (child.getID() != null) {
				cid = child.getID();
			} else {
				cid = child.getName();
			}
			
			String childPrefix = flatName(cid, prefix);
			importFlattened(child, childPrefix);
		}

		
		for (DerivedVariable dv : dyn.getDerivedVariables()) {

			String fname = flatName(dv.getName(), prefix, varHM);
		 
			String val = dv.getValueExpression();
			String sel = dv.getSelect();

			
			if (val != null) {
				val = substituteVariables(val, varHM);
				typeB.addDerivedVariable(fname, dv.getDimension(), val);
				
			} else if (sel != null) {
				String red = dv.getReduce();
				String selval = sel;
				if (red != null) {
					String op = " ? ";
					String dflt = "";
					if (red.equals("add")) {
						op = " + ";
						dflt = "0";
					} else if (red.equals("multiply")) {
						op = " * ";
						dflt = "1";
					} else {
						throw new ContentError("Unrecognized reduce: " + red);
					}
				
					int iwc = sel.indexOf("[*]");
					String rt = sel.substring(0, iwc);
					String var = sel.substring(iwc + 4, sel.length());
					
					ArrayList<String> items = new ArrayList<String>();
					items.add(dflt);
					for (Component c : cpt.getChildrenAL(rt)) {
						items.add(flatName(c.getID() + "_" + var, prefix));
					}
					selval = StringUtil.join(items, op);
				}
				
				for (Child child : typ.getChilds()) {
					String sp = child.getName();
					String fp = flatName(sp, prefix);
					selval = selval.replace(sp + "/", fp + "_");
				}

				for (ComponentReference compRef : typ.getComponentReferences()) {
					String sp = compRef.getName();
					String refid = cpt.getRefComponents().get(compRef.getName()).getID();
					String fp = flatName(refid, prefix);
					selval = selval.replace(sp + "/", fp + "_");
				}
			 
				
				typeB.addDerivedVariable(fname, dv.getDimension(), selval);
			}
			 

			if (dv.exposure != null) {
				String enm = flatName(dv.exposure, prefix);
				typeB.setDerivedVariableExposure(fname, enm);
			}
		}

		for (TimeDerivative td : dyn.getTimeDerivatives()) {

			String val = substituteVariables(td.getValueExpression(), varHM);
			
			String varnm = flatName(td.getVariable(), prefix);
			typeB.addTimeDerivative(varnm, val);
		}
		

		for (OnStart os : dyn.getOnStarts()) {
			for (StateAssignment sa : os.stateAssignments) {

				String vnm = flatName(sa.getVariable(), prefix);
				String val = substituteVariables(sa.getValueExpression(), varHM);
				typeB.addOnStart(vnm, val);
			}
		}
	}
	
	

	private String flatName(String nm, String pfx, HashMap<String, String> varmap) {
		String ret = flatName(nm, pfx);
		if (nm.equals(ret)) {
			// no need to add it
		} else {
			varmap.put(nm, ret);
		}
		return ret;
	}

	private String flatName(String nm, String pfx) {
		String ret = nm;
		if (pfx.length() > 0) {
			ret = pfx + "_" + nm;
		}
 		return ret;
	}
	
	
	private String substituteVariables(String expr, HashMap<String, String> varHM) throws ParseError, ContentError {
		
		ParseTree ptree = parser.parse(expr);
		
		ptree.substituteVariables(varHM);
		
		String ret = ptree.toExpression();
		return ret;
	}

		 
	  

}

package org.lemsml.jlems.codger;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.run.ComponentBehavior;
import org.lemsml.jlems.run.FixedQuantity;
import org.lemsml.jlems.run.PathDerivedVariable;
import org.lemsml.jlems.run.VariableROC;

public class ComponentGenerator {

	
	ArrayList<ComponentBehavior> aCB = new ArrayList<ComponentBehavior>();
	
	ArrayList<MetaClass> aMC = new ArrayList<MetaClass>();
	
	
	public ComponentGenerator() {
		
	}
	
	
	
	public void addComponentBehavior(ComponentBehavior cb) {
		aCB.add(cb);
	}
	
	
	public void buildMetaCode() {
		HashMap<String, ComponentBehavior> cbHM = new HashMap<String, ComponentBehavior>();
		for (ComponentBehavior cb : aCB) {
			cbHM.put(cb.getComponentID(), cb);
		}
		
		
		for (ComponentBehavior cb : aCB) {
			MetaClass mc = makeMetaClass(cb);
			aMC.add(mc);
		}
		
		
	}
	
	 
	private MetaClass makeMetaClass(ComponentBehavior cb) {
		MetaClass ret = new MetaClass(cb.getComponentID());
			
		for (FixedQuantity fq : cb.getFixed()) {
			ret.addConstant(fq.getName(), fq.getValue());
		}
		
		for (String s : cb.getStateVariables()) {
			ret.addVariable(s);
		}
		
		
		MetaMethod mmex = ret.newMetaMethod(VarType.DOUBLE, "getExposed", "ret");
		mmex.addFloatArgument("str");
		mmex.addMapConditionalAssignment(VarType.DOUBLE, "ret", "str", cb.getExposureMap());
		
		
		MetaMethod mm = ret.newMetaMethod("eval");
		for (String s : cb.getIndeps()) {
			mm.addFloatArgument(s);
		}
		
		// sort path deriveds, add to eval method
		for (PathDerivedVariable pdv : cb.getPathderiveds()) {
			
		}
		// sort expression deriveds, add to eval method
		
		// add rates to eval
		
		
		
		
		
		MetaMethod mmfe = ret.newMetaMethod("forwardEuler");
		mmfe.addFloatArgument("dt");
		MethodCall mc = mmfe.newMethodCall("eval");
		
		for (VariableROC vroc : cb.getRates()) {
			String vnm = vroc.getVariable();
			mmfe.addIncrement(vnm, new Product(vnm, "dt"));
		}
	
		return ret;
	}



}

package org.lemsml.jlems.codger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.lemsml.jlems.run.ComponentBehavior;
import org.lemsml.jlems.run.FixedQuantity;
import org.lemsml.jlems.run.PathDerivedVariable;
import org.lemsml.jlems.run.VariableROC;

public class ComponentGenerator {

	
	ArrayList<ComponentBehavior> aCB = new ArrayList<ComponentBehavior>();
	
 	
	
	boolean mcUpToDate = false;
	
	
	HashMap<String, MetaClass> metaClassHM = new HashMap<String, MetaClass>();
 	
	
	public ComponentGenerator() {
		
	}
	
	
	
	public void addComponentBehavior(ComponentBehavior cb) {
		aCB.add(cb);
	}
	
	
	public String getJavaSource(String cbid) {
		if (!mcUpToDate) {
			buildMetaCode();
		}
		MetaClass mc = metaClassHM.get(cbid);
		return mc.generateJava();
	}
	
	
	
	public void buildMetaCode() {
		HashMap<String, ComponentBehavior> cbHM = new HashMap<String, ComponentBehavior>();
		for (ComponentBehavior cb : aCB) {
			cbHM.put(cb.getComponentID(), cb);
		}
		
		
		for (ComponentBehavior cb : aCB) {
			MetaClass mc = makeMetaClass(cb);
			metaClassHM.put(cb.getComponentID(), mc);
		}
		mcUpToDate = true;
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



	public String getCombinedJavaSource() {
		if (!mcUpToDate) {
			buildMetaCode();
		}
		
		ArrayList<String> aid = new ArrayList<String>();
		aid.addAll(metaClassHM.keySet());
		Collections.sort(aid);
		 
		
		StringBuilder sb = new StringBuilder();
		for (String s : aid) {
			MetaClass mc = metaClassHM.get(s);
			sb.append(mc.generateJava());
			sb.append("\n\n");
		}
		return sb.toString();
	}



}

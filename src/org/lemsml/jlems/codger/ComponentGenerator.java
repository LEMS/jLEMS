package org.lemsml.jlems.codger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.run.ComponentBehavior;
import org.lemsml.jlems.run.ExpressionDerivedVariable;
import org.lemsml.jlems.run.FixedQuantity;
import org.lemsml.jlems.run.MultiComponentBehavior;
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
		
		metaClassHM = new HashMap<String, MetaClass>();
		
		
		GenPackage gp = new GenPackage("org.lemsml.generated");
		
		for (ComponentBehavior cb : aCB) {
			String cbid = cb.getComponentID();
			recAdd(gp, cb, cbid);
		}
		mcUpToDate = true;
	}
		
		
	private void recAdd(GenPackage gp, ComponentBehavior cb, String cnm) {
		MetaClass mc = makeMetaClass(gp, cb, cnm);
		
	 	
		metaClassHM.put(gp.getFQClassName(cnm), mc);
		
		GenPackage cgp = new GenPackage(cnm, gp);
		
		HashMap<String, ComponentBehavior> chm = cb.getChildHM();
		for (String s : chm.keySet()) {
			ComponentBehavior ccb = chm.get(s);
			E.info("Adding child cb " + cnm + " child=" + getCNM(ccb, s));
			String chnm = getCNM(ccb, s);
			recAdd(cgp, ccb, chnm);
		}
		
		HashMap<String, MultiComponentBehavior> mchm = cb.getMultiHM();
		for (String s : mchm.keySet()) {
			// TODO create abstract type s that exposes properties we need
			ArrayList<ComponentBehavior> acb = mchm.get(s).getCBs();
		
			int ctr = 0;
			if (acb.size() > 0) {
				for (ComponentBehavior ccb : acb) {
					String acnm = getCNM(cb, s + ctr);
					ctr += 1;
					
					E.info("Adding array child cb " + cnm + " child=" + acnm);
					// TODO the ones we're adding here need to extend s
					recAdd(cgp, ccb, acnm);
				}	
			}
		}
		
	}
	
	
	
	 
	private MetaClass makeMetaClass(GenPackage gp, ComponentBehavior cb, String cnm) {
		
		
		MetaClass ret = new MetaClass(gp, cnm);
			
		for (FixedQuantity fq : cb.getFixed()) {
			ret.addConstant(fq.getName(), fq.getValue());
		}
		
		for (String s : cb.getStateVariables()) {
			ret.addVariable(s);
		}
		
		HashMap<String, ComponentBehavior> chm = cb.getChildHM();
		for (String s : chm.keySet()) {
			ret.addObjectField(cnm, s, chm.get(s).getComponentID());
		}
		
		HashMap<String, MultiComponentBehavior> mchm = cb.getMultiHM();
		for (String s : mchm.keySet()) {
			ArrayList<ComponentBehavior> acb = mchm.get(s).getCBs();
			if (acb.size() > 0) {
				ret.addObjectArrayField(cnm, arrayName(s), s);				
			}
		}
		
		
		
		
		
		MetaConstructor mc = ret.addMetaConstructor();
		for (String s : chm.keySet()) {
			String scnm = getCNM(chm.get(s), s);
			mc.addInstantiation(s, scnm);	
		}
		
		for (String s : mchm.keySet()) {
			ArrayList<ComponentBehavior> acb = mchm.get(s).getCBs();
			int ctr = 0;
			for (ComponentBehavior scb : acb) {
				String acnm = getCNM(scb, s + ctr);
				ctr += 1;
				mc.addObjectToArrayInstantiator(acnm, arrayName(s), scb.getComponentID());
			}
			
		}
		
	 	
		HashMap<String, String> ehm = cb.getExposureMap();
		for (String s : ehm.keySet()) {
			ret.addFloatGetter(s, ehm.get(s));
		}
	 	
		
		MetaMethod mm = ret.newMetaMethod("eval");
		for (String s : cb.getIndeps()) {
			mm.addFloatArgument(s);
		}
		
		// sort path deriveds, add to eval method
		for (PathDerivedVariable pdv : cb.getPathderiveds()) {
			// TODO path needs to be converted to calls to get methods
			mm.addFloatAssignment(pdv.getVarName(), pdv.getPath());
		}
	
		// TODO
//		cb.sortExpressions();
		for (ExpressionDerivedVariable edv : cb.getExderiveds()) {
			mm.addFloatAssignment(edv.getVarName(), edv.getExpressionString());
		}
		
		for (VariableROC vroc : cb.getRates()) {
			String vnm = vroc.getVarName();
			String rnm = makeRateVar(vnm);
		 
			mm.addFloatAssignment(rnm, vroc.getTextExpression());
		}
		
		
		
		
		
		MetaMethod mmfe = ret.newMetaMethod("forwardEuler");
		mmfe.addFloatArgument("dt");
		MethodCall evmc = mmfe.newMethodCall("eval");
		
		for (VariableROC vroc : cb.getRates()) {
			String vnm = vroc.getVariable();
			mmfe.addIncrement(vnm, new Product(makeRateVar(vnm), "dt"));
		}
	
		return ret;
	}



	private String getCNM(ComponentBehavior cb, String ka) {
		String ret = cb.getComponentID();
		if (ret == null) {
			ret = ka; 
		}
		if (ret == null || ret.length() == 0) {
			E.error("No name for cpt? + cb");
		}
		return ret;
	}
	
	
	private String makeRateVar(String vnm) {
		String ret = "d_" + vnm + "_dt";
		return ret;
	}

	private String arrayName(String vnm) {
		String ret = "arr_" + vnm;
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

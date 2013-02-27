package org.lemsml.jlems.core.codger;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.lemsml.jlems.core.codger.metaclass.CodeUnit;
import org.lemsml.jlems.core.codger.metaclass.Constructor;
import org.lemsml.jlems.core.codger.metaclass.MetaClass;
import org.lemsml.jlems.core.codger.metaclass.MetaInterface;
import org.lemsml.jlems.core.codger.metaclass.MetaPackage;
import org.lemsml.jlems.core.codger.metaclass.Method;
import org.lemsml.jlems.core.codger.metaclass.MethodCall;
import org.lemsml.jlems.core.codger.metaclass.Product;
import org.lemsml.jlems.core.codger.metaclass.VarType;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.ExpressionDerivedVariable;
import org.lemsml.jlems.core.run.FixedQuantity;
import org.lemsml.jlems.core.run.MultiStateType;
import org.lemsml.jlems.core.run.PathDerivedVariable;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.run.VariableROC;

public class StateTypeGenerator {

	
	String targetPackage;
	
	ArrayList<StateType> aCB = new ArrayList<StateType>();
	
 
	boolean mcUpToDate = false;
	
	
	ArrayList<CodeUnit> codeUnits = new ArrayList<CodeUnit>();
	
	HashMap<String, CodeUnit> metaItemHM = new HashMap<String, CodeUnit>();
  
	
	
	public StateTypeGenerator(String tp) {
		targetPackage = tp;
	}
	
	
	public void addStateType(StateType cb) {
		aCB.add(cb);
	}
	
	
	public String getJavaSource(String cbid) {
		if (!mcUpToDate) {
			buildMetaCode();
		}
		CodeUnit mc = metaItemHM.get(cbid);
		return mc.generateJava();
	}
	
	
	
	public MetaPackage getRootPackage() {
		return makeMetaPackage(targetPackage);
	}
	
	public MetaPackage makeMetaPackage(String spkg) {	
		String[] bits = spkg.split("\\.");
		MetaPackage ret = new MetaPackage(bits[0]);
		for (int i = 1; i < bits.length; i++) {
			ret = new MetaPackage(bits[i], ret);
		}
		return ret;
	}
	
	
	public void buildMetaCode() {
		HashMap<String, StateType> cbHM = new HashMap<String, StateType>();
		for (StateType cb : aCB) {
			cbHM.put(cb.getComponentID(), cb);
		}
		
		codeUnits = new ArrayList<CodeUnit>();
		metaItemHM = new HashMap<String, CodeUnit>();
		
		
		MetaPackage gp = getRootPackage();
		
		MetaInterface geninst = new MetaInterface(makeMetaPackage("org.lemsml.jlems.run"), "GeneratedInstance");
		
		for (StateType cb : aCB) {
			String cbid = cb.getComponentID();
			MetaClass mc = recAdd(gp, cb, cbid, null);
			mc.addImplements(geninst);
			addAdvanceMethod(mc, cb);
			addExposedGetter(mc, cb);
		}
		mcUpToDate = true;
	}
		
	
	
	
	private void addAdvanceMethod(MetaClass mc, StateType cb) {
		Method m = mc.newMetaMethod("advance");
		m.addFloatArgument("t");
		m.addFloatArgument("dt");
		m.addMapArgument("vars", VarType.STRING, VarType.DOUBLEPOINTER);
		mc.addDependency("MAP");
		mc.addDependency("DOUBLEPOINTER");
		
	 
		for (String s : cb.getRequirements()) {
 			m.addMapDoubleExtraction(s, "vars", s);
		}
		
		m.addCall(mc.getMethod("forwardEuler"));
	}
	
		
	
	
	private void addExposedGetter(MetaClass mc, StateType cb) {
		Method m = mc.newMetaMethod("getVariable");
		m.setReturnType(VarType.DOUBLE);
		m.setReturnName("ret");
		
		m.addStringArgument("varname");
		
		for (String s : cb.getExposureMap().keySet()) {
			String sv = cb.getExposureMap().get(s);
			m.addStringConditionalSetter("varname", s, "ret", sv);
		}
	
	}
	
	
	
	
	
	private MetaClass recAdd(MetaPackage gp, StateType cb, String cnm, MetaInterface mi) {
		
		String partsPkg = cnm + "_parts";
		 
		MetaPackage cgp = new MetaPackage(partsPkg, gp);
		
		HashMap<String, MetaClass> childMCHM = new HashMap<String, MetaClass>();
		
		HashMap<String, StateType> chm = cb.getChildHM();
		for (String s : chm.keySet()) {
			StateType ccb = chm.get(s);
			String chnm = getCNM(ccb, s);
 			MetaClass mcc = recAdd(cgp, ccb, chnm, null);
			childMCHM.put(chnm, mcc);
		}
		
		
		HashMap<String, MetaInterface> childMIHM = new HashMap<String, MetaInterface>();
		
		HashMap<String, MultiStateType> mchm = cb.getMultiHM();
		for (String s : mchm.keySet()) {
			// TODO create abstract type s that exposes properties we need
			ArrayList<StateType> acb = mchm.get(s).getCBs();
		
			int ctr = 0;
			if (!acb.isEmpty()) {
				
				
				MetaPackage gpi = new MetaPackage(partsPkg, gp);
				String anm = arrayName(s);
				String atn = arrayTypeName(s);
				MetaInterface lmi = new MetaInterface(gpi, atn);
				codeUnits.add(lmi);
				metaItemHM.put(atn,  lmi);
				
				childMIHM.put(anm, lmi);
				
				for (StateType ccb : acb) {
					String acnm = getCNM(ccb, s + ctr);
					ctr += 1;
					recAdd(cgp, ccb, acnm, lmi);
				}	
			
				
				lmi.pullUp();
			}
		}
		
		MetaClass mc = makeMetaClass(gp, cb, cnm, partsPkg, mi, childMCHM, childMIHM);
		codeUnits.add(mc);
		metaItemHM.put(gp.getFQClassName(cnm), mc);
		
		return mc;
	}
	
	
	
	 
	private MetaClass makeMetaClass(MetaPackage gp, StateType cb, String cnm, String partsPkg, 
			MetaInterface mi, HashMap<String, MetaClass> childMCHM, HashMap<String, MetaInterface> childMIHM) {
		
		
		MetaClass ret = new MetaClass(gp, cnm);
		
		if (mi != null) {
			ret.addImplements(mi);
			mi.addImplementer(ret);
		}
		
		/*
		for (FixedQuantity fq : cb.getFixed()) {
			ret.addConstant(fq.getName(), fq.getValue());
		}
		*/
		
		for (String s : cb.getStateVariables()) {
			ret.addVariable(s);
		}
		
		for (PathDerivedVariable pdv : cb.getPathderiveds()) {
			ret.addVariable(pdv.getVariableName());
		}
		
		for (ExpressionDerivedVariable edv : cb.getExderiveds()) {
			ret.addVariable(edv.getVariableName());
		}
		
		
		for (VariableROC vroc : cb.getRates()) {
			String rnm = makeRateVar(vroc.getVariableName());
			ret.addVariable(rnm);
		}
		
		
		HashMap<String, StateType> chm = cb.getChildHM();
		for (String s : chm.keySet()) {
			
			String scnm = getCNM(chm.get(s), s);
			String oname = getChildObjectName(s);
			ret.addObjectField(partsPkg, oname, scnm);
		}
		
		HashMap<String, MultiStateType> mchm = cb.getMultiHM();
		for (String s : mchm.keySet()) {
			ArrayList<StateType> acb = mchm.get(s).getCBs();
			if (!acb.isEmpty()) {
				String atn = arrayTypeName(s);
				ret.addDependency("LIST");
				ret.addObjectArrayField(partsPkg, arrayName(s), atn);				
			}
		}
		
		
		Constructor mc = ret.addMetaConstructor();
		for (String s : chm.keySet()) {
			String scnm = getCNM(chm.get(s), s);
			String oname = getChildObjectName(s);
			mc.addInstantiation(oname, scnm);	
		}
		
		for (String s : mchm.keySet()) {
			ArrayList<StateType> acb = mchm.get(s).getCBs();
			int ctr = 0;
			for (StateType scb : acb) {
				String acnm = getCNM(scb, s + ctr);
				ctr += 1;
				mc.addObjectToArrayInstantiator(acnm, arrayName(s), scb.getComponentID());
			}
			
		}
		
	 	
		HashMap<String, String> ehm = cb.getExposureMap();
		for (String s : ehm.keySet()) {
			ret.addFloatGetter(s, ehm.get(s));
		}
	 	
		
		Method mm = ret.newMetaMethod("eval");
		for (String s : getAllIndeps(cb)) {
			mm.addFloatArgument(s);
		}
		
		// sort path deriveds, add to eval method
		for (PathDerivedVariable pdv : cb.getPathderiveds()) {
			String select=  pdv.getPath();
			if (select.indexOf("[") >= 0) {
				
				String vnm = pdv.getVariableName();
				String mmname = "listeval_" + vnm;
				mm.addFloatMethodAssignment(pdv.getVariableName(), mmname);
				
				
				Method mev = ret.newMetaMethod(VarType.DOUBLE, mmname, "ret");
				populateListReducer(mev, pdv);
				
				
			} else {
				String getex = buildSimpleSelect(select);
				mm.addFloatAssignment(pdv.getVariableName(), getex);
			}
		}
	
		// TODO
//		cb.sortExpressions();
		for (ExpressionDerivedVariable edv : cb.getExderiveds()) {
			mm.addFloatAssignment(edv.getVariableName(), edv.getExpressionString());
		}
		
		for (VariableROC vroc : cb.getRates()) {
			String vnm = vroc.getVariableName();
			String rnm = makeRateVar(vnm);
		 
			mm.addFloatAssignment(rnm, vroc.getTextExpression());
		}
		
		
		 
		Method mmfe = ret.newMetaMethod("forwardEuler");
		mmfe.addFloatArgument("dt");
	 
		
		
		
		HashMap<String, StateType> achm = cb.getChildHM();
		for (String s : achm.keySet()) {
			String scnm = getCNM(chm.get(s), s);
			String oname = getChildObjectName(s);
			
			MetaClass rmc = childMCHM.get(scnm);
			
			
			mmfe.addRefCall(oname, rmc, "forwardEuler");
			//  need to get a ref to the forwardEuler method call in corresponding class
			// add here as call
			
		}
		
		HashMap<String, MultiStateType> amchm = cb.getMultiHM();
		for (String s : amchm.keySet()) {
			ArrayList<StateType> acb = amchm.get(s).getCBs();
			if (!acb.isEmpty()) {
				String atn = arrayTypeName(s);
//				ret.addDependency("LIST");
//				ret.addObjectArrayField(partsPkg, arrayName(s), atn);				
			
				String anm = arrayName(s);
				
				MetaInterface cmi = childMIHM.get(anm);
				mmfe.addMultiRefCall(anm, cmi, "forwardEuler");
		 
			}
		}
		
		
		
		MethodCall evmc = mmfe.newMethodCall("eval");
	 	
		for (String s : getAllIndeps(cb)) {
			mmfe.addFloatArgument(s);
			evmc.addFloatArgument(s);
		}
		
		
		 
		
		
		
		for (VariableROC vroc : cb.getRates()) {
			String vnm = vroc.getVariable();
			mmfe.addIncrement(vnm, new Product(makeRateVar(vnm), "dt"));
		}
	
		return ret;
	}



	private void populateListReducer(Method mev, PathDerivedVariable pdv) {
		// TODO 
		// get select from pdv, check if adds or multiplies
		// parse select, find list, iterate over list adding function calls to rest of select.
		
		String select = pdv.getPath();
		String[] bits = select.split("/");
		if (bits[0].endsWith("[*]")) {
			String anm = bits[0].substring(0, bits[0].indexOf("["));
			
			String wk = "";	
			// TODO this is java specific...
			for (int i = 1; i < bits.length; i++) {
				wk += ".get_" + bits[i] + "()";
			}
	 		if (pdv.isProduct()) {
	 			mev.addArrayProduct("ret", anm, wk);
	 		} else if (pdv.isSum()) {
	 			mev.addArraySum("ret", anm, wk);
	 			
	 		} else {
	 			E.missing();
	 		}
			
		} else {
			E.error("can't handle path: " + select);
		}
	}

	
	

	private ArrayList<String> getAllIndeps(StateType cb) {
		ArrayList<String> ret = new ArrayList<String>();
		ret.addAll(cb.getAllIndeps());
		Collections.sort(ret);
		return ret;
	}


	private String getCNM(StateType cb, String ka) {
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
		
		StringBuilder sb = new StringBuilder();
		for (CodeUnit mi : codeUnits) {
			sb.append(mi.generateJava());
			sb.append("\n\n");
		}
	 
		return sb.toString();
	}




	private String getChildObjectName(String s) {
		return "o_" + s;
	}

	
	 
	
	private String buildSimpleSelect(String select) {
		String ret = "";
		String[] bits = select.split("/");
		for (String s : bits) {
 			if (ret.length() == 0) {
				ret += getChildObjectName(s);
			} else {
				ret += ".get_" + s + "()";
			}
 			
		}
		return ret;
	}
	
	
	private String arrayTypeName(String s) {
		return s + "_base";
	}


	public ArrayList<CodeUnit> getCodeUnits() {
		
		if (!mcUpToDate) {
			buildMetaCode();
		}
		
		return codeUnits;
	}
	
	
	
	
	
}

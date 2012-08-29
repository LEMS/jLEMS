package org.lemsml.display;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.run.ComponentBehavior;
import org.lemsml.run.ExpressionDerivedVariable;
import org.lemsml.run.MultiComponentBehavior;
import org.lemsml.run.PathDerivedVariable;
import org.lemsml.run.VariableROC;
import org.lemsml.util.E;

public class ComponentBehaviorWriter {

	
	public ComponentBehaviorWriter() {
		
	}
	
	
	
	public void print(ComponentBehavior cb) {
		StringBuilder sb = new StringBuilder();
		
		appendTo(sb, "", cb, "");
	
		E.info(sb.toString());
	}
	
	
	private void appendTo(StringBuilder sb, String pfx, ComponentBehavior cb, String ka) {
		sb.append(pfx + (ka.length() > 0 ? ka + ": " : "") + "ComponentBehavior " + cb.getComponentID() + "\n");
		
		pfx += "   ";
		
		sb.append(pfx);
		if (cb.getIndeps().size() > 0) {
			sb.append("Independents");
			sb.append(makeList(cb.getIndeps()) + "\n");
		}
		
		if (cb.getVars().size() > 0) {
			sb.append("Variables");
			sb.append(makeList(cb.getVars()) + "\n");
		}
		
		if (cb.getSvars().size() > 0) {
			sb.append("State Variables");
			sb.append(makeList(cb.getSvars()) + "\n");
		}
		
		if (cb.getRates().size() > 0) {
			sb.append("Rates\n");
			sb.append(makeRatesList(cb.getRates()) + "\n");
		}
		
		if (cb.getExderiveds().size() > 0) {
			sb.append("Expression derived\n");
			sb.append(makeEDVList(cb.getExderiveds()) + "\n");
		}
		if (cb.getPathderiveds().size() > 0) {
			sb.append("Path derived\n");
			sb.append(makePDVList(cb.getPathderiveds()) + "\n");
		}
		
		if (cb.getExposureMap().size() > 0) {
			sb.append(makeExposuresList(cb.getExposureMap()) + " ");
		}
		
		sb.append("\n");
		
		HashMap<String, ComponentBehavior> chm = cb.getChildHM();
		if (chm.size() > 0) {
		sb.append(pfx + "Children: " + chm.size() + "\n");
		for (String s : chm.keySet()) {
			appendTo(sb, pfx, chm.get(s), s);
		}
		}
		
		HashMap<String, MultiComponentBehavior> mcbhm = cb.getMultiHM();
		if (mcbhm.size() > 0) {
		sb.append(pfx + "MultiChildren: " + mcbhm.size() + "\n");
		for (String s : mcbhm.keySet()) {
			MultiComponentBehavior mcb = mcbhm.get(s);
			ArrayList<ComponentBehavior> acb = mcb.getCBs();
			for (int i = 0; i < acb.size(); i++) {
				appendTo(sb, pfx, acb.get(i), s + "[" + i + "]");				
			}
		}
		}
		
		HashMap<String, ComponentBehavior> rhm = cb.getRefHM();
		if (rhm.size() > 0) {
		sb.append(pfx + "Refs: " + rhm.size() + "\n");
		for (String s : rhm.keySet()) {
			appendTo(sb, pfx, rhm.get(s), s);
		}
		}
		
	}



	private String makeExposuresList(HashMap<String, String> exposureMap) {
		String ret = "Exposes ";
		boolean first = true;
		
		for (String s : exposureMap.keySet()) {
			if (!first) {
				ret += ", ";
			}
			ret += s + "->" + exposureMap.get(s);
			first = false;
		}
		return ret;
	}



	private String makeRatesList(ArrayList<VariableROC> rates) {
		String ret = "";
		for (VariableROC vr : rates) {
			ret += "            " + vr.getVarName() + " = " + vr.getTextExpression() + "\n";
		}
		return ret;
	}

	private String makeEDVList(ArrayList<ExpressionDerivedVariable> arrayList) {
		String ret =  "";
		for (ExpressionDerivedVariable edv : arrayList) {
			ret += ("    " + edv.getVarName() + " = " + edv.getExpressionString() + "\n");
		}
		return ret;
		
		/*
		ArrayList<String> wk = new ArrayList<String>();
		for (ExpressionDerivedVariable vr : arrayList) {
			wk.add(vr.getVarName());
		}
		String ret = makeList(lnm, wk);
		return ret;
		*/
	}

	private String makePDVList(ArrayList<PathDerivedVariable> arrayList) {
		String ret = " ";
	 	for (PathDerivedVariable vr : arrayList) {
			ret += "    " + vr.getVarName() + " = " + vr.getPath() + "\n";
		}
		return ret;
	}
	
	
	private String makeList(ArrayList<String> als) {
	 		boolean wrapped = false;
			int linelength = 0;
			String ret = "";
			boolean first = true;
			for (String s : als) {
				if (!first) {
					ret += ", ";
				}
				first = false;
				if (linelength > 80) {
					ret += "\n                 ";
					linelength = 10;
					wrapped = true;
				}
				ret += s;
				linelength += s.length() + 2;
			}
			ret += ";";
			if (wrapped) {
				ret += "\n";
			}
			return ret;
 	}

 
	private String join(ArrayList<String> als, String sep) {
		String ret = "";
		boolean first = true;
		for (String s : als) {
			if (!first) {
				ret += sep;
			}
			ret += s;
			first = false;
		}
		return ret;
	}
	
	
	
}

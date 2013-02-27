package org.lemsml.jlems.core.sim;

import java.util.HashSet;

import org.lemsml.jlems.core.logging.E;

public abstract class AbstractInclusionReader {

	HashSet<String> included = new HashSet<String>();
 	
	public abstract String getRootContent() throws ContentError;
	
	public abstract String getRelativeContent(String s) throws ContentError;
	
	
	
	public String read() throws ContentError {
		String sroot = getRootContent();
		String ret = insertIncludes(sroot);
		return ret;
	}
	
	public String cleanAttributeString(String sattsa) throws ContentError {
		String ret = "";
		String satts = sattsa;
		
		String sxmlns = "xmlns=\"";
        if (satts.startsWith(sxmlns)){
            satts = satts.substring(satts.indexOf(" ")+1);
        }


		String scomp = satts.replace(" ", "");
		String sfind = "file=\"";
		if (scomp.startsWith(sfind)) {
			ret = scomp.substring(sfind.length(), scomp.length()-1);
		
		} else {
			throw new ContentError("can't parse include directive: " + satts);
		}
		return ret;
	}
	
	
	protected String getIncludeContent(String srel) throws ContentError {
			String ret = "";
			if (included.contains(srel)) {
				// already included - nothing to do;
                //E.info(srel+ " IS already included: "+included);
			
			} else {
                //E.info(srel+ " NOT already included: "+included);
				included.add(srel);
				String sr = getRelativeContent(srel);
				ret = insertIncludes(sr);
				ret = trimOuterElement(ret);
 			}
			return ret;
	}
	
		
	protected String insertIncludes(String stxta) throws ContentError {
		String stxt = removeXMLComments(stxta);
		StringBuilder sfullSB = new StringBuilder();
		String sinc = "<Include ";
		while (true) {	
			int iinc = stxt.indexOf(sinc);
			if (iinc < 0) {
				break;
			} else {
				sfullSB.append(stxt.substring(0, iinc));
				int icb = stxt.indexOf("/>", iinc);
				String srel = cleanAttributeString(stxt.substring(iinc + sinc.length(), icb));
                                //E.info("Including: "+srel);
				sfullSB.append(getIncludeContent(srel));
				stxt = stxt.substring(icb + 2, stxt.length());
			}
		
		}
		sfullSB.append(stxt);
		return sfullSB.toString();
	}
	
	

	public String trimOuterElement(String s) {
		String ret = "";
		String swk = removeXMLComments(s);
		swk = swk.trim();

		if (swk.startsWith("<?xml")) {
			int index = swk.indexOf(">");
			swk = swk.substring(index+1).trim();
		}

		if (swk.startsWith("<")) {
			int isp = swk.indexOf(" ");
			int ict = swk.indexOf(">");
			String eltname = swk.substring(1, (ict < isp ? ict : isp));
			int sco = swk.indexOf(">");
			
			String ctag = "</" + eltname + ">";
 			int ice = swk.lastIndexOf(ctag);
			
			if (ice > sco) {
				ret = swk.substring(sco + 1, ice);
 				
			} else {
				int l = swk.length();
				E.error("non matching XML close in include: open tag=" + eltname + " end= ..." + swk.substring(l-15, l));
			}
		} else {
			int l = swk.length();
			E.error("Cant extract content from " + swk.substring(0, (20 < l ? 20 : l)));
		}
	
		return ret;
	}
	
	
	
	
	
	// TODO: put elsewhere? 
	// TODO accumulate fragments in buffer rather than processing whole string each time
	public static String removeXMLComments(String xml) {
		String ret = xml;
		while(ret.indexOf("<!--") >= 0){
			int start = ret.indexOf("<!--");
			int end = ret.indexOf("-->") + 3;
			ret = ret.substring(0,start) + ret.substring(end);
		}
		return ret;
     }
	
	
  
	
}

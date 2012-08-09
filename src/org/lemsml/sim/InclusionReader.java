package org.lemsml.sim;

import java.util.HashSet;

import org.lemsml.util.ContentError;
import org.lemsml.util.E;

public abstract class InclusionReader {

	HashSet<String> included = new HashSet<String>();
	HashSet<String> includedTexts = new HashSet<String>();
	
	public abstract String getRootContent() throws ContentError;
	public abstract String getRelativeContent(String s) throws ContentError;
	
	
	public String read() throws ContentError {
		String sroot = getRootContent();
		String ret = insertIncludes(sroot);
		return ret;
	}
	
	public String cleanAttributeString(String satts) throws ContentError {
		String ret = "";

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
			if (included.contains(srel) || included.contains("NeuroML2CoreTypes/"+srel)) {
				// already included - nothing to do;
                //E.info(srel+ " IS already included: "+included);
			} else {
                //E.info(srel+ " NOT already included: "+included);
				included.add(srel);
				String sr = getRelativeContent(srel);
				ret = insertIncludes(sr);
				ret = trimOuterElement(ret);

                if (includedTexts.contains(ret))
                    ret = "";
                else
                    includedTexts.add(ret);
			}
			return ret;
	}
	
		
	protected String insertIncludes(String stxt) throws ContentError {

                //E.info("<<<<--------------------------------");
                //E.info(stxt.substring(0,100));
                //E.info("-------------------------------->>>>");

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
		String swk = removeXmlComments(s);
		swk = swk.trim();

                if (swk.startsWith("<?xml")) {
                        int index = swk.indexOf(">");
                        swk = swk.substring(index+1).trim();
                }

                if (swk.startsWith("<neuroml")) {
                        int index = swk.indexOf(">");
                        swk = swk.substring(index+1);
                        // Assume </neuroml> at end...
			swk = swk.replace("</neuroml>", "");
                        ret = swk;
                } else {

                        if (swk.startsWith("<Lems")) {
                                int index = swk.indexOf(">");
                                swk = swk.substring(index+1);
                        }


                        if (swk.endsWith("</Lems>")) {
                                swk = swk.replace("</Lems>", "");
                                ret = swk;
                        } else {
                                E.error("expecting 'Lems' element in included file but not found:  " + swk);
                        }
                }
		return ret;
	}
	
        //TODO: put elsewhere?
        public static String removeXmlComments(String xml)
        {
             while(xml.indexOf("<!--")>=0){
                     int start = xml.indexOf("<!--");
                     int end = xml.indexOf("-->")+3;
                     xml = xml.substring(0,start)+xml.substring(end);
                             
             }
             return xml;
        }
	
    public static void main(String[] args)
    {
            String x = "this is <!-- --> a com<!-- -->ment";
            System.out.println(x+"\n"+removeXmlComments(x));
    }
	
	
}

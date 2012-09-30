package org.lemsml.jlemsio;

import org.lemsml.jlems.sim.InclusionReader;
import org.lemsml.jlems.util.ContentError;

public class PathInclusionReader extends InclusionReader {
 
	
	Class<?> root;
	String rootFnm;
	
	
	
	public PathInclusionReader(Class<?> cr, String crf) {
		root = cr;
		rootFnm = crf;
	}


	public String getRelativeContent(String s) throws ContentError {
		return JUtil.getRelativeResource(root, s);
	}

 
	public String getRootContent() throws ContentError {
		return JUtil.getRelativeResource(root, rootFnm);
	}
	

}

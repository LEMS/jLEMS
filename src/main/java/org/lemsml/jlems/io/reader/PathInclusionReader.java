package org.lemsml.jlems.io.reader;

import org.lemsml.jlems.io.util.JUtil;
import org.lemsml.jlems.sim.AbstractInclusionReader;
import org.lemsml.jlems.sim.ContentError;

public class PathInclusionReader extends AbstractInclusionReader {
 
	
	Class<?> root;
	String rootFnm;
	
	
	
	public PathInclusionReader(Class<?> cr, String crf) {
		super();
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

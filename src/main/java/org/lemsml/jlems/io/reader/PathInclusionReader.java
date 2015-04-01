package org.lemsml.jlems.io.reader;

import org.lemsml.jlems.core.sim.AbstractInclusionReader;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.io.util.JUtil;

public class PathInclusionReader extends AbstractInclusionReader {
 
	
	Class<?> root;
	String rootFnm;
	
	
	
	public PathInclusionReader(Class<?> cr, String crf) {
		super();
		root = cr;
		rootFnm = crf;
	}


	public String getRelativeContent(String attribute, String s) throws ContentError {
    	if(attribute.equals(URL))
    	{
    		throw new IllegalArgumentException("URL is not supported when using the PathInclusionReader!");
    	}
		return JUtil.getRelativeResource(root, s);
	}

 
	public String getRootContent() throws ContentError {
		return JUtil.getRelativeResource(root, rootFnm);
	}
	

}

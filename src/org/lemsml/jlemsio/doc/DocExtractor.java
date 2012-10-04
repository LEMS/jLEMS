package org.lemsml.jlemsio.doc;

import java.awt.Component;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.lemsml.jlems.annotation.ExplicitChildContainer;
import org.lemsml.jlems.annotation.Mat;
import org.lemsml.jlems.annotation.Mel;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.xml.XMLElement;
import org.lemsml.jlemsio.FileUtil;



public class DocExtractor {

	LinkedList<Class<? extends Object>> classLL = new LinkedList<Class<? extends Object>>();
	HashSet<Class<? extends Object>> classHS = new HashSet<Class<? extends Object>>();
	HashMap<Class<? extends Object>, DocItem> itemHM = new HashMap<Class<? extends Object>, DocItem>();
	
	ArrayList<DocItem> items = new ArrayList<DocItem>();
	
	public static void main(String[] argv) {
		DocExtractor de = new DocExtractor();
		de.extract(argv);
	}
	
	 
	
	
	public void extract(String[] argv) {
		recAdd(ComponentType.class, null);
//		addClass(Dynamics.class, null);
		recAdd(Component.class, null);
	 
		
		XMLElement root = new XMLElement("ElementTypes");
		for (DocItem di : items) {
			root.add(di.makeXMLElement());
		}
		
		String stxt = root.toXMLString("");
		// E.info("stxt is " + stxt);
		if (argv.length > 0) {
			String sf = argv[0];
			File f = new File(sf);
			try {
				FileUtil.writeStringToFile(stxt, f);
				E.info("Written file " + f.getAbsolutePath());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
			
	}
	
	
	private void recAdd(Class<? extends Object> cl, Class<? extends Object> pclass) {
		if (!classHS.contains(cl)) {
			classLL.add(cl);
			classHS.add(cl);
			DocItem di = new DocItem(cl);
			items.add(di);
			itemHM.put(cl, di);
			
			Annotation[] caa = (Annotation[]) cl.getAnnotations();
			if (caa != null) {
				for (Annotation a : caa) {
					if (a instanceof Mel) {
						Mel mel = (Mel)a;
						di.setInfo(mel.info());
					}
				}
			}
			
			
			
			
			for (Field fld : cl.getFields()) {
				if (Modifier.isPublic(fld.getModifiers())) {
				
					String fnm = fld.getName();
				 
					
					if (fnm.startsWith("r_") || fnm.startsWith("p_")) {
						// naming convention for semi private fields
					} else {
						if (fld.getType().equals(LemsCollection.class)) {
							ParameterizedType t = (ParameterizedType)fld.getGenericType();
 
							Class<?> c =  (Class<?>)(t.getActualTypeArguments()[0]);
							recAdd(c, cl);
						}
						
						Annotation[] aa = fld.getAnnotations();
						if (aa != null) {
							for (Annotation a : aa) {
								if (a instanceof Mat) {
									Mat mat = (Mat)a;
									di.addAttribute(fld.getName(), fld.getType(), mat.info());
								}
							}
						}
					}
							
				}
				
			}
			
			for (Class<?> cfi : cl.getInterfaces()) {
				if (cfi.equals(ExplicitChildContainer.class)) {
					try {
					ExplicitChildContainer obj = (ExplicitChildContainer)cl.newInstance();
					 
					for (Class<?> cc : obj.getChildClasses()) {
						recAdd(cc, cl);
					}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			
			
		}
		if (pclass != null) {
			// itemHM.get(pclass).addC
			itemHM.get(cl).addContainer(pclass);
			itemHM.get(pclass).addContent(cl);
		}
	
	}
	
}

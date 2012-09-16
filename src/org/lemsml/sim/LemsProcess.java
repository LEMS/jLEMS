package org.lemsml.sim;

import java.io.File;

import org.lemsml.canonical.CanonicalWriter;
import org.lemsml.expression.ParseError;
import org.lemsml.io.LemsMap;
import org.lemsml.io.NameMapper;
import org.lemsml.procedure.Procedure;
import org.lemsml.run.ConnectionError;
import org.lemsml.run.EventManager;
import org.lemsml.run.ExecutableProcedure;
import org.lemsml.run.StateInstance;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Lems;
import org.lemsml.type.Target;
import org.lemsml.type.dynamics.Dynamics;
import org.lemsml.type.simulation.Simulation;
import org.lemsml.type.structure.Structure;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.RuntimeError;
import org.lemsml.xml.ReflectionInstantiator;
import org.lemsml.xml.XMLReader;

public class LemsProcess {

	protected Class<?> root;
	protected String srcfnm;
	protected File srcFile;
	protected String srcStr;
	protected Lems lems;
	public static final int NONE = 0;
	public static final int PATH = 1;
	public static final int FILE = 2;
	public static final int STRING = 3;
	public static final int BUILT = 4;
	protected int mode = NONE;

	boolean allowConsolidation = true;
	 
	
	  public LemsProcess(Class<?> c, String fnm) {
	        root = c;
	        srcfnm = fnm;
	        mode = PATH;
	    }

	    public LemsProcess(File file) {
	        srcFile = file;
	        mode = FILE;
	    }

	    public LemsProcess(String srcStr) {
	        this.srcStr = srcStr;
	        mode = STRING;
	    }

	    public LemsProcess(Lems lems) {
	        this.lems = lems;
	        mode = BUILT;
	    }
	
	 
	    public void setNoConsolidation() {
	    	allowConsolidation = false;
	    }
	    
	
	
	public void readModel() throws ContentError {
		ReflectionInstantiator ri = new ReflectionInstantiator();
		ri.addSearchPackage(Lems.class.getPackage());
		ri.addSearchPackage(Dynamics.class.getPackage());
		ri.addSearchPackage(Structure.class.getPackage());
		ri.addSearchPackage(Simulation.class.getPackage());
		ri.addSearchPackage(Procedure.class.getPackage());
		
		NameMapper cm = new LemsMap();
		ri.setImportNameMapper(cm);
		readModel(ri, false);
		 
	}

 
	public void readModel(ReflectionInstantiator refin, boolean loose) throws ContentError {
	    String stxt = "";
	
	    if (mode == FILE) {
	        E.info("Reading model from " + srcFile.getAbsolutePath());
	
	        FileInclusionReader fir = new FileInclusionReader(srcFile);
	        stxt = fir.read();
	        
	    } else if (mode == PATH) {
	        PathInclusionReader pir = new PathInclusionReader(root, srcfnm);
	        stxt = pir.read();
	    
	    } else if (mode == STRING) {
	        stxt = srcStr.trim();
	
	        if (stxt.startsWith("<?xml")) {
	            int index = stxt.indexOf(">");
	            stxt = stxt.substring(index + 1).trim();
	        }
	        
	
	        StringInclusionReader sir = new StringInclusionReader(stxt);
	        stxt = sir.read();
	
	    }
	
	
	
	    XMLReader xmlr = new XMLReader(refin);
	
	
	    try {
	        lems = (Lems) (xmlr.read(stxt));
	     
	        if (loose) {
	        	lems.setResolveModeLoose();
	        }
	        
	        lems.deduplicate();
	        lems.resolve();
	        lems.evaluateStatic();
	        
	        
	        
	
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }          
	
	
	}

	public void print() {
	    E.info("Model:\n" + lems.textSummary());
	}

	
	public String canonicalText() {
	    CanonicalWriter cw = new CanonicalWriter(lems);
	    return cw.writeText();
	}

	public Lems getLems() {
	    return lems;
	}

	
	
	public void process() throws ContentError, ConnectionError, ParseError, RuntimeError {
		for (Target dr : lems.getTargets()) {

			Component cpt = dr.getComponent();

			ComponentType ct = cpt.getComponentType();
			
			for (Procedure pr : ct.getProcedures()) {
				
				ExecutableProcedure ep = pr.makeExecutableProcedure(lems);
				
				// E.info("Running procedure: " + pr + " on " + cpt);
				
				EventManager eventManager = new EventManager();
				StateInstance so = lems.build(cpt.getComponentBehavior(), eventManager);
 
				ep.execute(so);
			}
			
		}
		
	}
	
	
	
	
	
	
	
}

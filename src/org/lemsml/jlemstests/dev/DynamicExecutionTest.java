package org.lemsml.jlemstests.dev;

import java.io.File;
import java.io.IOException;
 

import org.codehaus.commons.compiler.jdk.JavaSourceClassLoader;
import org.lemsml.javagen.JavaGenerator;
import org.lemsml.jlems.codger.CodeGenerationException;
import org.lemsml.jlems.codger.CompilationException;
import org.lemsml.jlems.codger.StateTypeGenerator;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.GeneratedInstance;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.run.StateType;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.sim.Sim;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.logging.DefaultLogger;
import org.lemsml.jlemsio.reader.FileInclusionReader;
import org.lemsml.jlemsviz.datadisplay.SwingDataViewerFactory;
import org.lemsml.jruntime.NativeType;


public class DynamicExecutionTest {

	
	public static void main(String[] argv) {
		DefaultLogger.initialize();
		SwingDataViewerFactory.initialize();
		
		DynamicExecutionTest dct = new DynamicExecutionTest();
		try {
			File f1 = new File("examples/example1.xml");
			dct.generateAndRun(f1);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public void generateAndRun(File fmod) throws CodeGenerationException, CompilationException, 
	ContentError, ConnectionError, ParseError, RuntimeError, ParseException, BuildException, XMLException {
				
		File fsrc = new File("src");
		
		String pkgName = "org.lemsml.dynamic";
		String cptid = "na";
		
		generateFromFile(fmod, "na", fsrc, pkgName);
		
		Class cptClass = compileAndLoad(fsrc, pkgName, cptid);
	
		
	
    	FileInclusionReader fir = new FileInclusionReader(fmod);
    	Sim sim = new Sim(fir.read());

        sim.readModel();
        
        sim.setMaxExecutionTime(1000);

     

        
        
        
        NativeType nta = new NativeType("na", cptClass);
        
        nta.enableTiming();
        
        NativeType ntk = new NativeType("k", cptClass);

         sim.addSubstitutionType(nta);
       // sim.addSubstitutionType(ntk);
        
        
        
        Lems lems = sim.getLems();
        Component cna = lems.getComponent("na");
        StateType cst = cna.getStateType();
        cst.enableTiming();
        E.info("Enabled timing on " + cst.hashCode());
        
        sim.build();
        
        Component cna2 = lems.getComponent("na");
        StateType cst2 = cna.getStateType();
        E.info("post build " + cst2.hashCode());
        
        long t1 = System.currentTimeMillis();
        sim.runTree();
        
        long t2 = System.currentTimeMillis();
         E.info("Execution took " + (t2 - t1));
        
   
		
        E.info("Time in cpt type " + cst.getID() + ": " + Math.round(1.e-6 * cst.getTotalTime()));
	
        E.info("Time in native type " + nta.getID() + ": " + Math.round(1.e-6 * nta.getTotalTime()));
	}
	
 
	
	
	  private void generateFromFile(File f, String tgtid, File destdir, String pkgName) throws CodeGenerationException {
		  E.info("Loading LEMS file from: " + f.getAbsolutePath());

		  try {
		  FileInclusionReader fir = new FileInclusionReader(f);
		  Sim sim = new Sim(fir.read());

		  sim.readModel();

		  sim.build();

		  //sim.run();

		  Lems lems = sim.getLems();
  
		  StateTypeGenerator cg = new StateTypeGenerator(pkgName);
  
		  Component cna = lems.getComponent("na");
		  cg.addStateType(cna.getStateType());
 
  //for (Component cpt : lems.getComponents()) {
  //	E.info("Adding cpt " + cpt.getID());
  //	cg.addStateType(cpt.getStateType());
  //}
  	
		  String srcCode = cg.getCombinedJavaSource();
	
		  JavaGenerator jg = new JavaGenerator();
		  jg.writeSourceFiles(destdir, cg);
		  
		  
		  // cg.getJavaSource(tgtid);
	
		//  E.info("Generated code:\n\n" + srcCode);
		  } catch (Exception ex) {
			  ex.printStackTrace();
			  throw new CodeGenerationException(ex);
		  }
	  }


		
		public Class compileAndLoad(File srcPath, String rtPkg, String cnm) throws CompilationException   {
			Class ret = null;
			try {
			ClassLoader pcl = getClass().getClassLoader();
			
			File[] spath = {srcPath};
			
			JavaSourceClassLoader cl = new JavaSourceClassLoader(pcl);
			cl.setSourcePath(spath);
		 	
		
			ret = cl.loadClass(rtPkg + "." + cnm);
			E.info("Loaded class " + ret);
			
			Object inst = ret.newInstance();
			E.info("instantiated one " + inst);
			
			GeneratedInstance gi = (GeneratedInstance)inst;
		//	gi.advance(0.01);
			} catch (Exception ex) {
				throw new CompilationException(ex);
			}
			return ret;
		}
		
	  
	 
	

    public boolean executeExample(String filename) throws ContentError, ConnectionError, RuntimeError, ParseError, IOException, ParseException, BuildException, XMLException {
    	File fdir = new File("examples");
    	File f = new File(fdir, filename);
    	FileInclusionReader fir = new FileInclusionReader(f);
    	Sim sim = new Sim(fir.read());

        sim.readModel();
        
        sim.setMaxExecutionTime(1000);
        
        sim.build();
        sim.run();
        
        E.info("OK - executed " + filename);
        return true;
    }
    
    

   
	  
    
    
    
  
	
	 
	
	
}

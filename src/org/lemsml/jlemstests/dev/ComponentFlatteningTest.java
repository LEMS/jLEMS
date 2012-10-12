package org.lemsml.jlemstests.dev;

import java.io.File;
import java.io.IOException;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.flatten.ComponentFlattener;
import org.lemsml.jlems.logging.E;
 
import org.lemsml.jlems.run.ComponentBehavior;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.sim.Sim;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.reader.FileInclusionReader;
import org.lemsml.jlemsio.util.FileUtil;
import org.lemsml.jlemsio.xmlio.XMLSerializer;


public class ComponentFlatteningTest {

    public static void main(String[] args) throws ContentError, ParseError, ConnectionError, RuntimeError, IOException {
    	
    	File f1 = new File("examples/IaFcurr.xml");
    	File f2 = new File("examples/HHex.xml");
    	File f3 = new File("examples/NMDAex.xml");
    
    	try {
    		// flattenFromFile(f1, "iafTau0");
    		flattenFromFile(f2, "k");
//    		flattenFrimFile(f3, "nmdaSyn1");
    		
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    
    public static void flattenFromFile(File f, String tgtid) throws ContentError, ConnectionError, ParseError, IOException, RuntimeError, ParseException, BuildException, XMLException {
        E.info("Sys: " + System.getenv());
        E.info("Loading LEMS file from: " + f.getAbsolutePath());

        FileInclusionReader fir = new FileInclusionReader(f);
        Sim sim = new Sim(fir.read());

        sim.readModel();
     
        sim.build();

        //sim.run();

        Lems lems = sim.getLems();

        E.info("Found " + lems.getComponentTypes().size() + " Component Types:\n");
        //Component comp0 = lems.getComponent("nmdaSyn1");
        //Component comp0 = lems.getComponent("hhcell");
        //Component comp0 = lems.getComponent("k");
        Component comp0 = lems.getComponent(tgtid);

       
        E.info("Found: " + comp0);
        E.info("Children: " + comp0.getAllChildren());

        ComponentBehavior cb = comp0.getComponentBehavior();

        StateInstance si = cb.newInstance();

        E.info("Found: " + si.getMultiHM());

        //ComponentBehavior cb2 = cb.getConsolidatedComponentBehavior();
        //E.info("Found: " + cb2);

        ComponentType ct0 = comp0.getComponentType();

        String sout0 = XMLSerializer.serialize(comp0);

        E.info("-----------\n" + sout0 + "----------\n");
      
        String sout = XMLSerializer.serialize(ct0);

        E.info("-----------\n" + sout + "------\n");
 
        E.info(""+comp0.getParamValues());
        

        ComponentType ctNew = new ComponentType(ct0.getName()+"_flat");
        Component compNew = null; // new Component(comp0.getID()+"_flat", ctNew);

        
        ComponentFlattener cf = new ComponentFlattener();
        
        cf.parseAndAdd(compNew, comp0, ctNew, ct0, "");
        
        lems.addComponentType(ctNew);
       // lems.addComponent(compNew);
        compNew.resolve(lems, ctNew);


        String sout2 = XMLSerializer.serialize(ctNew);
        E.info("-----------\n" + sout2 + "------\n");
         

        String sout3 = XMLSerializer.serialize(compNew);
        E.info("-----------\n" + sout3 + "------\n");
        
//        Component net = lems.getComponent("net1");
//        Component pop = net.getChildrenAL("populations").get(1);
//		pop.getRefHM().put("component",compNew);
 //       System.out.println("net: "+net+", pop: "+pop.getRefHM());
        //pop.paramValues.getByName("component")., comp0.getID()+"_flat");

        
        lems.resolve();
        String lemsString  = XMLSerializer.serialize(lems);

        //E.info("Created: \n"+lemsString);
        //E.info("Info: \n"+lems.textSummary());

        File testFile = new File(new File("/tmp"), f.getName().replaceAll(".xml", "")+"_SBML.xml");

        FileUtil.writeStringToFile(lemsString, testFile);

        E.info("Written to: "+ testFile.getCanonicalPath());
    }
}

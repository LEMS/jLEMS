package org.lemsml.jlems.core.run;

import java.util.HashMap;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.RunnableAccessor;
import org.lemsml.jlems.core.type.Component;

public class WithBuilder extends AbstractPostBuilder {

	String path;
	String var;
	
    public WithBuilder(String instance, String as) {
        super();
        //E.info("WithBuilder created, instance: "+instance+", as: "+as);
    	path = instance;
        var = as;
    }

    public void postBuild(StateRunnable base, HashMap<String, StateRunnable> siHM, BuildContext bc) throws ConnectionError {
        RunnableAccessor ra = new RunnableAccessor(base);


        StateRunnable sr = null;
        //E.info("WithBuilder needs "+path+" relative to: "+base);
        
        if (path.equals("this")) {
            sr = base;
        
        } else if(path.equals("parent")) {
            sr = base.getParent();
        
        } else if(path.startsWith("this/")) {
            sr = ra.getRelativeStateInstance(base, path.substring(5));
        
        } else {
            sr = ra.getRelativeStateInstance(base.getParent(), path);
        }
        
        //E.info("WithBuilder path "+path+" (="+var+") resolved to: "+sr);

        siHM.put(var, (StateInstance) sr);

    }

	@Override
	public void consolidateStateTypes() {
		// TODO Auto-generated method stub
		
	}

 
 

}

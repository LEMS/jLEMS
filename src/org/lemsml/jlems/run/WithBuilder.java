package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.sim.RunnableAccessor;
import org.lemsml.jlems.type.Component;

public class WithBuilder extends AbstractPostBuilder {

	String path;
	String var;
	
    public WithBuilder(String instance, String as) {
        super();
    	path = instance;
        var = as;
    }

    public void postBuild(StateRunnable base, HashMap<String, StateRunnable> siHM, BuildContext bc) throws ConnectionError {
        RunnableAccessor ra = new RunnableAccessor(base);


        StateRunnable sr = null;
        //E.info("WithBuilder needs "+path+" relative to: "+base);
        
        if (path.equals(Component.THIS_COMPONENT)) {
            sr = base;
        }
        else if(path.equals(Component.PARENT_COMPONENT)) {
            sr = base.getParent();
        }
        else {
            sr = ra.getRelativeStateInstance(base.getParent(), path);
        }
        
        //E.info("WithBuilder path "+path+" ( = "+var+") resolved to: "+sr);

        siHM.put(var, (StateInstance) sr);

    }

	@Override
	public void consolidateStateTypes() {
		// TODO Auto-generated method stub
		
	}

 
 

}

package org.lemsml.run;

import java.util.HashMap;

import org.lemsml.sim.RunnableAccessor;
import org.lemsml.type.Component;

public class WithBuilder extends PostBuilder {

	String path;
	String var;
	
    public WithBuilder(String instance, String as) {
        path = instance;
        var = as;
    }

    public void postBuild(StateInstance base, HashMap<String, StateInstance> siHM, BuildContext bc) throws ConnectionError {
        RunnableAccessor ra = new RunnableAccessor(base);


        StateInstance sr = null;
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
	public void consolidateComponentBehaviors() {
		// TODO Auto-generated method stub
		
	}

 
 

}

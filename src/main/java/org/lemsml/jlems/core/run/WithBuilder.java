package org.lemsml.jlems.core.run;

import java.util.HashMap;

import org.lemsml.jlems.core.sim.RunnableAccessor;

public class WithBuilder extends AbstractPostBuilder {

    String path;
    String var;

    public WithBuilder(String instance, String as) {
        super();
        path = instance;
        var = as;
        //E.info("made with builder " + instance + " " + as);
    }

    @Override
    public void postBuild(StateRunnable base, HashMap<String, StateRunnable> siHM, BuildContext bc) throws ConnectionError {
        RunnableAccessor ra = new RunnableAccessor(base);

        //E.info("with builder building...");
        StateRunnable sr;
        //E.info("WithBuilder needs " + path + " relative to: " + base);

        if (path.equals("this")) {
            sr = base;
        } else if (path.equals("parent")) {
            sr = base.getParent();

        } else if (path.startsWith("./")) {
            sr = ra.getRelativeStateInstance(base, path.substring(2));

            //E.info("1 got rel state instance for path " + path + ": " + sr);
        } else {
            sr = ra.getRelativeStateInstance(base.getParent(), path);

            //E.info("2 got rel state instance for path " + path + ": " + sr);
        }

        //E.info("WithBuilder path " + path + " (= " + var + ") resolved to: " + sr);
        siHM.put(var, (StateInstance) sr);

    }

    @Override
    public void consolidateStateTypes() {
        // TODO Auto-generated method stub

    }

}

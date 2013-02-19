package org.lemsml.jlems.viz.plot;


public interface Pickable {


     Object getRef();


   // maybe the least bad solution?
     void setCache(int i);

     int getCache();

}

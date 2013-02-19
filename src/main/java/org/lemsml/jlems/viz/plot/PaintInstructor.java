package org.lemsml.jlems.viz.plot;
 


public interface PaintInstructor {


     boolean antialias();

     void instruct(Painter p);
     
     Box getLimitBox();
   

}

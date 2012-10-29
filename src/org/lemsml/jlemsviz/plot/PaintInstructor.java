package org.lemsml.jlemsviz.plot;
 


public interface PaintInstructor {


     boolean antialias();

     void instruct(Painter p);
     
     Box getLimitBox();
   

}

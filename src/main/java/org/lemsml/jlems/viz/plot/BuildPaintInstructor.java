package org.lemsml.jlems.viz.plot;
 

public interface BuildPaintInstructor {


     boolean antialias();


     void instruct(Painter p, Builder b);


	Box getLimitBox(Painter p);


}

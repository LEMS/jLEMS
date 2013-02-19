package org.lemsml.jlems.viz.plot;


public interface MoviePaintInstructor extends PaintInstructor {

   void advanceToFrame(int ifr);
   
   void setFrame(int ifr);
   
   int getNFrames();

   String getFrameDescription(int ifr);
   
}

package org.lemsml.jlems.viz.plot;
 

public interface PickListener {


   void backgroundPressed(int i, int x, int y);

   void pickPressed(Pickable pbl, int button, int ix, int iy);

   void pickDragged(Pickable pbl, Position pos, int button, int ix, int iy);

   void pickReleased(Pickable pbl, int button);

   void pickEnteredTrash(Pickable pbl);

   void pickLeftTrash(Pickable pbl);

   void pickTrashed(Pickable pbl);

   void trashPressed();

   void pickHovered(Pickable hoverItem);

}

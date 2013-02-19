package org.lemsml.jlems.viz.plot;




public class Builder {

   Painter painter;

   PickStore pickStore;

   int width;
   int height;


   public Builder(Painter p, PickStore ps) {
      painter = p;
      pickStore = ps;
   }


   public void setCanvasSize(int w, int h) {
	   width = w;
	   height = h;
   }



   public void clear() {
      pickStore.clear();
   }


   public void add3DPickablePoint(double x, double y, double z, PickablePoint pp) {
	   double wx = painter.xProj(x, y, z);
	   double wy = painter.yProj(x, y, z);
	   pp.moveTo(wx, wy);

	   int ix = painter.powx(wx);
	   int iy = painter.powy(wy);
	//   if (ix > 0 && iy > 0 && ix < width && iy < height) {
		   pickStore.addPoint(pp, ix, iy);
	//   }
   }


   public void addPoint(PickablePoint pp) {
      addPickablePoint(pp);
   }




   public void addPickablePoint(PickablePoint pp) {
      addPickablePoint(pp, pp.getPosition());
   }



   public void addPickablePoint(PickablePoint pp, Position pos) {
      // or could get the world transform ourselves?
      int ix = painter.powx(pos.getX());
      int iy = painter.powy(pos.getY());

      pickStore.addPoint(pp, ix, iy);

      painter.fillPixelRectangle(ix, iy, pp.getColor(), pp.getSize());

   }



   public void addPickableRegion(PickableRegion pka) {
      pickStore.addPickableRegion(pka,
				  painter.powx(pka.getXReference()),
				  painter.powy(pka.getYReference()));

   }



public void addPickableRegion(Pickable pbl, int x, int y, int w, int h) {
	pickStore.addPoint(pbl, x, y, x + w, y + h);

}







public void add2DPickableRegionOn3DPoint(Pickable pbl, double x, double y, double z,
										int dx, int dy, int w, int h) {
	 double wx = painter.xProj(x, y, z);
	 double wy = painter.yProj(x, y, z);

	 int ix = painter.powx(wx);
     int iy = painter.powy(wy);
     addPickableRegion(pbl, ix + dx, iy + dy, w, h);

}



}

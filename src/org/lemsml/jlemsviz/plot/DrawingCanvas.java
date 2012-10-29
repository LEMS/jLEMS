
package org.lemsml.jlemsviz.plot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;



public class DrawingCanvas extends BasePanel {
   static final long serialVersionUID = 1001;

   PickWorldCanvas pwCanvas;



   public DrawingCanvas(int w, int h) {
      super();
      setPreferredSize(new Dimension(w, h));

      pwCanvas = new PickWorldCanvas(w, h - 20, true); // POSERR - ever used in batch mode?


      setLayout(new BorderLayout());
      add("Center", pwCanvas);
  }

   public void reframe() {
	   pwCanvas.reframe();
   }

   public void setMouseMode(String s) {
	   pwCanvas.setMouseMode(s);
   }

   public void setPaintInstructor(PaintInstructor pi) {
      pwCanvas.setPaintInstructor(pi);
   }


   public void setBuildPaintInstructor(BuildPaintInstructor bpi) {
      pwCanvas.setBuildPaintInstructor(bpi);
   }

   public void setPickListener(PickListener pl) {
      pwCanvas.setPickListener(pl);
   }


   public void setClickListener(ClickListener cl) {
      pwCanvas.setClickListener(cl);
   }

   public void setRotationListener(RotationListener rl) {
	   pwCanvas.setRotationListener(rl);
   }

   public boolean isAntialiasing() {
	   return pwCanvas.isAntialiasing();
   }

   public void setAntialias(boolean b) {
	   pwCanvas.setAntialias(b);
   }

   public void setOnGridAxes() {
	   pwCanvas.setOnGridAxes();
   }

   public void setThreeD() {
	   pwCanvas.setFixedAspectRatio(1.0);
	   // TODO tell it to add turn handlers?
   }

   public void attach(Object obj) {
      if (obj instanceof BuildPaintInstructor) {
	 setBuildPaintInstructor((BuildPaintInstructor)obj);

      } else if (obj instanceof PaintInstructor) {
	 setPaintInstructor((PaintInstructor)obj);
      }

      if (obj instanceof PickListener) {
	 setPickListener((PickListener)obj);
      }

      if (obj instanceof ClickListener) {
         setClickListener((ClickListener)obj);
      }

      if (obj instanceof RotationListener) {
    	  setRotationListener((RotationListener)obj);
      }

   }


   public void setBackgroundColor(Color c) {
	   pwCanvas.setDataBackground(c);

   }

   public void setGridColor(Color c) {
	   pwCanvas.setGridColor(c);
   }
   public void setAxisColor(Color c) {
	   pwCanvas.setAxisColor(c);
   }

   public void setXRange(double low, double high) {
      pwCanvas.setXRange(low, high);
   }

   public void setFixedAspectRatio(double ar) {
      pwCanvas.setFixedAspectRatio(ar);
   }


   public void viewChanged() {
      if (pwCanvas != null) {
	 pwCanvas.repaint();
      }
   }

public double[][] getProjectionMatrix() {
	 return pwCanvas.getProjectionMatrix();
}

public void setRollCenter(double x, double y, double z) {
	 pwCanvas.setRollCenter(x, y, z);

}

public void turn(double d) {
	pwCanvas.turn(d);
}

public double[] get3Center() {
	 return pwCanvas.get3Center();
}

public double[] get2Center() {
	 return pwCanvas.get2Center();
}

public void setShowGrid(boolean b) {
	pwCanvas.setShowGrid(b);

}

public void setFourMatrix(double[] fm) {
	 pwCanvas.setFourMatrix(fm);

}

public double[] getFourMatrix() {
	 return pwCanvas.getFourMatrix();
}

}

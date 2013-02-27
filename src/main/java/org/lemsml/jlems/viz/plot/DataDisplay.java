package org.lemsml.jlems.viz.plot;
 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.lemsml.jlems.core.logging.E;
 

public class DataDisplay extends BasePanel implements ModeSettable, Repaintable, PaintListener {

   static final long serialVersionUID = 1001;

   XAxisCanvas xAxisCanvas;
   YAxisCanvas yAxisCanvas;
   PickWorldCanvas pwCanvas;
   CornerPanel cornerPanel;

   BasePanel controlPanel;

   AboveBelowSplitPanel ab1;
   AboveBelowSplitPanel ab2;
   LeftRightSplitPanel lr;

   GraphColors gcols;
 
   RangeWatcher rangeWatcher;

   static boolean interactive = true;
   public static void setBatch() {
      interactive = false;
   }

   public DataDisplay() {
      super();
       
      gcols = new GraphColors();
      int leftmargin = 64;
      int bottommargin = 32;
      xAxisCanvas = new XAxisCanvas();
      yAxisCanvas = new YAxisCanvas();
      
      xAxisCanvas.setMinimumSize(new Dimension(200, bottommargin));
      yAxisCanvas.setMinimumSize(new Dimension(leftmargin, 200));
      
      xAxisCanvas.setPreferredSize(new Dimension(200, bottommargin));
      yAxisCanvas.setPreferredSize(new Dimension(leftmargin, 200));
      
      
      pwCanvas = new PickWorldCanvas(interactive);

      cornerPanel = new CornerPanel();


      ab1 = new AboveBelowSplitPanel(yAxisCanvas, cornerPanel, gcols);

      ab2 = new AboveBelowSplitPanel(pwCanvas, xAxisCanvas, gcols);


      ab1.setResizeWeight(0.95);
      ab2.setResizeWeight(0.95);

      lr = new LeftRightSplitPanel(ab1, ab2, gcols);

      lr.setResizeWeight(0.0);

      setLayout(new BorderLayout(0, 0));
      add("Center", lr);


      pwCanvas.addRangeListener(xAxisCanvas);
      pwCanvas.addRangeListener(yAxisCanvas);
      
      pwCanvas.addPaintListener(this);

   }


   public void repaintAll() {
      xAxisCanvas.invalidate();
      yAxisCanvas.invalidate();
      validateTree();
      xAxisCanvas.repaint();
      yAxisCanvas.repaint();
      repaint();
   }

  public void painted() {
	  int hc = pwCanvas.getHeight();
	  int ha = yAxisCanvas.getHeight();
	  if (ha != hc) {
		  ab1.setDividerLocation(hc);
	  }
  }

 

   public void setBg(Color c) {
      setDataBg(c);
      setBorderBg(c.brighter());
   }

   public void setDataBg(Color c) {
      gcols.setGraphBg(c);
      pwCanvas.setBg(c);
   }

   public void setBorderBg(Color c) {
      gcols.setBorderBg(c);

      xAxisCanvas.setBg(c);
      yAxisCanvas.setBg(c);
      cornerPanel.setBg(c);
   }


   public void setMode(String dom, String mod) {
      pwCanvas.setMode(dom, mod);
   }


   public void setMode(String dom, boolean b) {
      pwCanvas.setMode(dom, b);
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


   public void attach(Object obj) {
      boolean done = false;

      if (obj instanceof BuildPaintInstructor) {
         setBuildPaintInstructor((BuildPaintInstructor)obj);
         done = true;

      } else if (obj instanceof PaintInstructor) {
         setPaintInstructor((PaintInstructor)obj);
         done = true;
      }

      if (obj instanceof PickListener) {
         setPickListener((PickListener)obj);
         done = true;
      }

      if (!done) {
         E.error("cant attach " + obj + " to a data XDisplay");
      }
   }


   public void setXAxisLabel(String lab) {
      xAxisCanvas.setLabel(lab);
   }

   public void setYAxisLabel(String lab) {
      yAxisCanvas.setLabel(lab);
   }

   public void setXAxis(String lab, double min, double max) {
       setXAxisLabel(lab);
       setXRange(min, max);
   }





   public void setYAxis(String lab, double min, double max) {
      setYAxisLabel(lab);
      setYRange(min, max);
   }

   public void setYRange(double min, double max) {
      pwCanvas.setYRange(min, max);
   }


   public void setXXYYLimits(double[] d) {
	   double[] xyxy = {d[0], d[2], d[1], d[3]};
	   setLimits(xyxy);
	}


   public void setLimits(double[] xyxy) {
	  pwCanvas.syncSize(); 
	  pwCanvas.setXRange(xyxy[0], xyxy[2]);
      pwCanvas.setYRange(xyxy[1], xyxy[3]);
      pwCanvas.requestRepaint();
   }


   public void setXRange(double low, double high) {
      pwCanvas.setXRange(low, high);
   }


   public double[] getXRange() {
      return pwCanvas.getXRange();
   }


   public double[] getYRange() {
      return pwCanvas.getYRange();
   }


   public void setFixedAspectRatio(double ar) {
      pwCanvas.setFixedAspectRatio(ar);
   }


   public void viewChanged() {
      if (pwCanvas != null) {
         pwCanvas.repaint();
      }
   }


   public void reframe() {
      pwCanvas.reframe();
   }


   public static void main(String[] argv) {
      JFrame f = new JFrame();
      DataDisplay dataDisplay = new DataDisplay();
      f.setPreferredSize(new Dimension(500, 300));
      dataDisplay.setPaintInstructor(new Demo1());

      f.getContentPane().add(dataDisplay);
      f.pack();
      f.setVisible(true);
      
      
   }


   public void setColorRange(double cmin, double cmax) {
       pwCanvas.setColorRange(cmin, cmax);
   }

   public void setColorTable(Color[] ac) {
      pwCanvas.setColorTable(ac);
   }


   public void syncSizes() {
      pwCanvas.syncSize();
   }
 

public void frameData() {
	 pwCanvas.reframe();
	
}

public void requestRepaint() {
	pwCanvas.requestRepaint();
}
 
public void setCursor(String string) {
	// TODO Auto-generated method stub
	
}


}

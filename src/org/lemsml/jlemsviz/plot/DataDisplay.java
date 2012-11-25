package org.lemsml.jlemsviz.plot;
 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.lemsml.jlems.logging.E;
 

public class DataDisplay extends BasePanel implements ModeSettable, RangeListener, Repaintable {

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

   Dimension prefDim;

   RangeWatcher rangeWatcher;

   static boolean interactive = true;
   public static void setBatch() {
      interactive = false;
   }

   public DataDisplay(int w, int h) {
      super();
 
      gcols = new GraphColors();
      int leftmargin = 64;
      int bottommargin = 22;
      xAxisCanvas = new XAxisCanvas(w - leftmargin, bottommargin);
      yAxisCanvas = new YAxisCanvas(leftmargin, h - bottommargin);
      pwCanvas = new PickWorldCanvas(w - leftmargin, h - bottommargin, interactive);

      cornerPanel = new CornerPanel(leftmargin, bottommargin, pwCanvas);


      ab1 = new AboveBelowSplitPanel(yAxisCanvas, cornerPanel, gcols);

      ab2 = new AboveBelowSplitPanel(pwCanvas, xAxisCanvas, gcols);


      ab1.setResizeWeight(1.0);
      ab2.setResizeWeight(1.0);


      setPrefSize(w, h);

      // NB calls setDividerLocation which has side effect of starting event thread
      ab1.setSplitPanelFollower(ab2);
      ab2.setSplitPanelFollower(ab1);


      lr = new LeftRightSplitPanel(ab1, ab2, gcols);

      lr.setResizeWeight(0.1);

      setLayout(new BorderLayout(0, 0));
      add("Center", lr);


      pwCanvas.addRangeListener(xAxisCanvas);
      pwCanvas.addRangeListener(yAxisCanvas);

   }


   public void repaintAll() {
      xAxisCanvas.invalidate();
      yAxisCanvas.invalidate();
      validateTree();
      xAxisCanvas.repaint();
      yAxisCanvas.repaint();
      repaint();
   }

   public void addRangeWatcher(RangeWatcher rw) {
      if (rangeWatcher == null) {
         pwCanvas.addRangeListener(this);
         rangeWatcher = rw;
      } else {
         E.error("cant add another range watcher - already watching");
      }
   }

   public void rangeChanged(int mode, double[] newLimits) {
      if (rangeWatcher != null) {
         rangeWatcher.rangeChanged();
      }
   }




   public Dimension getPreferredSize() {
      return prefDim;
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



   public void setLimits(double[] xyxy) {
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
      DataDisplay wc = new DataDisplay(500, 300);
      wc.setPaintInstructor(new Demo1());

      f.getContentPane().add(wc);
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

   public final void setPrefSize(int w, int h) {

      prefDim = new Dimension(w, h);
      setPreferredSize(prefDim);

      int leftmargin = 64;
      int bottommargin = 22;
      xAxisCanvas.setPreferredSize(w - leftmargin, bottommargin);
      yAxisCanvas.setPreferredSize(leftmargin, h - bottommargin);
      pwCanvas.setPreferredSize(w - leftmargin, h - bottommargin);
      xAxisCanvas.setMinimumSize(new Dimension(100, bottommargin - 20));
      yAxisCanvas.setMinimumSize(new Dimension(leftmargin - 20, 100));
      cornerPanel.setMinimumSize(new Dimension(leftmargin - 20, bottommargin - 20));


      // NB - these cause the AWT event thread to be started (which delays exit in batch mode)
      ab1.setDividerLocation(h - leftmargin);
      ab2.setDividerLocation(h - bottommargin);
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

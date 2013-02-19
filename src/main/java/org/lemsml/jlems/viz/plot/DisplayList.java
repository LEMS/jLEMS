package org.lemsml.jlems.viz.plot;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayList {

	Repaintable repaintable;
	
	
	ArrayList<DisplayLine> lines;
	HashMap<String, DisplayLine> lineHM;
	
	
	public DisplayList() {
		lines = new ArrayList<DisplayLine>();
		lineHM = new HashMap<String, DisplayLine>();
	}
 	
	public void setRepaintable(Repaintable rp) {
		repaintable = rp;		
	}

	public ArrayList<DisplayLine> getLines() {
		return lines;
	}
	
	public void addPoint(String sl, double x, double y) {
		 addPoint(sl, x, y, null);
	}

	public void addPoint(String sl, double x, double y, String color) {
		if (lineHM.containsKey(sl)) {
			lineHM.get(sl).addPoint(x, y);
			
		} else {
			DisplayLine dl = new DisplayLine(sl, color);
			lineHM.put(sl, dl);
			lines.add(dl);
			dl.addPoint(x, y);
		}
		
	}

	public void addLine(double[] xp, double[] yp, String color) {
		DisplayLine dl = new DisplayLine("", color);
		dl.setPoints(xp, yp);
		lines.add(dl);
	}

	public void clear() {
		lines = new ArrayList<DisplayLine>();
		lineHM = new HashMap<String, DisplayLine>();
	}

}

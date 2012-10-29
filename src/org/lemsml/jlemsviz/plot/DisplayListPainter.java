package org.lemsml.jlemsviz.plot;

public class DisplayListPainter implements PaintInstructor {

	
	Repaintable repaintable;
	
	DisplayList displayList;
	
	public DisplayListPainter(DisplayList dl) {
		displayList = dl;
	}
	
	public boolean antialias() {
		return true;
	}

	@Override
	public Box getLimitBox() {
		Box ret = new Box();
		for (DisplayLine dl : displayList.getLines()) {
			ret.push(dl.getXpts(), dl.getYpts(), dl.getNpts());
		}
		return ret;
	}
 
	
	
	public void instruct(Painter p) {
		for (DisplayLine dl : displayList.getLines()) {
			p.setColor(dl.getColor());
			int np = dl.getNpts();
			if (np >= 2) {
				p.drawPolyline(dl.getXpts(), dl.getYpts(), np);
			}
		}
	}

	
	public void setRepaintable(Repaintable rb) {
		 repaintable = rb;
		
	}

}

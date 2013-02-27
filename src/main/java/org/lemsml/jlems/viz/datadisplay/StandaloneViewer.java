package org.lemsml.jlems.viz.datadisplay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import org.lemsml.jlems.core.display.DataViewPort;
import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.io.data.FormattedDataUtil;
import org.lemsml.jlems.io.util.FileUtil;
import org.lemsml.jlems.viz.plot.DataDisplay;
import org.lemsml.jlems.viz.plot.DisplayLine;
import org.lemsml.jlems.viz.plot.DisplayList;
import org.lemsml.jlems.viz.plot.DisplayListPainter;
import org.lemsml.jlems.viz.plot.PaintInstructor;
import org.lemsml.jlems.viz.plot.WorldCanvas;

public final class StandaloneViewer implements ActionListener, DataViewer, DataViewPort {
	JFrame frame;

	DataDisplay dataDisplay;

	DisplayList displayList;

	JButton frameB;

	JPanel ptop = new JPanel();

	Color mainBackground = new Color(120, 120, 120);
	// RCC displayList contains all the data that traceInfo was saving
	
	boolean setRange = false;
	double[] region;
	
	Long lastUpdate = 0l;

	public StandaloneViewer() {
		this("");
	}

	public String getTitle() {
		return frame.getTitle();
	}

	public StandaloneViewer(String title) {
		frame = new JFrame(title);
		frame.setPreferredSize(new Dimension(550, 450));
		Container ctr = frame.getContentPane();

		// viewer = new SceneGraphViewer();
		ctr.setLayout(new BorderLayout(2, 2));
		// ctr.add(viewer.getPanel(), BorderLayout.CENTER);

	 
		JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("File");
		String[] actions = {"Open", "Save", "Import", "Clear", "Exit"};
		addToMenu(actions, jm);
		jmb.add(jm);
		
		
		JMenu jmview = new JMenu("View");
		String[] va = {"Frame", "Legend"};
		addToMenu(va, jmview);
		jmb.add(jmview);
		
		
		JMenu jmmouse = new JMenu("Mouse");
		String[] ma = {"Pan", "Zoom", "Box", "Multi" };
		addToMenu(ma, jmmouse);
		jmb.add(jmmouse);
		
		
		
		ctr.add(jmb, BorderLayout.NORTH);
		
		
		
		dataDisplay = new DataDisplay();
		ctr.add(dataDisplay, BorderLayout.CENTER);

		dataDisplay.setBg(mainBackground);
		dataDisplay.setMode("antialias", true);

		dataDisplay.setMode("mouse", WorldCanvas.PAN);

		dataDisplay.setMode("labels", true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		displayList = new DisplayList();

		setData(displayList);
		
		show();
	
		checkUserPref();
	}
	
	
	private File fpref() {
		File fu = new File(System.getProperty("user.home"));
		File fjl = new File(fu, ".jlems");
		return fjl;
	}
	
	
	private void checkUserPref() {
		try {
	 
		File fjl = fpref();
		
		if (fjl.exists()) {
			String s = FileUtil.readStringFromFile(fjl);
			for (String line : s.split("\n")) {
				if (line.indexOf("mouseMode") == 0) {
					String[] bits = line.split(":");
					if (bits.length >= 2) {
						dataDisplay.setMode("mouse", bits[1].trim());
					}
				}
			}
		}
		} catch (Exception ex) {
			E.warning("couldn't read preferences");
		}
	}
	
	
	private void setPref(String pf, String v) {
		try {
		File fjl = fpref();
		String wk = "";
		if (fjl.exists()) {
			String s = FileUtil.readStringFromFile(fjl);
			for (String line : s.split("\n")) {
				String[] bits = line.split(":");
				if (bits.length > 0 && bits[0].trim().equals(pf)) {
					// leave it out
				} else {
					wk += line + "\n";
				}
			}
		}
		wk += pf + " : " + v + "\n";
		FileUtil.writeStringToFile(wk, fjl);
		E.info("Written " + fjl.getAbsolutePath());
		} catch (Exception ex) {
			E.error("coldn't save preferences: " + ex);
		}
	}
	
	

	private void addToMenu(String[] actions, JMenu jm) {
	for (String s : actions) {
		JMenuItem jmi = new JMenuItem(s);
		jmi.setActionCommand(s.toLowerCase());
		jmi.addActionListener(this);
		jm.add(jmi);
	}
	}
	
	
	
	
	
	
	public void setRegion(double[] d) {
		if (d != null && d.length == 4) {
			region = d;
			dataDisplay.setXXYYLimits(d);
			setRange = true;
		}
	}
	
	
	public void setPainter(PaintInstructor pi) {
		dataDisplay.setPaintInstructor(pi);
	}

	public void show() {
		frame.pack();
		frame.setVisible(true);
	}

	public void frameData() {
		dataDisplay.frameData();
	}

	public void legend() {

		String lgnd = buildHTMLLegend();
		
		JOptionPane.showMessageDialog(frame, lgnd);
	}

	private void setData(DisplayList dl) {
		DisplayListPainter dlp = new DisplayListPainter(dl);
		dlp.setRepaintable(dataDisplay);
		setPainter(dlp);
	}

	public void actionPerformed(ActionEvent e) {
		String sev = e.getActionCommand();
		
		if (sev.equals("import")) {
			importFile();
			
		} else if (sev.equals("clear")) {
			displayList.clear();
			dataDisplay.repaint();
			
		} else if (sev.equals("exit")) {
			frame.dispose();

		} else if (sev.equals("frame")) {
			frameData();

		} else if (sev.equals("legend")) {
			legend();

		} else if (sev.equals("pan")) {
			dataDisplay.setMode("mouse", WorldCanvas.PAN);
			setPref("mouseMode", WorldCanvas.PAN);
			

		} else if (sev.equals("zoom")) {
			dataDisplay.setMode("mouse", WorldCanvas.EZOOM);
			setPref("mouseMode", WorldCanvas.EZOOM);

		} else if (sev.equals("box")) {
			dataDisplay.setMode("mouse", WorldCanvas.BOX);
			setPref("mouseMode", WorldCanvas.BOX);

		} else if (sev.equals("multi")) {
			dataDisplay.setMode("mouse", WorldCanvas.MULTI);
			setPref("mouseMode", WorldCanvas.MULTI);
			
		 	
		} else {		
			dataDisplay.setMode("mouse", sev);
		}
	}

	public void addPoint(String s, double x, double y, String color) {
		displayList.addPoint(s, x, y, color);	
		
	 
		checkUpdate();
	}

	public void addPoint(String s, double x, double y) {
		addPoint(s, x, y, "#ffffff");
	}

	private void checkUpdate() {
		long currentTime = System.nanoTime() / 1000000;
		if (currentTime - lastUpdate > 250) {
			lastUpdate = currentTime;
		 	
			if (!setRange) {
				frameData();
			}
			dataDisplay.requestRepaint();
		}
	}

	public static void main(String[] argv) {
		StandaloneViewer sv = new StandaloneViewer();

		int numPoints = 200;
		for (int i = 0; i < numPoints - 5; i++) {
			double maxR = 2 * Math.PI * 2;
			double r = maxR * i / numPoints;
			sv.addPoint("y = sin(x)", r, Math.sin(r), "#00FF00");
			sv.addPoint("y = cos(x)", r, Math.cos(r), "#0000FF");
			sv.addPoint("y = abs(x)", r, Math.abs(r));
		}

		sv.frameData();
		sv.show();
	}

	
	
	public String buildHTMLLegend() {
		StringBuilder sb = new StringBuilder("<html><b>Traces present:</b><br/>");
		for (DisplayLine dl : displayList.getLines()) {
			sb.append("&nbsp;&nbsp;<font color=\"" + dl.getColor() + "\">----- " + dl.getName() + "</font><br/>");
		}		
		return sb.toString();
	}
	
	
	
	@Override
	public void showFinal() {
//		frameData();
//		show();
	}

	
	
	
	public void importFile() {
		File f = SwingDialogs.getInstance().getFileToRead();
		if (f != null) {
			double[][] dat = FormattedDataUtil.readDataArray(f);
			double[][] cols = FormattedDataUtil.transpose(dat);
			
			int nc = cols.length;
			for (int i = 1; i < nc; i++) {
				displayList.addLine(cols[0], cols[i], "#00ff00");
			}
			dataDisplay.repaint();
		}
	}
	
	
}

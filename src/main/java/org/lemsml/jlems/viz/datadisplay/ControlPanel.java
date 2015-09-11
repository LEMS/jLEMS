package org.lemsml.jlems.viz.datadisplay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.lemsml.jlems.core.run.RunConfig;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.io.reader.FileInclusionReader;

public class ControlPanel implements ActionListener {
	JFrame frame;
	
	File workingFile;
	
	Sim simulation;
	Map<Integer, RunConfig> runConfigs = new HashMap<Integer, RunConfig>();
	Map<String, Rectangle> viewerBounds = new HashMap<String, Rectangle>();
	
	JPanel pmain = new JPanel();
	Dimension windowDimension = new Dimension(200, 100);

	Color mainBackground = new Color(120, 120, 120);
	// RCC displayList contains all the data that traceInfo was saving
	
	boolean setRange = false;
	double[] region;
	
	Long lastUpdate = 0l;
	
	private static ControlPanel instance;
	
	public static ControlPanel getInstance(String title) {
		if(instance == null)
			instance = new ControlPanel(title);
		
		return instance;
	}

	public ControlPanel registerSimulation(Sim sim, File simFile) {
		simulation = sim;
		
		if(simFile != null) {
			workingFile = simFile;
		}
		
		loadRunConfigsFromSimulation();
		
		positionViewers();
		
		return this;
	}
	
	public String getTitle() {
		return frame.getTitle();
	}

	private ControlPanel(String title) {
		
		frame = new JFrame(title);
		frame.setPreferredSize(windowDimension);
		Container ctr = frame.getContentPane();

		ctr.setLayout(new BorderLayout(2, 2));
	 
		JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("File");
		String[] actions = {"Open", "Exit"};
		addToMenu(actions, jm);
		jmb.add(jm);
		
		JMenu jvm = new JMenu("View");
		String[] viewActions = {"Show All"};
		addToMenu(viewActions, jvm);
		jmb.add(jvm);
		
		JMenu jmsimulation = new JMenu("Simulation");
		addToMenuWithShortcut("Reload and Run", jmsimulation, KeyEvent.VK_F6, 0 );
		jmb.add(jmsimulation);	
		
		ctr.add(jmb, BorderLayout.NORTH);
		
		ctr.add(pmain, BorderLayout.WEST);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		show();
	}
	
	public void loadRunConfigsFromSimulation() {
		
		int index = -1;
		for(RunConfig conf : simulation.getRunConfigs()) {			
			runConfigs.put(index++, conf);
		}
		
	}
	
	public void positionViewers() {
		int borderWidth = 30;
		int start_cursor_x = (int)windowDimension.getWidth() + borderWidth;
		int start_cursor_y = 0;
		
		frame.setLocation(0, 0);
		
		// list of 3x2 matrix positions
		Dimension[] positions = {new Dimension(0,0),
								 new Dimension(0,1),
								 new Dimension(1,0),
								 new Dimension(1,1),
								 new Dimension(2,0),
								 new Dimension(2,1)};
		
		int viewIndex = 0;
		
		for(String key : simulation.getDvHM().keySet()) {
			if(simulation.getDvHM().get(key) instanceof StandaloneViewer) {
				StandaloneViewer sViewer = ((StandaloneViewer)simulation.getDvHM().get(key));
				
				if(viewerBounds.containsKey(key)) {
					sViewer.setViewerRectangle(viewerBounds.get(key));
					continue;
				}
				
				int layer = viewIndex / 6;
				Dimension pos = positions[(viewIndex%6)];
				
				int cursor_x = (int)(start_cursor_x + (layer*borderWidth) + pos.getWidth() * (sViewer.getDimensions().getWidth() + borderWidth));
				int cursor_y = (int)(start_cursor_y + (layer*borderWidth) + pos.getHeight() * (sViewer.getDimensions().getHeight() + borderWidth));
				
				sViewer.setPosition(cursor_x, cursor_y);
				
				viewIndex++;
			}
		}
		
		//give focus to control panel
		frame.setVisible(true);

	}

	private void addToMenu(String[] actions, JMenu jm) {
	for (String s : actions) {
		JMenuItem jmi = new JMenuItem(s);
		jmi.setActionCommand(s.toLowerCase());
		jmi.addActionListener(this);
		jm.add(jmi);
	}
	}
	
	/**
	 * 
	 * @param action - The name of the action item
	 * @param jm - the menu for this item to be added to
	 * @param key - int representing the ID of KeyEvent (eg KeyEvent.VK_F6)
	 * @param modifier - int representing the ID of ActionEvent (eg ActionEvent.ALT_MASK , 0 for no modifier)
	 */
	private void addToMenuWithShortcut(String action, JMenu jm, int key, int modifier) {
		JMenuItem jmi = new JMenuItem(action);
		jmi.setActionCommand(action.toLowerCase());
		jmi.addActionListener(this);
		jmi.setAccelerator(KeyStroke.getKeyStroke(key, modifier));
		jm.add(jmi);
	}
	
	public void show() {
		frame.pack();
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String sev = e.getActionCommand();
		
		if (sev.equals("open")) {
			importNewFile();
			try {
				simulation.run();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(new JFrame(), 
						String.format("Failed to run simulation : %s", ex.getMessage()), 
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			
		} else if (sev.equals("exit")) {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		} else if(sev.equals("show all")) {
			bringAllViewersToForeground();
		} else if (sev.equals("reload and run")) {
			if(simulation != null)
				try {
					clearCurrentSimulation();
					importFile();
					for(Entry<Integer, RunConfig> conf : runConfigs.entrySet()) {
						simulation.run(conf.getValue(), false);
					}
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(new JFrame(), 
							String.format("Failed to run simulation : %s", ex.getMessage()), 
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	
	public void clearCurrentSimulation() {
		for(String key : simulation.getDvHM().keySet()) {
			if(simulation.getDvHM().get(key) instanceof StandaloneViewer) {
				StandaloneViewer viewer = ((StandaloneViewer)simulation.getDvHM().get(key));
				viewerBounds.put(key, viewer.getViewerRectangle());
				viewer.close();
			}
		}
	}
	
	public void bringAllViewersToForeground() {
		for(String key : simulation.getDvHM().keySet()) {
			if(simulation.getDvHM().get(key) instanceof StandaloneViewer) {
				StandaloneViewer viewer = ((StandaloneViewer)simulation.getDvHM().get(key));
				viewer.show();
			}
		}
		
		//give focus to control panel
		frame.setVisible(true);

	}
	
	public void importNewFile() {
		//importing new file so reset the windows
		File newfile = SwingDialogs.getInstance().getFileToRead();
		
		if(newfile == null)
			return;
		
		workingFile = newfile;
		
		clearCurrentSimulation();
		viewerBounds.clear();
		runConfigs.clear();
		
		importFile();
	}
	
	public void importFile() {
		FileInclusionReader fir = new FileInclusionReader(workingFile);
		
		try {
			Sim sim = new Sim(fir.read());
            
	        sim.readModel();
	        sim.build();
	        
	        ControlPanel.getInstance("jLEMS").registerSimulation(sim, null);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), 
					String.format("Unexpected error while opening model file with message : %s", e.getMessage()), 
					"Error", JOptionPane.ERROR_MESSAGE);
			
		}
		
	}
	
}

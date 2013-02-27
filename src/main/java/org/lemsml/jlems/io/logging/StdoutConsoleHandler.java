package org.lemsml.jlems.io.logging;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;

public class StdoutConsoleHandler extends ConsoleHandler {

 
	
	 protected void setOutputStream(OutputStream out) throws SecurityException {
		    super.setOutputStream(System.out); 
	 }

	
}

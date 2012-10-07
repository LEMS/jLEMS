package org.lemsml.jlemsio.logging;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;

public class StdoutConsoleHandler extends ConsoleHandler {

	 public StdoutConsoleHandler() {
		 super();
	 }
	
	
	 protected void setOutputStream(OutputStream out) throws SecurityException {
		    super.setOutputStream(System.out); 
	 }

	
}

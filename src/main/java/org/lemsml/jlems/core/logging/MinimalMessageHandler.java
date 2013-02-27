package org.lemsml.jlems.core.logging;

import java.util.logging.Logger;
/*
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
*/



public class MinimalMessageHandler implements MessageHandler {

 
	private static final Logger errorLogger = Logger.getLogger("errors");
	
	private static final Logger infoLogger = Logger.getLogger("info");
	 
	
	 
	
	
	public void msg(MessageType type, String txt) {

		String fmsg = " (" + type.name() + ") " + txt;
 		
		if (type == MessageType.ERROR || 
			type == MessageType.COREERROR || 
			type == MessageType.FATAL) {
			
			errorLogger.severe(fmsg);
			
		} else if (type == MessageType.WARNING) {
			infoLogger.warning(fmsg);
			
		} else {
			infoLogger.info(fmsg);
		}
		 
	}

	public void msg(final String txt) {
		msg(MessageType.LOG, txt);
	}


}

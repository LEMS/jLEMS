package org.lemsml.jlems.io.logging;
 
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.logging.MessageHandler;
import org.lemsml.jlems.core.logging.MessageType;
 

public final class DefaultLogger implements MessageHandler {

 
	private static final Logger errorLogger = Logger.getLogger("errors");
	
	private static final Logger infoLogger = Logger.getLogger("info");
	
	private static DefaultLogger instance;
	
  
	
	public static void initialize() {
		if (instance == null) {
		infoLogger.setUseParentHandlers(false);
		SimpleFormatter fmt = new OneLineFormatter();
		StreamHandler sh = new StreamHandler(System.out, fmt);
		infoLogger.addHandler(sh);
		
		instance = new DefaultLogger();
		E.setMessageHandler(instance);
		}
	}
	
	
	private DefaultLogger() {
		
	}
	
	
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

package org.lemsml.jlemsio.logging;
 
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
/*
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
*/

import org.lemsml.jlems.util.MessageHandler;
import org.lemsml.jlems.util.MessageType;


public class MessagePrintlnHandler implements MessageHandler {

 
	private static final Logger errorLogger = Logger.getLogger("errors");
	
	private static final Logger infoLogger = Logger.getLogger("info");
	
	{
		infoLogger.setUseParentHandlers(false);
		SimpleFormatter fmt = new OneLineFormatter();
 		 StreamHandler sh = new StreamHandler(System.out, fmt);
		 infoLogger.addHandler(sh);
	 
	}
	
	
	public MessagePrintlnHandler() {
		
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

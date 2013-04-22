package org.lemsml.jlems.core.logging;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/*
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
*/



public class MinimalMessageHandler implements MessageHandler {

 
	private static final Logger errorLogger = Logger.getLogger("errors");
	
	private static final Logger infoLogger = Logger.getLogger("info");
	 
	
	private static boolean veryMinimal = false;
	
	public static void setVeryMinimal(boolean vm) {
		veryMinimal = vm;
	}
	
	
	
	public void msg(MessageType type, String txt) {
		
		if (veryMinimal) {
			System.out.println("(" + type.name() + ") " + txt);
			return;
		}

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
	

    public static void main(String[] args) throws Exception {
    	System.out.println("Testing logger...");
    	MinimalMessageHandler mmh = new MinimalMessageHandler();

    	mmh.msg(MessageType.INFO, "Some info");
    	
    	MinimalMessageHandler.setVeryMinimal(true);

    	mmh.msg(MessageType.INFO, "Some more info");
    	mmh.msg(MessageType.ERROR, "Some error");
    	
    	
    }


}

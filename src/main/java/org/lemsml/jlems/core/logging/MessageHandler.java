package org.lemsml.jlems.core.logging;



public interface MessageHandler {



	void msg(MessageType type, String txt);


	void msg(String txt);


}

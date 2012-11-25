package org.lemsml.jlems.logging;



public interface MessageHandler {



	void msg(MessageType type, String txt);


	void msg(String txt);


}

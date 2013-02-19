package org.lemsml.jlems.viz.plot;



public class MessagePrintlnHandler implements MessageHandler {

	public void msg(MessageType type, String txt) {
		System.out.println(type.name() + " - " + txt);
	}

	public void msg(String txt) {
		System.out.println(txt);
	}


}

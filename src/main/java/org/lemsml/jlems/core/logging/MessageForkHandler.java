package org.lemsml.jlems.core.logging;

import java.util.ArrayList;



public class MessageForkHandler implements MessageHandler {


	private ArrayList<MessageHandler> handlers;


	public MessageForkHandler() {
		handlers = new ArrayList<MessageHandler>();
		handlers.add(new MinimalMessageHandler());
	}


	public void addHandler(MessageHandler mh) {
		handlers.add(mh);
	}



	public void msg(MessageType type, String txt) {
		for (MessageHandler mh : handlers) {
			mh.msg(type, txt);
		}

	}


	public void msg(String txt) {
		for (MessageHandler mh : handlers) {
			mh.msg(txt);
		}
	}


	public void setHandler(MessageHandler mh) {
		handlers = new ArrayList<MessageHandler>();
		handlers.add(mh);
	}

}

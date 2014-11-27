package org.lemsml.jlems.core.sim;

import java.io.Serializable;

public class ParseException extends LEMSException implements Serializable {

    private static final long serialVersionUID = 1L;

    String message;

    public ParseException(String msg) {
        super(msg);
        message = msg;
    }

    public String toString() {
        return "ParseException: " + message;
    }

}

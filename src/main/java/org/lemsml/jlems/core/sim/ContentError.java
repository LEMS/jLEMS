package org.lemsml.jlems.core.sim;

import java.io.Serializable;

public class ContentError extends LEMSException implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public ContentError(String msg) {
        super(msg);
    }

    public ContentError(String msg, Throwable t) {
        super(msg, t);
    }

}

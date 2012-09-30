package org.lemsml.jlems.type;

import org.lemsml.jlems.annotation.Mat;
import org.lemsml.jlems.annotation.Mel;
import org.lemsml.jlems.util.ContentError;

@Mel(info="Holds textual information that does not change the model but is needed for other purposes such as " +
		"labelling graphs.")
public class Text implements Named  {
	@Mat(info="")
	public String name;

    public Text() {
    }

    public Text(String name) {
        this.name = name;
    }

    
	
	public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
	 
		
	}


	public String getName() {
		return name;
	}

 
 
}

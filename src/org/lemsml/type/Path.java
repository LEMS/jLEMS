package org.lemsml.type;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.util.ContentError;

@Mel(info="Duplicates some functionality of PathParameter - the two should be merged.")
public class Path implements Named  {
	@Mat(info="")
	public String name;

    public Path() {
    }

    public Path(String name) {
        this.name = name;
    }

    


	public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
	 	
	}


	public String getName() {
		return name;
	}

	
	
	
	
 
}

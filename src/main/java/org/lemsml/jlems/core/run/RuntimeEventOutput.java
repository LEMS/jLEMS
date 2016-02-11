package org.lemsml.jlems.core.run;

public class RuntimeEventOutput extends RuntimeOutput{

	String format;
	
 
    @Override
	public String toString() {
		return "RuntimeEventOutput, id=" + id + " file=" + fileName + " format=" + format;
	}

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }
	

}

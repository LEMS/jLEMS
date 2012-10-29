package org.lemsml.jlemsviz.plot;



// tag anything that is directly addable to panels as a DComponent (eg then know it is safe to cast to
// a JComponent in the case that we're using swing)

public interface DComponent {
   
   public void setTooltip(String s);

}

package org.lemsml.jlems.viz.plot;



// tag anything that is directly addable to panels as a DComponent (eg then know it is safe to cast to
// a JComponent in the case that we're using swing)

public interface DComponent {
   
   void setTooltip(String s);

}

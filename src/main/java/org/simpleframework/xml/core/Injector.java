package org.simpleframework.xml.core;

/**
 * This can be used to set the values on the object, or if there is
 * a constructor to be used it will set the values to an internal
 * map keyed by the label name. When all values have been set this
 * can be used to created the object using the <code>commit</code>
 * method.
 * 
 * @author Niall Gallagher
 */
public interface Injector {
   
   // Get a previously set label
   public Label get(String name);
   
   public Object get(Label label);
   
   public void set(Label label, Object value);
   
   public void commit();
}
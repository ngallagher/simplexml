package org.simpleframework.xml.core;

interface Entity {
  
   public Class getDependant() throws Exception;
   public String getEntry() throws Exception;
   public String getOverride();
   public boolean isInline();

}

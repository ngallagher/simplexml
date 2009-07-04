package org.simpleframework.xml.core;



interface Parameter {

   public int getIndex();
   public String getName() throws Exception;
   public Class getType();
}

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;



interface Parameter {
   
   public String getName() throws Exception;
   public Annotation getAnnotation();
   public Class getType();
   public int getIndex();
}

package org.simpleframework.xml.core;

import java.util.List;
import java.util.Set;

interface Specification {

   public boolean isDefault(); 
   public Object getDefault() throws Exception; 
   public Parameter getParameter(String name);
   public Builder getBuilder(Set<String> names);
   public List<Builder> getBuilders();
}

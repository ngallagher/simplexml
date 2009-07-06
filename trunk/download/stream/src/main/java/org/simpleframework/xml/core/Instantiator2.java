package org.simpleframework.xml.core;

import java.util.List;
import java.util.Set;

interface Instantiator2 {

   public boolean isDefault(); // represents default constructor
   public Object getDefault() throws Exception; // get default constructor
   public Parameter getParameter(String name);
   public Builder getBuilder(Set<String> names);
   public List<Builder> getBuilders();
}

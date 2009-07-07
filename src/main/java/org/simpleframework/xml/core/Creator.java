package org.simpleframework.xml.core;

import java.util.List;

interface Creator {

   public boolean isDefault(); 
   public Object getInstance() throws Exception; 
   public Object getInstance(Criteria criteria) throws Exception;
   public Parameter getParameter(String name);
   public List<Builder> getBuilders();
}

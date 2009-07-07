package org.simpleframework.xml.core;

import java.util.Iterator;

interface Criteria extends Iterable<String> {
   
   public Pointer get(String name);
   public void remove(String name);
   public Iterator<String> iterator();
   public void set(Label label, Object value) throws Exception;
   public void commit(Object source) throws Exception;

}

package org.simpleframework.xml.stream;

interface EventReader {

   public NodeEvent next() throws Exception;
   public NodeEvent peek() throws Exception;
}

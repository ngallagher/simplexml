package org.simpleframework.xml.stream;

interface NodeEvent extends Iterable<Attribute> {
   public String getName();
   public String getValue();
   public String getReference();
   public String getPrefix();
   public boolean isEnd();
   public boolean isStart();
   public boolean isText();
}

package org.simpleframework.xml.benchmark.simple;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="element")
public class SimpleElementExample {

   @Attribute(name="text")
   private String text;
   
   @Element(name="integer")
   private int integerValue;
   
   @Element(name="long")
   private long longValue;
   
   @Element(name="boolean")
   private boolean booleanValue;
}

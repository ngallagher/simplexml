package org.simpleframework.xml.benchmark.simple;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name="root")
public class SimpleClass {

   @Element(name="example1")
   private SimpleExample example1;
   
   @Element(name="example2")
   private SimpleExample example2;
   
   @Attribute(name="id")
   private int id;
   
}
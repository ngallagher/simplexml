package org.simpleframework.xml.benchmark.simple;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="example")
public class SimpleExample {

   @Attribute(name="version")
   private int version;
   
   @Attribute(name="name")
   private String name;
   
   @Element(name="element1")
   private SimpleElementExample example1;
   
   @Element(name="element2")
   private SimpleElementExample example2;
   
   @Element(name="element3")
   private SimpleElementExample example3;
   
   private SimpleElementExample example4;
   
   @Element(name="text")
   private String text;
   
   @Element(name="element4")
   private SimpleElementExample getElement() {      
      return example4;
   }
   
   @Element(name="element4")
   private void setElement(SimpleElementExample example) {      
      this.example4 = example; 
   }
   
}

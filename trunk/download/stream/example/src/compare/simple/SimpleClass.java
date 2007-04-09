package compare.simple;

import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.Root;


@Root(name="root")
public class SimpleClass {

   @Element(name="example1")
   private SimpleExample example1;
   
   @Element(name="example2")
   private SimpleExample example2;
   
   @Attribute(name="id")
   private int id;
   
}
package compare.simple;

import simple.xml.Attribute;
import simple.xml.Root;

@Root(name="example")
public class SimpleExample {

   @Attribute(name="version")
   public int version;
   
   @Attribute(name="name")
   public String name;
}

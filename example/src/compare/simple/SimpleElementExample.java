package compare.simple;

import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.Root;

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

package org.simpleframework.xml.transform.sql;

import java.sql.Time;

import junit.framework.TestCase;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

public class TimeTransformTest extends TestCase {
   
   @Root
   public class TimeExample {
      
      @Attribute     
      private Time attribute;
      
      @Element
      private Time element;      
      
      public TimeExample() {
         super();
      }
      
      public TimeExample(long time) {
         this.attribute = new Time(time);
         this.element = new Time(time);
      }   
   }
   
   public void testTime() throws Exception {
      long now = System.currentTimeMillis();
      Time date = new Time(now);
      TimeTransform format = new TimeTransform();
      String value = format.write(date);
      Time copy = format.read(value);
      
      assertEquals(date, copy);  
      assertEquals(copy.getTime(), now);
   }
}
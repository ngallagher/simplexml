package org.simpleframework.xml.transform.sql;

import java.sql.Time;
import java.util.Date;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.load.Persister;

public class TimeTransformTest extends ValidationTestCase {
   
   @Root
   public class TimeExample {
      
      @Attribute     
      private Date attribute;
      
      @Element
      private Date element;
      
      @Element
      private Time time;
      
      public TimeExample() {
         super();
      }
      
      public TimeExample(long time) {
         this.attribute = new Time(time);
         this.element = new Time(time);
         this.time = new Time(time);
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
   
   public void testPersistence() throws Exception {
      long now = System.currentTimeMillis();
      Persister persister = new Persister();
      TimeExample example = new TimeExample(now);
      
      validate(example, persister);      
   }
}
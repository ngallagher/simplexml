package org.simpleframework.http.core;

import java.util.List;
import java.util.Map;

import org.simpleframework.util.buffer.Buffer;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root
public class Analyser {
   
   @ElementList(inline=true)
   private List<Expect> expect;
   
   public void analyse(Buffer buffer){
      
   }
   
   @Root
   private static class Expect{
      @Attribute
      private String name;
      @Text
      private String value;
      
      public boolean satisfied(Map<String, String> response) {
         String text = response.get(name);
         
         if(value == null) {
            return false;
         }
         return value.equals(text);
         
      }
   }
}

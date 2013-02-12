package org.simpleframework.xml.core;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ValidationTestCase;

public class NoAnnotationsRequiredTest extends ValidationTestCase {
   
   public static class Thing {
      public String thing = "thing";
   }
   
   public static class Person extends Thing {
      private String name = "Jim";
      private String surname = "Beam";
      private int age = 90;
      private Address address = new Address();
      private List<String> hobbies = new ArrayList<String>();
      public Person() {
         hobbies.add("Soccer");
         hobbies.add("Golf");
         hobbies.add("Tennis");
      }
   }
   
   public static class Address {
      private String street = "10 Some Street";
      private String city = "New York";
      private String country = "US";
   }

   public void testSerialization() throws Exception {
      Persister persister = new Persister();
      Person person = new Person();
      StringWriter writer = new StringWriter();
      
      persister.write(person, writer);
      validate(persister, person);
      
      String text = writer.toString();
      
      assertElementExists(text, "/person");
      assertElementHasValue(text, "/person/thing", "thing");
      assertElementHasValue(text, "/person/name", "Jim");
      assertElementHasValue(text, "/person/surname", "Beam");
      assertElementHasValue(text, "/person/age", "90");
      assertElementExists(text, "/person/address");
      assertElementHasValue(text, "/person/address/street", "10 Some Street");
      assertElementHasValue(text, "/person/address/city", "New York");
      assertElementHasValue(text, "/person/address/country", "US");
      assertElementExists(text, "/person/hobbies");
      assertElementHasValue(text, "/person/hobbies/string[1]", "Soccer");
      assertElementHasValue(text, "/person/hobbies/string[2]", "Golf");
      assertElementHasValue(text, "/person/hobbies/string[3]", "Tennis");
   }

}

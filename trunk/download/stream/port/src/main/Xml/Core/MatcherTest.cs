#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MatcherTest : ValidationTestCase {
      [Root]
      [Namespace(Prefix="foo", Reference="http://www.domain.com/value")]
      private static class Example {
         [Element]
         private Integer value;
         [Attribute]
         private Integer attr;
         public Example() {
            super();
         }
         public Example(Integer value, Integer attr) {
            this.value = value;
            this.attr = attr;
         }
      }
      [Root]
      private static class EmptyStringExample {
         [Element(Required=false)]
         private String emptyValue;
         [Element(Required=false)]
         private String nullValue;
         public EmptyStringExample() {
            super();
         }
         public EmptyStringExample(String emptyValue, String nullValue) {
            this.emptyValue = emptyValue;
            this.nullValue = nullValue;
         }
      }
      private static class ExampleIntegerMatcher : Matcher, Transform<Integer> {
         public Transform Match(Class type) {
            if(type == Integer.class) {
               return this;
            }
            return null;
         }
         public Integer Read(String value) {
            return Integer.valueOf(value);
         }
         public String Write(Integer value) {
            return "12345";
         }
      }
      private static class ExampleStringMatcher : Matcher, Transform<String> {
         public Transform Match(Class type) {
            if(type == String.class) {
               return this;
            }
            return null;
         }
         public String Read(String value) {
            if(value != null) {
               if(value.equals("[[NULL]]")) {
                  return null;
               }
               if(value.equals("[[EMPTY]]")) {
                  return "";
               }
            }
            return value;
         }
         public String Write(String value) {
            if(value == null) {
               return "[[NULL]]";
            }
            if(value.equals("")) {
               return "[[EMPTY]]";
            }
            return value;
         }
      }
      public void TestMatcher() {
         Matcher matcher = new ExampleIntegerMatcher();
         Serializer serializer = new Persister(matcher);
         Example example = new Example(1, 9999);
         serializer.Write(example, System.out);
         validate(serializer, example);
      }
      public void TestStringMatcher() {
         Matcher matcher = new ExampleStringMatcher();
         Serializer serializer = new Persister(matcher);
         EmptyStringExample original = new EmptyStringExample("", null);
         StringWriter writer = new StringWriter();
         serializer.Write(original, writer);
         String text = writer.toString();
         System.out.println(text);
         EmptyStringExample recovered = serializer.Read(EmptyStringExample.class, text);
         assertEquals(recovered.emptyValue, original.emptyValue);
         assertEquals(recovered.nullValue, original.nullValue);
      }
   }
}

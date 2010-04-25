#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class StrategyTest : TestCase {
      private const String ELEMENT_NAME = "example-attribute";
      private const String ELEMENT =
      "<?xml version=\"1.0\"?>\n"+
      "<root key='attribute-example-key' example-attribute='SimpleFramework.Xml.Core.StrategyTest$ExampleExample'>\n"+
      "   <text>attribute-example-text</text>  \n\r"+
      "</root>";
      [Root(Name="root")]
      private static abstract class Example {
         public abstract String Value {
            get;
         }
         //public abstract String GetValue();
      }
      private static class ExampleExample : Example {
         [Attribute(Name="key")]
         public String key;
         [Element(Name="text")]
         public String text;
         public String Value {
            get {
               return text;
            }
         }
         //public String GetValue() {
         //   return text;
         //}
            return key;
         }
      }
      public class ExampleStrategy : Strategy {
         private StrategyTest test;
         public ExampleStrategy(StrategyTest test){
            this.test = test;
         }
         public Value Read(Type field, NodeMap node, Dictionary map) {
            Node value = node.remove(ELEMENT_NAME);
            if(value == null) {
           	 return null;
            }
            String name = value.Value;
            Class type = Class.forName(name);
            return new SimpleType(type);
         }
         public bool Write(Type field, Object value, NodeMap node, Dictionary map) {
            if(field.Type != value.getClass()) {
               node.put(ELEMENT_NAME, value.getClass().getName());
            }
            return false;
         }
      }
      public static class SimpleType : Value{
   	   private Class type;
   	   public SimpleType(Class type) {
   		   this.type = type;
   	   }
   	   public int Length {
   	      get {
      	      return 0;
      	   }
   	   }
   	   //public int GetLength() {
   	   //   return 0;
   	   //}
            try {
      		   Constructor method = type.getDeclaredConstructor();
      		   if(!method.isAccessible()) {
      		      method.setAccessible(true);
      		   }
      		   return method.newInstance();
            }catch(Exception e) {
               throw new RuntimeException(e);
            }
   	   }
         public Object Value {
            set {
            }
         }
         //public void SetValue(Object value) {
         //}
             return false;
          }
   	   public Class Type {
   	      get {
      		  return type;
      	   }
   	   }
   	   //public Class GetType() {
   	   //return type;
   	   //}
      public void TestExampleStrategy() {
         ExampleStrategy strategy = new ExampleStrategy(this);
         Serializer persister = new Persister(strategy);
         Example example = persister.Read(Example.class, ELEMENT);
         assertTrue(example instanceof ExampleExample);
         assertEquals(example.Value, "attribute-example-text");
         assertEquals(example.Key, "attribute-example-key");
         persister.Write(example, System.err);
      }
   }
}

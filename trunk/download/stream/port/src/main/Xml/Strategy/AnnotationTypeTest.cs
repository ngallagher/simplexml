#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class AnnotationTypeTest : ValidationTestCase {
      private const String SOURCE =
      "<?xml version=\"1.0\"?>\n"+
      "<annotationExample age='10'>\n"+
      "   <name key='name'>John Doe</name>\n"+
      "</annotationExample>";
      [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
      private static class Component : System.Attribute {
         public String Name();
      }
      @Root
      private static class AnnotationExample {
         @Component(name="name")
         @Element
         private String name;
         @Component(name="age")
         @Attribute
         private int age;
         public AnnotationExample(@Element(name="name") String name, @Attribute(name="age") int age) {
            this.name = name;
            this.age = age;
         }
      }
      private static class AnnotationStrategy : Strategy {
         private const String KEY = "key";
         private readonly Strategy strategy;
         private AnnotationStrategy() {
            this.strategy = new TreeStrategy();
         }
         public Value Read(Type type, NodeMap<InputNode> node, Dictionary map) {
            Component component = type.getAnnotation(Component.class);
            if(component != null) {
               String name = component.Name();
               InputNode value = node.get(KEY);
               if(!value.getValue().equals(name)) {
                  throw new IllegalStateException("Component name incorrect, expected '"+name+"' but was '"+value.getValue()+"'");
               }
            }
            return strategy.Read(type, node, map);
         }
         public bool Write(Type type, Object value, NodeMap<OutputNode> node, Dictionary map) {
            Component component = type.getAnnotation(Component.class);
            if(component != null) {
               String name = component.Name();
               if(name != null) {
                  node.put(KEY, name);
               }
            }
            return strategy.Write(type, value, node, map);
         }
      }
      public void TestAnnotationType() {
         Strategy strategy = new AnnotationStrategy();
         Persister persister = new Persister(strategy);
         StringWriter writer = new StringWriter();
         AnnotationExample example = persister.Read(AnnotationExample.class, SOURCE);
         persister.Write(example, writer);
         String text = writer.toString();
         assertXpathExists("/annotationExample[@age='10']", text);
         assertXpathExists("/annotationExample/name[@key='name']", text);
         AnnotationExample result = persister.Read(AnnotationExample.class, text);
         assertEquals(example.name, result.name);
         assertEquals(example.age, result.age);
         validate(result, persister);
      }
   }
}

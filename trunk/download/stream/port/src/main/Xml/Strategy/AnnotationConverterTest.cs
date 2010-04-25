#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   public class AnnotationConverterTest : ValidationTestCase {
      [AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]
      private static class Convert: System.Attribute {
         public Class<? : Converter> value();
      }
      private static interface Converter<T> {
         public T Read(InputNode node);
         public void Write(OutputNode node, T value);
      }
      private static class CowConverter : Converter<Cow> {
         public Cow Read(InputNode node) {
            String name = node.getAttribute("name").Value;
            String age = node.getAttribute("age").Value;
            return new Cow(name, Integer.parseInt(age));
         }
         public void Write(OutputNode node, Cow cow) {
            node.setAttribute("name", cow.Name);
            node.setAttribute("age", String.valueOf(cow.Age));
            node.setAttribute("legs", String.valueOf(cow.Legs));
         }
      }
      private static class ChickenConverter : Converter<Chicken> {
         public Chicken Read(InputNode node) {
            String name = node.getAttribute("name").Value;
            String age = node.getAttribute("age").Value;
            return new Chicken(name, Integer.parseInt(age));
         }
         public void Write(OutputNode node, Chicken chicken) {
            node.setAttribute("name", chicken.Name);
            node.setAttribute("age", String.valueOf(chicken.Age));
            node.setAttribute("legs", String.valueOf(chicken.Legs));
         }
      }
      private static class Animal {
         private readonly String name;
         private readonly int age;
         private readonly int legs;
         public Animal(String name, int age, int legs) {
            this.name = name;
            this.legs = legs;
            this.age = age;
         }
         public String Name {
            get {
               return name;
            }
         }
         //public String GetName() {
         //   return name;
         //}
            return age;
         }
         public int Legs {
            get {
               return legs;
            }
         }
         //public int GetLegs() {
         //   return legs;
         //}
      private static class Chicken : Animal {
         public Chicken(String name, int age) {
            super(name, age, 2);
         }
      }
      private static class Cow : Animal {
         public Cow(String name, int age) {
            super(name, age, 4);
         }
      }
      private static class AnnotationStrategy : Strategy {
         private readonly Strategy strategy;
         public AnnotationStrategy(Strategy strategy) {
            this.strategy = strategy;
         }
         public Value Read(Type type, NodeMap<InputNode> node, Dictionary map) {
            Value value = strategy.Read(type, node, map);
            Convert convert = type.getAnnotation(Convert.class);
            InputNode parent = node.getNode();
            if(convert != null) {
               Class<? : Converter> converterClass = convert.value();
               Constructor<? : Converter> converterConstructor = converterClass.getDeclaredConstructor();
               if(!converterConstructor.isAccessible()) {
                  converterConstructor.setAccessible(true);
               }
               Converter converter = converterConstructor.newInstance();
               Object result = converter.Read(parent);
               return new Wrapper(result);
            }
            return value;
         }
         public bool Write(Type type, Object value, NodeMap<OutputNode> node, Dictionary map) {
            Convert convert = type.getAnnotation(Convert.class);
            OutputNode parent = node.getNode();
            if(convert != null) {
               Class<? : Converter> converterClass = convert.value();
               Constructor<? : Converter> converterConstructor = converterClass.getDeclaredConstructor();
               if(!converterConstructor.isAccessible()) {
                  converterConstructor.setAccessible(true);
               }
               Converter converter = converterConstructor.newInstance();
               converter.Write(parent, value);
               return true;
            }
            return strategy.Write(type, value, node, map);
         }
      }
      public static class Wrapper : Value {
         private Object data;
         public Wrapper(Object data){
            this.data = data;
         }
         public int Length {
            get {
               return 0;
            }
         }
         //public int GetLength() {
         //   return 0;
         //}
            return data.getClass();
         }
         public Object Value {
            get {
               return data;
            }
            set {
               this.data = value;
            }
         }
         //public Object GetValue() {
         //   return data;
         //}
            return true;
         }
         //public void SetValue(Object data) {
         //   this.data = data;
         //}
      private const String SOURCE =
      "<farmExample>"+
      "    <chicken name='Hen' age='1' legs='2'/>"+
      "    <cow name='Bull' age='4' legs='4'/>"+
      "</farmExample>";
      [Root]
      private static class FarmExample {
         [Element]
         [Convert(ChickenConverter.class)]
         private Chicken chicken;
         [Element]
         [Convert(CowConverter.class)]
         private Cow cow;
         public FarmExample(@Element(name="chicken") Chicken chicken, @Element(name="cow") Cow cow) {
            this.chicken = chicken;
            this.cow = cow;
         }
         public Chicken Chicken {
            get {
               return chicken;
            }
         }
         //public Chicken GetChicken() {
         //   return chicken;
         //}
            return cow;
         }
      }
      public void TestAnnotationConversion() {
         Strategy strategy = new TreeStrategy();
         Strategy converter = new AnnotationStrategy(strategy);
         Serializer serializer = new Persister(converter);
         StringWriter writer = new StringWriter();
         FarmExample example = serializer.Read(FarmExample.class, SOURCE);
         assertEquals(example.Cow.Name, "Bull");
         assertEquals(example.Cow.Age, 4);
         assertEquals(example.Cow.Legs, 4);
         assertEquals(example.Chicken.Name, "Hen");
         assertEquals(example.Chicken.Age, 1);
         assertEquals(example.Chicken.Legs, 2);
         serializer.Write(example, System.out);
         serializer.Write(example, writer);
         String text = writer.toString();
         assertXpathExists("/farmExample/chicken[@name='Hen']", text);
         assertXpathExists("/farmExample/chicken[@age='1']", text);
         assertXpathExists("/farmExample/chicken[@legs='2']", text);
         assertXpathExists("/farmExample/cow[@name='Bull']", text);
         assertXpathExists("/farmExample/cow[@age='4']", text);
         assertXpathExists("/farmExample/cow[@legs='4']", text);
      }
   }
}

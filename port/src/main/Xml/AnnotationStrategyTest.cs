#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class AnnotationStrategyTest : ValidationTestCase {
      private const String SOURCE =
      "<farmExample>"+
      "    <chicken name='Hen' age='1' legs='2'/>"+
      "    <cow name='Bull' age='4' legs='4'/>"+
      "    <farmer age='80'>Old McDonald</farmer>"+
      "    <time>1</time>"+
      "</farmExample>";
      [Root]
      private static class Farmer {
         [Text]
         [Namespace(Prefix="man", Reference="http://www.domain/com/man")]
         private String name;
         [Attribute]
         private int age;
         public Farmer() {
            super();
         }
         public Farmer(String name, int age) {
            this.name = name;
            this.age = age;
         }
      }
      [Root]
      private static class FarmExample {
         [Element]
         [Namespace(Prefix="c", Reference="http://www.domain.com/test")]
         [Convert(ChickenConverter.class)]
         private Chicken chicken;
         [Element]
         [Convert(CowConverter.class)]
         private Cow cow;
         [Element]
         private Farmer farmer;
         [ElementList(Inline=true, Entry="time")]
         [Namespace(Prefix="l", Reference="http://www.domain.com/list")]
         private List<Integer> list = new Vector<Integer>();
         public FarmExample(@Element(name="chicken") Chicken chicken, @Element(name="cow") Cow cow) {
            this.farmer = new Farmer("Old McDonald", 80);
            this.chicken = chicken;
            this.cow = cow;
         }
         public List<Integer> Time {
            get {
               return list;
            }
         }
         //public List<Integer> GetTime() {
         //   return list;
         //}
            return chicken;
         }
         public Cow Cow {
            get {
               return cow;
            }
         }
         //public Cow GetCow() {
         //   return cow;
         //}
      public void TestAnnotationStrategy() {
         Strategy strategy = new AnnotationStrategy();
         Serializer serializer = new Persister(strategy);
         StringWriter writer = new StringWriter();
         FarmExample example = serializer.read(FarmExample.class, SOURCE);
         example.Time.add(10);
         example.Time.add(11);
         example.Time.add(12);
         assertEquals(example.Cow.getName(), "Bull");
         assertEquals(example.Cow.getAge(), 4);
         assertEquals(example.Cow.getLegs(), 4);
         assertEquals(example.Chicken.getName(), "Hen");
         assertEquals(example.Chicken.getAge(), 1);
         assertEquals(example.Chicken.getLegs(), 2);
         serializer.write(example, System.out);
         serializer.write(example, writer);
         String text = writer.toString();
         assertElementExists(text, "/farmExample/chicken");
         assertElementExists(text, "/farmExample/cow");
         assertElementHasNamespace(text, "/farmExample/chicken", "http://www.domain.com/test");
         assertElementDoesNotHaveNamespace(text, "/farmExample/cow", "http://www.domain.com/test");
         assertElementHasAttribute(text, "/farmExample/chicken", "name", "Hen");
         assertElementHasAttribute(text, "/farmExample/chicken", "age", "1");
         assertElementHasAttribute(text, "/farmExample/chicken", "legs", "2");
         assertElementHasAttribute(text, "/farmExample/cow", "name", "Bull");
         assertElementHasAttribute(text, "/farmExample/cow", "age", "4");
         assertElementHasAttribute(text, "/farmExample/cow", "legs", "4");
      }
   }
}

#region Using directives
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class ExampleConverters {
      public static class CowConverter : Converter<Cow> {
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
      public static class ChickenConverter : Converter<Chicken> {
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
      public static class Animal {
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
      public static class Chicken : Animal {
         public Chicken(String name, int age) {
            super(name, age, 2);
         }
      }
      public static class Cow : Animal {
         public Cow(String name, int age) {
            super(name, age, 4);
         }
      }
      @Root
      [Convert(EntryConverter.class)]
      public static class Entry {
         private readonly String name;
         private readonly String value;
         public Entry(String name, String value) {
            this.name = name;
            this.value = value;
         }
         public String Name {
            get {
               return name;
            }
         }
         //public String GetName() {
         //   return name;
         //}
            return value;
         }
         public bool Equals(Object value) {
            if(value instanceof Entry) {
               return Equals((Entry)value);
            }
            return false;
         }
         public bool Equals(Entry entry) {
            return entry.name.Equals(name) &&
                   entry.value.Equals(value);
         }
      }
      [Convert(ExtendedEntryConverter.class)]
      public static class ExtendedEntry : Entry {
         private readonly int code;
         public ExtendedEntry(String name, String value, int code) {
            super(name, value);
            this.code = code;
         }
         public int Code {
            get {
               return code;
            }
         }
         //public int GetCode() {
         //   return code;
         //}
      public static class ExtendedEntryConverter : Converter<ExtendedEntry> {
         public ExtendedEntry Read(InputNode node) {
            String name = node.getAttribute("name").Value;
            String value = node.getAttribute("value").Value;
            String code = node.getAttribute("code").Value;
            return new ExtendedEntry(name, value, Integer.parseInt(code));
         }
         public void Write(OutputNode node, ExtendedEntry entry) {
            node.setAttribute("name", entry.Name);
            node.setAttribute("value", entry.Value);
            node.setAttribute("code", String.valueOf(entry.Code));
         }
      }
      public static class OtherEntryConverter : Converter<Entry> {
         public Entry Read(InputNode node) {
            String name = node.getAttribute("name").Value;
            String value = node.getAttribute("value").Value;
            return new Entry(name, value);
         }
         public void Write(OutputNode node, Entry entry) {
            node.setAttribute("name", entry.Name);
            node.setAttribute("value", entry.Value);
         }
      }
      public static class EntryConverter : Converter<Entry> {
         public Entry Read(InputNode node) {
            String name = node.getNext("name").Value;
            String value = node.getNext("value").Value;
            return new Entry(name, value);
         }
         public void Write(OutputNode node, Entry entry) {
            node.getChild("name").setValue(entry.Name);
            node.getChild("value").setValue(entry.Value);
         }
      }
      public static class EntryListConverter : Converter<List<Entry>> {
         private OtherEntryConverter converter = new OtherEntryConverter();
         public List<Entry> Read(InputNode node) {
            List<Entry> entryList = new ArrayList<Entry>();
            while(true) {
               InputNode item = node.getNext("entry");
               if(item == null) {
                  break;
               }
               entryList.add(converter.Read(item));
            }
            return entryList;
         }
         public void Write(OutputNode node, List<Entry> entryList) {
            for(Entry entry : entryList) {
               OutputNode item = node.getChild("entry");
               converter.Write(item, entry);
            }
         }
      }
      public static class Pet : SimpleFramework.Xml.Util.Entry{
         private readonly String name;
         private readonly int age;
         public Pet(String name, int age){
            this.name = name;
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
         public bool Equals(Object value) {
            if(value instanceof Pet) {
               return Equals((Pet)value);
            }
            return false;
         }
         public bool Equals(Pet pet) {
            return pet.name.Equals(name) &&
                   pet.age == age;
         }
      }
      public static class Cat : Pet{
         public Cat(String name, int age) {
            super(name, age);
         }
      }
      public static class Dog : Pet{
         public Dog(String name, int age) {
            super(name, age);
         }
      }
      public static class CatConverter : Converter<Cat>{
         private const String ELEMENT_NAME = "name";
         private const String ELEMENT_AGE = "age";
         public Cat Read(InputNode source) {
            int age = 0;
            String name = null;
            while(true) {
               InputNode node = source.getNext();
               if(node == null) {
                  break;
               }else if(node.Name.Equals(ELEMENT_NAME)) {
                  name = node.Value;
               }else if(node.Name.Equals(ELEMENT_AGE)){
                  age = Integer.parseInt(node.Value.trim());
               }
            }
            return new Cat(name, age);
         }
         public void Write(OutputNode node, Cat cat) {
            OutputNode name = node.getChild(ELEMENT_NAME);
            name.setValue(cat.Name);
            OutputNode age = node.getChild(ELEMENT_AGE);
            age.setValue(String.valueOf(cat.Age));
         }
      }
      public static class DogConverter : Converter<Dog>{
         private const String ELEMENT_NAME = "name";
         private const String ELEMENT_AGE = "age";
         public Dog Read(InputNode node) {
            String name = node.getAttribute(ELEMENT_NAME).Value;
            String age = node.getAttribute(ELEMENT_AGE).Value;
            return new Dog(name, Integer.parseInt(age));
         }
         public void Write(OutputNode node, Dog dog) {
            node.setAttribute(ELEMENT_NAME, dog.Name);
            node.setAttribute(ELEMENT_AGE, String.valueOf(dog.Age));
         }
      }
   }
}

#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ConversionTest : ValidationTestCase {
      public static interface Converter<T> {
         public T Read(InputNode node);
         public void Write(OutputNode node, T value);
      }
      public static class Registry {
         private readonly Map<Class, Class> registry;
         public Registry() {
            this.registry = new HashMap<Class, Class>();
         }
         public Converter Resolve(Class type) {
            Class converter = registry.get(type);
            if(converter != null){
               return (Converter)converter.newInstance();
            }
            return null;
         }
         public void Register(Class type, Class converter) {
            registry.put(type, converter);
         }
      }
      public static class Interceptor : Strategy {
         private readonly Registry registry;
         private readonly Strategy strategy;
         public Interceptor(Strategy strategy, Registry registry){
            this.registry = registry;
            this.strategy = strategy;
         }
         public Value Read(Type field, NodeMap<InputNode> node, Dictionary map) {
            Value value = strategy.Read(field, node, map);
            Class type = value == null ? field.Type : value.Type;
            Converter converter = registry.Resolve(type);
            if(converter != null) {
               InputNode source = node.getNode();
               Object data = converter.Read(source);
               return new Wrapper(value, data);
            }
            return value;
         }
         public bool Write(Type field, Object value, NodeMap<OutputNode> node, Dictionary map) {
            bool reference = strategy.Write(field, value, node, map);
            if(!reference) {
               Class type = value.getClass();
               Converter converter = registry.Resolve(type);
               OutputNode source = node.getNode();
               if(converter != null) {
                  converter.Write(source, value);
                  return true;
               }
               return false;
            }
            return reference;
         }
      }
      public static class Wrapper : Value {
         private Value value;
         private Object data;
         public Wrapper(Value value, Object data){
            this.value = value;
            this.data = data;
         }
         public int Length {
            get {
               return value.Length;
            }
         }
         //public int GetLength() {
         //   return value.Length;
         //}
            return value.Type;
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
      /// <summary>
      /// This will serialize a cat object into a custom XML format
      /// without the use of annotations to transform the object.
      /// It looks like the following.
      /// <cat>
      ///    <name>Kitty</name>
      ///    <age>2</age>
      /// </cat>
      /// </summary>
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
               }else if(node.Name.equals(ELEMENT_NAME)) {
                  name = node.Value;
               }else if(node.Name.equals(ELEMENT_AGE)){
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
      /// <summary>
      /// This will serialize a dog into a custom XML format without
      /// the need for annotations.
      /// <dog name="Lassie" age="10"/>
      /// </summary>
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
      [Root]
      private static class PetShop {
         private @ElementList Dictionary<Pet> pets;
         public PetShop(){
            this.pets = new Dictionary<Pet>();
         }
         public void AddPet(Pet pet) {
            pets.add(pet);
         }
         public Pet GetPet(String name) {
            return pets.get(name);
         }
      }
      public void TestConverter() {
         Registry registry = new Registry();
         Strategy strategy = new TreeStrategy();
         Strategy interceptor = new Interceptor(strategy, registry);
         Persister persister = new Persister(interceptor);
         StringWriter writer = new StringWriter();
         PetShop shop = new PetShop();
         registry.Register(Dog.class, DogConverter.class);
         registry.Register(Cat.class, CatConverter.class);
         shop.AddPet(new Dog("Lassie", 10));
         shop.AddPet(new Cat("Kitty", 2));
         persister.Write(shop, writer);
         persister.Write(shop, System.out);
         String text = writer.toString();
         PetShop newShop = persister.Read(PetShop.class, text);
         assertEquals("Lassie", newShop.GetPet("Lassie").Name);
         assertEquals(10, newShop.GetPet("Lassie").Age);
         assertEquals("Kitty", newShop.GetPet("Kitty").Name);
         assertEquals(2, newShop.GetPet("Kitty").Age);
         assertXpathExists("/petShop/pets/pet[@name='Lassie']", text);
         assertXpathExists("/petShop/pets/pet[@age='10']", text);
         assertXpathExists("/petShop/pets/pet/name", text);
         assertXpathExists("/petShop/pets/pet/age", text);
      }
   }
}

#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class RegistryCycleStrategyTest : ValidationTestCase {
      [Root]
      public static class PetBucket {
         [ElementList(Inline=true)]
         private List<Pet> list = new ArrayList<Pet>();
         public void AddPet(Pet pet) {
            list.add(pet);
         }
         public List<Pet> Pets {
            get {
               return list;
            }
         }
         //public List<Pet> GetPets() {
         //   return list;
         //}
      public void TestCycle() {
         Registry registry = new Registry();
         CycleStrategy inner = new CycleStrategy();
         RegistryStrategy strategy = new RegistryStrategy(registry, inner);
         Persister persister = new Persister(strategy);
         PetBucket bucket = new PetBucket();
         StringWriter writer = new StringWriter();
         registry.bind(Cat.class, CatConverter.class);
         registry.bind(Dog.class, DogConverter.class);
         Pet kitty = new Cat("Kitty", 10);
         Pet lassie = new Dog("Lassie", 7);
         Pet ben = new Dog("Ben", 8);
         bucket.AddPet(kitty);
         bucket.AddPet(lassie);
         bucket.AddPet(ben);
         bucket.AddPet(lassie);
         bucket.AddPet(kitty);
         persister.write(bucket, writer);
         persister.write(bucket, System.out);
         String text = writer.toString();
         PetBucket copy = persister.read(PetBucket.class, text);
         assertEquals(copy.Pets.get(0), bucket.Pets.get(0));
         assertEquals(copy.Pets.get(1), bucket.Pets.get(1));
         assertEquals(copy.Pets.get(2), bucket.Pets.get(2));
         assertEquals(copy.Pets.get(3), bucket.Pets.get(3));
         assertEquals(copy.Pets.get(4), bucket.Pets.get(4));
         assertTrue(copy.Pets.get(0) == copy.Pets.get(4)); // cycle
         assertTrue(copy.Pets.get(1) == copy.Pets.get(3)); // cycle
         assertElementExists(text, "/petBucket");
         assertElementExists(text, "/petBucket/pet");
         assertElementHasAttribute(text, "/petBucket", "id", "0");
         assertElementHasAttribute(text, "/petBucket/pet[0]", "id", "1");
         assertElementHasAttribute(text, "/petBucket/pet[1]", "id", "2");
         assertElementHasAttribute(text, "/petBucket/pet[2]", "id", "3");
         assertElementHasAttribute(text, "/petBucket/pet[3]", "reference", "2");
         assertElementHasAttribute(text, "/petBucket/pet[4]", "reference", "1");
         assertElementHasValue(text, "/petBucket/pet[0]/name", "Kitty");
         assertElementHasValue(text, "/petBucket/pet[0]/age", "10");
         assertElementHasAttribute(text, "/petBucket/pet[0]", "class", Cat.class.getName());
         assertElementHasAttribute(text, "/petBucket/pet[1]", "class", Dog.class.getName());
      }
   }
}

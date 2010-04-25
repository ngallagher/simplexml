#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class RegistryStrategyTest : ValidationTestCase {
      @Root
      @Namespace(prefix="a", reference="http://domain/a")
      private static class PetShop {
         @ElementList
         @Namespace(prefix="b", reference="http://domain/b")
         private Dictionary<Pet> pets;
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
         Strategy interceptor = new RegistryStrategy(registry);
         Persister persister = new Persister(interceptor);
         StringWriter writer = new StringWriter();
         PetShop shop = new PetShop();
         registry.bind(Dog.class, DogConverter.class)
                 .bind(Cat.class, CatConverter.class);
         shop.AddPet(new Dog("Lassie", 10));
         shop.AddPet(new Cat("Kitty", 2));
         persister.write(shop, writer);
         persister.write(shop, System.out);
         String text = writer.toString();
         PetShop newShop = persister.read(PetShop.class, text);
         assertEquals("Lassie", newShop.GetPet("Lassie").getName());
         assertEquals(10, newShop.GetPet("Lassie").getAge());
         assertEquals("Kitty", newShop.GetPet("Kitty").getName());
         assertEquals(2, newShop.GetPet("Kitty").getAge());
         assertElementExists(text, "/petShop");
         assertElementExists(text, "/petShop/pets");
         assertElementExists(text, "/petShop/pets/pet[0]");
         assertElementExists(text, "/petShop/pets/pet[1]");
         assertElementDoesNotExist(text, "/petShop/pets/pet[2]");
         assertElementHasNamespace(text, "/petShop", "http://domain/a");
         assertElementHasNamespace(text, "/petShop/pets", "http://domain/b");
         assertElementHasNamespace(text, "/petShop/pets/pet[0]", null);
         assertElementHasAttribute(text, "/petShop/pets/pet[0]", "name", "Lassie");
         assertElementHasAttribute(text, "/petShop/pets/pet[0]", "age", "10");
         assertElementHasValue(text, "/petShop/pets/pet[1]/name", "Kitty");
         assertElementHasValue(text, "/petShop/pets/pet[1]/age", "2");
      }
   }
}

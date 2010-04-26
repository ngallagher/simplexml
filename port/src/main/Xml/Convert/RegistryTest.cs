#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   import static SimpleFramework.Xml.Util.ExampleConverters.*;
   public class RegistryTest : TestCase {
      public void TestRegistry() {
         Registry registry = new Registry();
         registry.bind(Cat.class, CatConverter.class);
         registry.bind(Dog.class, DogConverter.class);
         AssertEquals(registry.lookup(Cat.class).getClass(), CatConverter.class);
         AssertEquals(registry.lookup(Dog.class).getClass(), DogConverter.class);
         Converter cat = registry.lookup(Cat.class);
         Converter dog = registry.lookup(Dog.class);
         assertTrue(cat == registry.lookup(Cat.class));
         assertTrue(dog == registry.lookup(Dog.class));
      }
   }
}

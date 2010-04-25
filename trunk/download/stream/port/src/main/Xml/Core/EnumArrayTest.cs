#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class EnumArrayTest : ValidationTestCase {
      private const String SOURCE =
      "<example size='3'>"+
      "  <array>ONE,TWO,FOUR</array>"+
      "</example>";
      private static enum Number {
         ONE,
         TWO,
         THREE,
         FOUR
      }
      @Root(name="example")
      private static class NumberArray {
         @Element(name="array")
         private readonly Number[] array;
         private readonly int size;
         public NumberArray(@Element(name="array") Number[] array, @Attribute(name="size") int size) {
            this.array = array;
            this.size = size;
         }
         @Attribute(name="size")
         public int Length {
            get {
               return size;
            }
         }
         //public int GetLength() {
         //   return size;
         //}
      public void TestArrayElement() {
         Persister persister = new Persister();
         NumberArray array = persister.read(NumberArray.class, SOURCE);
         assertEquals(array.array.length, 3);
         assertEquals(array.array[0], Number.ONE);
         assertEquals(array.array[1], Number.TWO);
         assertEquals(array.array[2], Number.FOUR);
         assertEquals(array.Length, array.size);
         assertEquals(array.array.length, array.size);
         validate(persister, array);
      }
   }
}

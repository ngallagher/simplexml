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
      [Root(Name="example")]
      private static class NumberArray {
         [Element(Name="array")]
         private readonly Number[] array;
         private readonly int size;
         public NumberArray(@Element(name="array") Number[] array, @Attribute(name="size") int size) {
            this.array = array;
            this.size = size;
         }
         [Attribute(Name="size")]
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
         AssertEquals(array.array.length, 3);
         AssertEquals(array.array[0], Number.ONE);
         AssertEquals(array.array[1], Number.TWO);
         AssertEquals(array.array[2], Number.FOUR);
         AssertEquals(array.Length, array.size);
         AssertEquals(array.array.length, array.size);
         validate(persister, array);
      }
   }
}

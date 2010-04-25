#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class EnumMapTest : ValidationTestCase {
      private static enum Number {
         ONE,
         TWO,
         THREE,
         FOUR
      }
      @Root
      private static class EnumMapExample {
         @ElementMap
         private EnumMap<Number, String> numbers = new EnumMap<Number, String>(Number.class);
         private EnumMapExample() {
            super();
         }
         public EnumMapExample(EnumMap<Number, String> numbers) {
            this.numbers = numbers;
         }
         public String Get(Number number) {
            return numbers.Get(number);
         }
      }
      public void TestEnumMap() {
         EnumMap<Number, String> numbers = new EnumMap<Number, String>(Number.class);
         numbers.put(Number.ONE, "1");
         numbers.put(Number.TWO, "2");
         numbers.put(Number.THREE, "3");
         EnumMapExample example = new EnumMapExample(numbers);
         Persister persister = new Persister();
         StringWriter out = new StringWriter();
         persister.write(example, System.out);
         persister.write(example, out);
         EnumMapExample other = persister.read(EnumMapExample.class, out.toString());
         assertEquals(other.Get(Number.ONE), "1");
         assertEquals(other.Get(Number.TWO), "2");
         assertEquals(other.Get(Number.THREE), "3");
         assertEquals(other.Get(Number.FOUR), null);
         persister.write(example, System.out);
         validate(persister, example);
      }
   }
}

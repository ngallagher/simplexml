#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class EnumSetTest : ValidationTestCase {
      private static enum Qualification {
         BEGINNER,
         EXPERIENCED,
         EXPERT,
         GURU
      }
      @Root
      private static class EnumSetExample {
         @ElementList
         private EnumSet<Qualification> set = EnumSet.noneOf(Qualification.class);
         public EnumSetExample() {
            super();
         }
         public void Add(Qualification qualification) {
            set.Add(qualification);
         }
         public bool Contains(Qualification qualification) {
            return set.Contains(qualification);
         }
      }
      public void TestEnumSet() {
         Persister persister = new Persister();
         EnumSetExample example = new EnumSetExample();
         example.Add(Qualification.BEGINNER);
         example.Add(Qualification.EXPERT);
         assertTrue(example.Contains(Qualification.BEGINNER));
         assertTrue(example.Contains(Qualification.EXPERT));
         assertFalse(example.Contains(Qualification.GURU));
         persister.write(example, System.out);
         validate(persister, example);
      }
   }
}

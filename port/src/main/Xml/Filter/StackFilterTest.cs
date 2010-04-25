#region Using directives
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Filter {
   public class StackFilterTest : TestCase {
      public class ExampleFilter : Filter {
         private List<String> list;
         private String name;
         public ExampleFilter(List<String> list, String name) {
            this.list = list;
            this.name = name;
         }
         public String Replace(String token) {
            if(token == name) {
               list.Add(name);
               return name;
            }
            return null;
         }
      }
      public void TestFilter() {
         List<String> list = new List<String>();
         StackFilter filter = new StackFilter();
         filter.Push(new ExampleFilter(list, "one"));
         filter.Push(new ExampleFilter(list, "two"));
         filter.Push(new ExampleFilter(list, "three"));
         String one = filter.Replace("one");
         String two = filter.Replace("two");
         String three = filter.Replace("three");
         AssertEquals(one, "one");
         AssertEquals(two, "two");
         AssertEquals(three, "three");
         AssertEquals(list.Count, 3);
         AssertEquals(list[0], "one");
         AssertEquals(list[1], "two");
         AssertEquals(list[2], "three");
      }
   }
}

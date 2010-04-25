#region Using directives
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Filter {
   public class StackFilterTest : TestCase {
      public static class ExampleFilter : Filter {
         private List<String> list;
         private String name;
         public ExampleFilter(List<String> list, String name) {
            this.list = list;
            this.name = name;
         }
         public String Replace(String token) {
            if(token == name) {
               list.add(name);
               return name;
            }
            return null;
         }
      }
      public void TestFilter() {
         List<String> list = new ArrayList<String>();
         StackFilter filter = new StackFilter();
         filter.push(new ExampleFilter(list, "one"));
         filter.push(new ExampleFilter(list, "two"));
         filter.push(new ExampleFilter(list, "three"));
         String one = filter.Replace("one");
         String two = filter.Replace("two");
         String three = filter.Replace("three");
         assertEquals(one, "one");
         assertEquals(two, "two");
         assertEquals(three, "three");
         assertEquals(list.size(), 3);
         assertEquals(list.get(0), "one");
         assertEquals(list.get(1), "two");
         assertEquals(list.get(2), "three");
      }
   }
}

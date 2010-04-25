#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class HierarchyTest : ValidationTestCase {
      public static class Basic {
         [Element]
         private String a;
         [Element]
         private String b;
         private long one;
         private Basic() {
            super();
         }
         public Basic(long one, String a, String b) {
            this.one = one;
            this.a = a;
            this.b = b;
         }
         [Element]
         public long One {
            get {
               return one;
            }
            set {
               this.one = value;
            }
         }
         //public long GetOne() {
         //   return one;
         //}
         //public void SetOne(long one) {
         //   this.one = one;
         //}
      public static class Abstract : Basic {
         [Element]
         private int c;
         private Abstract() {
            super();
         }
         public Abstract(long one, String a, String b, int c) {
            super(one, a, b);
            this.c = c;
         }
      }
      public static class Specialized : Abstract {
         [Element]
         private int d;
         private double two;
         private Specialized() {
            super();
         }
         public Specialized(long one, double two, String a, String b, int c, int d) {
            super(one, a, b, c);
            this.two = two;
            this.d = d;
         }
         [Element]
         public double Two {
            get {
               return two;
            }
            set {
               this.two = value;
            }
         }
         //public double GetTwo() {
         //   return two;
         //}
         //public void SetTwo(double two) {
         //   this.two = two;
         //}
      public void TestHierarchy() {
         Serializer serializer = new Persister();
         Specialized special = new Specialized(1L, 2.0, "a", "b", 1, 2);
         validate(special, serializer);
      }
   }
}

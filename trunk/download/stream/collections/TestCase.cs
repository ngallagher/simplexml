using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Reflection;
using System.Diagnostics;

namespace SimpleFramework.Xml {
   public class TestCase {
      public void AssertFalse(bool value) {
         if(value) {
            throw new SystemException("Value was true");
         }
      }
      public void AssertTrue(bool value) {
         if(!value) {
            throw new SystemException("Value was false");
         }
      }
      public void AssertEquals(Object a, Object b) {
         if (a != b) {
            if ((a == null && b != null) || (a != null && b == null)) {
               throw new SystemException("Values are not equal as on is null");
            }
            if (a.GetType() != b.GetType()) {
               throw new SystemException("Comparing objects of different types");
            }
            if (!a.Equals(b)) {
               throw new SystemException("Values are not equal");
            }
         }
      }
      public void AssertNotNull(Object o) {
         if (o == null) {
            throw new SystemException("Value is null");
         }
      }
      public void AssertNull(Object o) {
         if (o != null) {
            throw new SystemException("Value is not null");
         }
      }
      public virtual void SetUp() { }
      public virtual void TearDown() { }
      protected internal void Run() {
         Type type = GetType();
         foreach (MemberInfo info in type.GetMethods()) {
            if (info.Name.StartsWith("Test") || info.Name.StartsWith("test")) {
               SetUp();
               try {
                  type.InvokeMember(info.Name,
                     BindingFlags.Public |
                     BindingFlags.DeclaredOnly |
                     BindingFlags.Instance |
                     BindingFlags.InvokeMethod,
                     null,
                     this,
                     null);
               } finally {
                  TearDown();
               }
            }
         }
      }
   }
}

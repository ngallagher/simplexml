#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ErasureReflectorTest : ValidationTestCase {
      private static class MapErasure<T> {
         public Field GetField(String name) {
            return MapErasure.class.GetField(name);
         }
         public Method GetMethod(String name) {
            if(name.startsWith("set")) {
               return MapErasure.class.GetMethod(name, Map.class);
            }
            return MapErasure.class.GetMethod(name);
         }
         public Constructor Constructor {
            get {
               return MapErasure.class.getDeclaredConstructors()[0];
            }
         }
         //public Constructor GetConstructor() {
         //   return MapErasure.class.getDeclaredConstructors()[0];
         //}
         private Map<T, String> erasedToString;
         private Map<String, T> stringToErased;
         private Map<String, String> stringToString;
         public MapErasure(
               Map<T, T> erasedToErased,
               Map<T, String> erasedToString,
               Map<String, T> stringToErased,
               Map<String, String> stringToString) {
         }
         public Map<T, T> getErasedToErased() {
            return erasedToErased;
         }
         public Map<T, String> getErasedToString() {
            return erasedToString;
         }
         public Map<String, T> getStringToErased() {
            return stringToErased;
         }
         public Map<String, String> getStringToString() {
            return stringToString;
         }
         public Map<T, T> ErasedToErased {
            set {
               this.erasedToErased = value;
            }
         }
         //public void SetErasedToErased(Map<T, T> erasedToErased) {
         //   this.erasedToErased = erasedToErased;
         //}
            this.erasedToString = erasedToString;
         }
         public Map<String, T> StringToErased {
            set {
               this.stringToErased = value;
            }
         }
         //public void SetStringToErased(Map<String, T> stringToErased) {
         //   this.stringToErased = stringToErased;
         //}
            this.stringToString = stringToString;
         }
      }
      private static class CollectionErasure<T> {
         public Field GetField(String name) {
            return CollectionErasure.class.GetField(name);
         }
         public Method GetMethod(String name) {
            if(name.startsWith("set")) {
               return CollectionErasure.class.GetMethod(name, Collection.class);
            }
            return CollectionErasure.class.GetMethod(name);
         }
         public Constructor Constructor {
            get {
               return CollectionErasure.class.getDeclaredConstructors()[0];
            }
         }
         //public Constructor GetConstructor() {
         //   return CollectionErasure.class.getDeclaredConstructors()[0];
         //}
         private Collection<String> string;
         public CollectionErasure(
               Collection<T> erased,
               Collection<String> string) {
         }
         public Collection<T> Erased {
            get {
               return erased;
            }
            set {
               this.erased = value;
            }
         }
         //public Collection<T> GetErased() {
         //   return erased;
         //}
            return string;
         }
         //public void SetErased(Collection<T> erased) {
         //   this.erased = erased;
         //}
            this.string = string;
         }
      }
      public void TesFieldReflection() {
         assertEquals(Object.class, Reflector.getDependent(CollectionErasure.GetField("erased")));
         assertEquals(String.class, Reflector.getDependent(CollectionErasure.GetField("string")));
         assertEquals(Object.class, Reflector.getDependent(MapErasure.GetField("erasedToErased")));
         assertEquals(Object.class, Reflector.getDependent(MapErasure.GetField("erasedToString")));
         assertEquals(String.class, Reflector.getDependent(MapErasure.GetField("stringToErased")));
         assertEquals(String.class, Reflector.getDependent(MapErasure.GetField("stringToString")));
         assertEquals(Object.class, Reflector.getDependents(MapErasure.GetField("erasedToErased"))[0]);
         assertEquals(Object.class, Reflector.getDependents(MapErasure.GetField("erasedToString"))[0]);
         assertEquals(String.class, Reflector.getDependents(MapErasure.GetField("stringToErased"))[0]);
         assertEquals(String.class, Reflector.getDependents(MapErasure.GetField("stringToString"))[0]);
         assertEquals(Object.class, Reflector.getDependents(MapErasure.GetField("erasedToErased"))[1]);
         assertEquals(String.class, Reflector.getDependents(MapErasure.GetField("erasedToString"))[1]);
         assertEquals(Object.class, Reflector.getDependents(MapErasure.GetField("stringToErased"))[1]);
         assertEquals(String.class, Reflector.getDependents(MapErasure.GetField("stringToString"))[1]);
      }
      public void TestMethodReflection() {
         assertEquals(Object.class, Reflector.getReturnDependent(CollectionErasure.GetMethod("getErased")));
         assertEquals(String.class, Reflector.getReturnDependent(CollectionErasure.GetMethod("getString")));
         assertEquals(null, Reflector.getReturnDependent(CollectionErasure.GetMethod("setErased")));
         assertEquals(null, Reflector.getReturnDependent(CollectionErasure.GetMethod("setString")));
         assertEquals(Object.class, Reflector.getParameterDependent(CollectionErasure.Constructor, 0)); // Collection<T>
         assertEquals(String.class, Reflector.getParameterDependent(CollectionErasure.Constructor, 1)); // Collection<String>
         assertEquals(Object.class, Reflector.getParameterDependents(CollectionErasure.Constructor, 0)[0]); // Collection<T>
         assertEquals(String.class, Reflector.getParameterDependents(CollectionErasure.Constructor, 1)[0]); // Collection<String>
         assertEquals(Object.class, Reflector.getReturnDependent(MapErasure.GetMethod("getErasedToErased")));
         assertEquals(Object.class, Reflector.getReturnDependent(MapErasure.GetMethod("getErasedToString")));
         assertEquals(String.class, Reflector.getReturnDependent(MapErasure.GetMethod("getStringToErased")));
         assertEquals(String.class, Reflector.getReturnDependent(MapErasure.GetMethod("getStringToString")));
         assertEquals(null, Reflector.getReturnDependent(MapErasure.GetMethod("setErasedToErased")));
         assertEquals(null, Reflector.getReturnDependent(MapErasure.GetMethod("setErasedToString")));
         assertEquals(null, Reflector.getReturnDependent(MapErasure.GetMethod("setStringToErased")));
         assertEquals(null, Reflector.getReturnDependent(MapErasure.GetMethod("setStringToString")));
         assertEquals(Object.class, Reflector.getReturnDependents(MapErasure.GetMethod("getErasedToErased"))[0]);
         assertEquals(Object.class, Reflector.getReturnDependents(MapErasure.GetMethod("getErasedToString"))[0]);
         assertEquals(String.class, Reflector.getReturnDependents(MapErasure.GetMethod("getStringToErased"))[0]);
         assertEquals(String.class, Reflector.getReturnDependents(MapErasure.GetMethod("getStringToString"))[0]);
         assertEquals(Object.class, Reflector.getReturnDependents(MapErasure.GetMethod("getErasedToErased"))[1]);
         assertEquals(String.class, Reflector.getReturnDependents(MapErasure.GetMethod("getErasedToString"))[1]);
         assertEquals(Object.class, Reflector.getReturnDependents(MapErasure.GetMethod("getStringToErased"))[1]);
         assertEquals(String.class, Reflector.getReturnDependents(MapErasure.GetMethod("getStringToString"))[1]);
         assertEquals(Object.class, Reflector.getParameterDependent(MapErasure.Constructor, 0)); // Map<T, T>
         assertEquals(Object.class, Reflector.getParameterDependent(MapErasure.Constructor, 1)); // Map<T, String>
         assertEquals(String.class, Reflector.getParameterDependent(MapErasure.Constructor, 2)); // Map<String, T>
         assertEquals(String.class, Reflector.getParameterDependent(MapErasure.Constructor, 3)); // Map<String, String>
         assertEquals(Object.class, Reflector.getParameterDependents(MapErasure.Constructor, 0)[0]); // Map<T, T>
         assertEquals(Object.class, Reflector.getParameterDependents(MapErasure.Constructor, 1)[0]); // Map<T, String>
         assertEquals(String.class, Reflector.getParameterDependents(MapErasure.Constructor, 2)[0]); // Map<String, T>
         assertEquals(String.class, Reflector.getParameterDependents(MapErasure.Constructor, 3)[0]); // Map<String, String>
         assertEquals(Object.class, Reflector.getParameterDependents(MapErasure.Constructor, 0)[1]); // Map<T, T>
         assertEquals(String.class, Reflector.getParameterDependents(MapErasure.Constructor, 1)[1]); // Map<T, String>
         assertEquals(Object.class, Reflector.getParameterDependents(MapErasure.Constructor, 2)[1]); // Map<String, T>
         assertEquals(String.class, Reflector.getParameterDependents(MapErasure.Constructor, 3)[1]); // Map<String, String>
      }
   }
}

#region Using directives
using SimpleFramework.Xml.Core;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class ReflectorTest : TestCase {
      public Collection<String> genericList;
      public Collection normalList;
      public Map<Float, Double> genericMap;
      public Dictionary normalMap;
      public void GenericMethodMapParameter(Map<String, Integer> map) {
      public void NormalMethodMapParameter(Dictionary map) {
      public void GenericMethodCollectionParameter(Collection<String> list) {
      public void NormalMethodCollectionParameter(Collection list) {
      public Map<String, Boolean> genericMethodMapReturn() {return null;}
      public Dictionary NormalMethodMapReturn() {
      public Collection<Float> GenericMethodCollectionReturn() {
      public Collection NormalMethodCollectionReturn() {
      public void TestFieldReflector() {
         Field field = GetField(ReflectorTest.class, "genericMap");
         Class[] types = Reflector.getDependents(field);
         assertEquals(types.length, 2);
         assertEquals(types[0], Float.class);
         assertEquals(types[1], Double.class);
         field = GetField(ReflectorTest.class, "normalMap");
         types = Reflector.getDependents(field);
         assertEquals(types.length, 0);
         field = GetField(ReflectorTest.class, "genericList");
         types = Reflector.getDependents(field);
         assertEquals(types.length, 1);
         assertEquals(types[0], String.class);
         field = GetField(ReflectorTest.class, "normalList");
         types = Reflector.getDependents(field);
         assertEquals(types.length, 0);
      }
      public void TestCollectionReflector() {
         Method method = GetMethod(ReflectorTest.class, "genericMethodCollectionParameter", Collection.class);
         Class[] types = Reflector.getParameterDependents(method, 0);
         assertEquals(types.length, 1);
         assertEquals(types[0], String.class);
         method = GetMethod(ReflectorTest.class, "normalMethodCollectionParameter", Collection.class);
         types = Reflector.getParameterDependents(method, 0);
         assertEquals(types.length, 0);
         method = GetMethod(ReflectorTest.class, "genericMethodCollectionReturn");
         types = Reflector.getReturnDependents(method);
         assertEquals(types.length, 1);
         assertEquals(types[0], Float.class);
         method = GetMethod(ReflectorTest.class, "normalMethodCollectionReturn");
         types = Reflector.getReturnDependents(method);
         assertEquals(types.length, 0);
      }
      public void TestMapReflector() {
         Method method = GetMethod(ReflectorTest.class, "genericMethodMapParameter", Map.class);
         Class[] types = Reflector.getParameterDependents(method, 0);
         assertEquals(types.length, 2);
         assertEquals(types[0], String.class);
         assertEquals(types[1], Integer.class);
         method = GetMethod(ReflectorTest.class, "normalMethodMapParameter", Map.class);
         types = Reflector.getParameterDependents(method, 0);
         assertEquals(types.length, 0);
         method = GetMethod(ReflectorTest.class, "genericMethodMapReturn");
         types = Reflector.getReturnDependents(method);
         assertEquals(types.length, 2);
         assertEquals(types[0], String.class);
         assertEquals(types[1], Boolean.class);
         method = GetMethod(ReflectorTest.class, "normalMethodMapReturn");
         types = Reflector.getReturnDependents(method);
         assertEquals(types.length, 0);
      }
      public Method GetMethod(Class type, String name, Class... types) {
         return type.getDeclaredMethod(name, types);
      }
      public Field GetField(Class type, String name) {
         return type.getDeclaredField(name);
      }
      public void TestCase() {
         assertEquals("URL", Reflector.getName("URL"));
         assertEquals("getEntry", Reflector.getName("getEntry"));
         assertEquals("iF", Reflector.getName("iF"));
         assertEquals("if", Reflector.getName("if"));
         assertEquals("URLConnection", Reflector.getName("URLConnection"));
         assertEquals("type", Reflector.getName("Type"));
      }
   }
}

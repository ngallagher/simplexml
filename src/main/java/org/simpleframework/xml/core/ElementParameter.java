package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.simpleframework.xml.Element;

class ElementParameter implements Parameter {
   
   private final Constructor factory;
   private final Contact contact;
   private final Label label;
   private final int index;
   
   public ElementParameter(Constructor factory, Element label, int index) {
      this.contact = new LabelContact(label, factory, index);
      this.label = new ElementLabel(contact, label);
      this.factory = factory;
      this.index = index;
   }

   public Class getType() {
      return factory.getParameterTypes()[index];
   }
   
   public int getIndex() {
      return index;
   }

   public String getName() throws Exception{
      return label.getName();
   }
   
   public Annotation getAnnotation() {
      return contact.getAnnotation();
   }
   
   private static class LabelContact extends ParameterContact<Element>  {
      
      public LabelContact(Element label, Constructor factory, int index) {
         super(label, factory, index);
      }
      
      public String getName() {
         return label.name();
      }
   }
}

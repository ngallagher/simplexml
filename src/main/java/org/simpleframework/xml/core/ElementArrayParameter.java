package org.simpleframework.xml.core;

import java.lang.reflect.Constructor;

import org.simpleframework.xml.ElementArray;

class ElementArrayParameter implements Parameter {
   private Constructor factory;
   private Contact contact;
   private Label label;
   private int index;
   
   public ElementArrayParameter(Constructor factory, ElementArray label, int index) {
      this.contact = new ParameterContact(factory, label, label.name(), index);
      this.label = new ElementArrayLabel(contact, label);
      this.factory = factory;
      this.index = index;
   }

   public Class getType() {
      return factory.getDeclaringClass();
   }
   
   public int getIndex() {
      return index;
   }

   public String getName() throws Exception{
      return label.getName();
   }
}

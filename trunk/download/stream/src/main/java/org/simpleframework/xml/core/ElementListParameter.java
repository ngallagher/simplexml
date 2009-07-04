package org.simpleframework.xml.core;

import java.lang.reflect.Constructor;

import org.simpleframework.xml.ElementList;

class ElementListParameter implements Parameter {

   private Constructor factory;
   private Contact contact;
   private Label label;
   private int index;
   
   public ElementListParameter(Constructor factory, ElementList label, int index) {
      this.contact = new ParameterContact(factory, label, label.name(), index);
      this.label = new ElementListLabel(contact, label);
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

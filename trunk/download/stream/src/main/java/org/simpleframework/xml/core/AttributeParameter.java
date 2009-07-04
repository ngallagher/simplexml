package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.simpleframework.xml.Attribute;

class AttributeParameter implements Parameter {
   private Constructor factory;
   private Contact contact;
   private Label label;
   private int index;
   
   public AttributeParameter(Constructor factory, Attribute label, int index) {
      this.contact = new ParameterContact(factory, label, label.name(), index);
      this.label = new AttributeLabel(contact, label);
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
}

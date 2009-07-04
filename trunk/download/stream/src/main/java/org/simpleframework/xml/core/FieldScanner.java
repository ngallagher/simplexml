/*
 * FieldScanner.java April 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General 
 * Public License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
 * Boston, MA  02111-1307  USA
 */

package org.simpleframework.xml.core;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Version;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * The <code>FieldScanner</code> object is used to scan an class for
 * fields marked with an XML annotation. All fields that contain an
 * XML annotation are added as <code>Contact</code> objects to the
 * list of contacts for the class. This scans the object by checking
 * the class hierarchy, this allows a subclass to override a super
 * class annotated field, although this should be used rarely.
 * 
 * @author Niall Gallagher
 */
class FieldScanner extends ContactList {
   
   /**
    * This is used to acquire the hierarchy for the class scanned.
    */
   private final Hierarchy hierarchy;
   
   /**
    * This is the primary scanner used to acquire the schema.
    */
   private final ParameterMap table;
   
   /**
    * This is the class that is being scanned for annotations.
    */
   private final Class type;
   
   /**
    * Constructor for the <code>FieldScanner</code> object. This is
    * used to perform a scan on the specified class in order to find
    * all fields that are labeled with an XML annotation.
    * 
    * @param scanner this is the primary scanner used for the class
    * @param type this is the schema class that is to be scanned
    * 
    * @throws Exception thrown if the object schema is invalid
    */
   public FieldScanner(ParameterMap table, Class type) throws Exception {
      this.hierarchy = new Hierarchy(type);
      this.table = table;
      this.type = type;
      this.scan(type);
   }
   
   /**
    * This method is used to scan the class hierarchy for each class
    * in order to extract fields that contain XML annotations. If
    * the field is annotated it is converted to a contact so that
    * it can be used during serialization and deserialization.
    * 
    * @param type this is the type to be scanned for fields
    * 
    * @throws Exception thrown if the object schema is invalid
    */
   private void scan(Class type) throws Exception {
      for(Class next : hierarchy) {
         scan(type, next);
      }  
      validate();
   }
   
   /**
    * This is used to scan the declared fields within the specified
    * class. Each method will be check to determine if it contains
    * an XML element and can be used as a <code>Contact</code> for
    * an entity within the object.
    * 
    * @param real this is the actual type of the object scanned
    * @param type this is one of the super classes for the object
    */  
   private void scan(Class real, Class type) {
      Field[] field = type.getDeclaredFields();
      
      for(int i = 0; i < field.length; i++) {                       
         scan(field[i]);                      
      }   
   }
   
   /**
    * This is used to scan all annotations within the given field.
    * Each annotation is checked against the set of supported XML
    * annotations. If the annotation is one of the XML annotations
    * then the field is considered for acceptance as a contact.
    * 
    * @param field the field to be scanned for XML annotations
    */
   public void scan(Field field) {
      Annotation[] list = field.getDeclaredAnnotations();
      
      for(int i = 0; i < list.length; i++) {
         scan(field, list[i]);                       
      }  
   }
   
   /**
    * This reflectively checks the annotation to determine the type 
    * of annotation it represents. If it represents an XML schema
    * annotation it is used to create a <code>Contact</code> which 
    * can be used to represent the field within the source object.
    * 
    * @param field the field that the annotation comes from
    * @param label the annotation used to model the XML schema
    */
   public void scan(Field field, Annotation label) {
      if(label instanceof Attribute) {
         process(field, label);
      }
      if(label instanceof ElementList) {
         process(field, label);
      }     
      if(label instanceof ElementArray) {
         process(field, label);
      }
      if(label instanceof ElementMap) {
         process(field, label);
      }
      if(label instanceof Element) {
         process(field, label);
      }             
      if(label instanceof Version) {
         process(field, label);
      }
      if(label instanceof Text) {
         process(field, label);
      }
   }
   
   /**
    * This method is used to process the field an annotation given.
    * This will check to determine if the field is accessible, if it
    * is not accessible then it is made accessible so that private
    * member fields can be used during the serialization process.
    * 
    * @param field this is the field to be added as a contact
    * @param label this is the XML annotation used by the field
    */
   public void process(Field field, Annotation label) {
      if(!field.isAccessible()) {
         field.setAccessible(true);              
      }           
      add(new FieldContact(field, label));
   }
   
   /**
    * This method is used to pair the get fields with a matching
    * parameter. This pairs the parameter and the field so that the
    * schema will always be a readable and writable object. If no
    * such pairing was done an illegal class schema could be created.
    *  
    * @throws Exception thrown if there is no matching parameter
    */
   private void validate()  throws Exception {
      for(Contact contact : this) {
         String name = contact.getName();
         
         if(name != null) {
            validate(contact, name);
         }
      }
   }
   
   /**
    * This method is used to pair the get fields with a matching
    * parameter. This pairs the parameter and the field so that the
    * schema will always be a readable and writable object. If no
    * such pairing was done an illegal class schema could be created.
    * 
    * @param field this is a field that is to be validated against
    * @param name this is the name of the parameter to be validated  
    *  
    * @throws Exception thrown if there is no matching parameter
    */
   private void validate(Contact field, String name)  throws Exception {
      Parameter parameter = table.getParameter(name);
      
      if(parameter != null) {
         validate(field, parameter);
      }
   }
   
   /**
    * This method is used to pair the get fields with a matching
    * parameter. This pairs the parameter and the field so that the
    * schema will always be a readable and writable object. If no
    * such pairing was done an illegal class schema could be created.
    * 
    * @param field this is a field that is to be validated against
    * @param parameter this is the parameter to be validated against
    *  
    * @throws Exception thrown if there is no matching parameter
    */
   private void validate(Contact field, Parameter parameter)  throws Exception {   
      Annotation label = field.getAnnotation();
      String name = parameter.getName();
      
      if(!parameter.getAnnotation().equals(label)) {
         throw new PersistenceException("Annotations do not match for '%s' in %s", name, type);
      }
      Class expect = field.getType();
      
      if(expect != parameter.getType()) {
         throw new PersistenceException("Parameter does not match field for '%s' in %s", name, type);
      }
 
   }
}

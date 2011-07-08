/*
 * FieldScanner.java April 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.core;

import static org.simpleframework.xml.DefaultType.FIELD;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.Version;

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
    * This is used to create the synthetic annotations for fields.
    */
   private final AnnotationFactory factory;
   
   /**
    * This is used to acquire the hierarchy for the class scanned.
    */
   private final Hierarchy hierarchy;
   
   /**
    * This is the default access type to be used for this scanner.
    */
   private final DefaultType access;
   
   /**
    * This is used to determine which fields have been scanned.
    */
   private final ContactMap done;
   
   /**
    * Constructor for the <code>FieldScanner</code> object. This is
    * used to perform a scan on the specified class in order to find
    * all fields that are labeled with an XML annotation.
    * 
    * @param type this is the schema class that is to be scanned
    */
   public FieldScanner(Class type) throws Exception {
      this(type, null);
   }
   
   /**
    * Constructor for the <code>FieldScanner</code> object. This is
    * used to perform a scan on the specified class in order to find
    * all fields that are labeled with an XML annotation.
    * 
    * @param type this is the schema class that is to be scanned
    * @param access this is the access type for the class
    */
   public FieldScanner(Class type, DefaultType access) throws Exception {
      this(type, access, true);
   }
   
   /**
    * Constructor for the <code>FieldScanner</code> object. This is
    * used to perform a scan on the specified class in order to find
    * all fields that are labeled with an XML annotation.
    * 
    * @param type this is the schema class that is to be scanned
    * @param access this is the access type for the class
    * @param required this is used to determine the requirement
    */
   public FieldScanner(Class type, DefaultType access, boolean required) throws Exception {
      this.factory = new AnnotationFactory(required);
      this.hierarchy = new Hierarchy(type);
      this.done = new ContactMap();
      this.access = access;
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
         scan(next, access);
      } 
      for(Class next : hierarchy) {
         scan(next, type);
      }  
      build();
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
   private void scan(Class type, Class real) {
      Field[] list = type.getDeclaredFields();
      
      for(Field field : list) {
         scan(field);                      
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
   private void scan(Field field) {
      Annotation[] list = field.getDeclaredAnnotations();
      
      for(Annotation label : list) {
         scan(field, label);                       
      }  
   }
   
   /**
    * This is used to scan all the fields of the class in order to
    * determine if it should have a default annotation. If the field
    * should have a default XML annotation then it is added to the
    * list of contacts to be used to form the class schema.
    * 
    * @param type this is the type to have its fields scanned
    * @param access this is the default access type for the class
    */
   private void scan(Class type, DefaultType access) throws Exception {
      Field[] list = type.getDeclaredFields();
      
      if(access == FIELD) {
         for(Field field : list) {
            Class real = field.getType();
            
            if(!isStatic(field)) {
               process(field, real);
            }
         }   
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
   private void scan(Field field, Annotation label) {
      if(label instanceof Attribute) {
         process(field, label);
      }
      if(label instanceof ElementUnion) {
         process(field, label);
      }
      if(label instanceof ElementListUnion) {
         process(field, label);
      }
      if(label instanceof ElementMapUnion) {
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
      if(label instanceof Transient) {
         remove(field, label);
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
    * @param type this is the type to acquire the annotation
    */
   private void process(Field field, Class type) throws Exception {
      Annotation label = factory.getInstance(type);
      
      if(label != null) {
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
   private void process(Field field, Annotation label) {
      Contact contact = new FieldContact(field, label);
      
      if(!field.isAccessible()) {
         field.setAccessible(true);              
      }          
      done.put(field, contact);
   }
   
   /**
    * This is used to remove a field from the map of processed fields.
    * A field is removed with the <code>Transient</code> annotation
    * is used to indicate that it should not be processed by the
    * scanner. This is required when default types are used.
    * 
    * @param field this is the field to be removed from the map
    * @param label this is the label associated with the field
    */
   private void remove(Field field, Annotation label) {
      done.remove(field);
   }
 
   /**
    * This is used to build a list of valid contacts for this scanner.
    * Valid contacts are fields that are either defaulted or those
    * that have an explicit XML annotation. Any field that has been
    * marked as transient will not be considered as valid.
    */
   private void build() {
      for(Contact contact : done) {
         add(contact);
      }
   }
   
   /**
    * This is used to determine if a field is static. If a field is
    * static it should not be considered as a default field. This
    * ensures the default annotation does not pick up static finals.
    * 
    * @param field this is the field to determine if it is static
    * 
    * @return true if the field is static, false otherwise
    */
   private boolean isStatic(Field field) {
      int modifier = field.getModifiers();
      
      if(Modifier.isStatic(modifier)) {
         return true;
      }
      return false;
   }
}
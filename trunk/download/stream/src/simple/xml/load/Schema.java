/*
 * Schema.java July 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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

package simple.xml.load;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * The <code>Schema</code> object is used to track which fields within
 * an object have been visited by a converter. This object is nessecary
 * for processing <code>Composite</code> objects. In particular it is
 * nessecary to keep track of which required nodes have been visited 
 * and which have not, if a required not has not been visited then the
 * XML source does not match the XML class schema and serialization
 * must fail before processing any further. 
 * 
 * @author Niall Gallagher
 */ 
final class Schema {

   /**
    * Contains a map of all attributes present within the schema.
    */
   private LabelMap attributes;
   
   /**
    * Contains a mpa of all elements present within the schema.
    */
   private LabelMap elements;

   /**
    * This is the pointer to the schema class commit method.
    */
   private Method commit;

   /**
    * This is the pointer to the schema class validation method.
    */
   private Method validate;

   /**
    * This is the pointer to the schema class persist method.
    */
   private Method persist;

   /**
    * This is the pointer to the schema class complete method.
    */
   private Method complete;

   /**
    * This is the table used to maintain attributes by the source.
    */ 
   private Map table;

   /**
    * Constructor for the <code>Schema</code> object. This is used 
    * to wrap the element and attribute XML annotations scanned from
    * a class schema. The schema tracks all fields visited so that
    * a converter can determine if all fields have been serialized.
    * 
    * @param schema this contains all labels scanned from the class
    */
   public Schema(Scanner schema, Map table) {
      this.attributes = schema.getAttributes();
      this.elements = schema.getElements();
      this.validate = schema.getValidate();      
      this.complete = schema.getComplete();
      this.commit = schema.getCommit();      
      this.persist = schema.getPersist();
      this.table = table;
   }
   
   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML attributes. Labels contained within
    * this map are used to convert primitive types only.
    * 
    * @return map with the details extracted from the schema class
    */ 
   public LabelMap getAttributes() {
      return attributes;
   }   
   /**
    * Returns a <code>LabelMap</code> that contains the details for
    * all fields marked as XML elements. The annotations that are
    * considered elements are the <code>ElementList</code> and the
    * <code>Element</code> annotations. 
    * 
    * @return a map containing the details for XML elements
    */
   public LabelMap getElements() {
      return elements;
   }

   /**
    * This method is used to invoke the provided objects commit method
    * during the deserialization process. The commit method must be
    * marked with the <code>Commit</code> annotation so that when the
    * object is deserialized the persister has a chance to invoke the
    * method so that the object can build further data structures.
    * 
    * @param source this is the object that has just been deserialized
    * 
    * @throws Exception thrown if the commit process cannot complete
    */
   public void commit(Object source) throws Exception {
      if(commit != null) {           
         if(isContextual(commit)) {              
            commit.invoke(source, table);           
         } else {
            commit.invoke(source);                 
         }
      }      
   }

   /**
    * This method is used to invoke the provided objects validation
    * method during the deserialization process. The validation method
    * must be marked with the <code>Validate</code> annotation so that
    * when the object is deserialized the persister has a chance to 
    * invoke that method so that object can validate its field values.
    * 
    * @param source this is the object that has just been deserialized
    * 
    * @throws Exception thrown if the validation process failed
    */
   public void validate(Object source) throws Exception {
      if(validate != null) {   
         if(isContextual(validate)) {         
            validate.invoke(source, table);           
         } else {
            validate.invoke(source);                 
         }            
      }         
   }
   
   /**
    * This method is used to invoke the provided objects persistence
    * method. This is invoked during the serialization process to
    * get the object a chance to perform an nessecary preparation
    * before the serialization of the object proceeds. The persist
    * method must be marked with the <code>Persist</code> annotation.
    * 
    * @param source the object that is about to be serialized
    * 
    * @throws Exception thrown if the object cannot be persisted
    */
   public void persist(Object source) throws Exception {
      if(persist != null) {           
         if(isContextual(persist)) {              
            persist.invoke(source, table);           
         } else {
            persist.invoke(source);                 
         }            
      }         
   }
   
   /**
    * This method is used to invoke the provided objects completion
    * method. This is invoked after the serialization process has
    * completed and gives the object a chance to restore its state
    * if the persist method required some alteration or locking.
    * This is marked with the <code>Complete</code> annotation.
    * 
    * @param source this is the object that has been serialized
    * 
    * @throws Exception thrown if the object cannot complete
    */
   public void complete(Object source) throws Exception {
      if(complete != null) {           
         if(isContextual(complete)) {              
            complete.invoke(source, table);           
         } else {
            complete.invoke(source);                 
         }            
      }         
   }

   /**
    * This is used to determine whether the annotated method takes a
    * contextual object. If the method takes a <code>Map</code> then
    * this returns true, otherwise it returns false.
    *
    * @param method this is the method to check the parameters of
    *
    * @return this returns true if the method takes a map object
    */ 
   private boolean isContextual(Method method) throws Exception {
      Class[] list = method.getParameterTypes();

      if(list.length == 1) {
         return Map.class.equals(list[0]);                 
      }      
      return false;
   }
}

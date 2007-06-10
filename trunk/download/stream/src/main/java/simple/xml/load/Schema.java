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
    * This is the pointer to the schema class replace method.
    */
   private Method replace;
   
   /**
    * This is used to represent a text value within the schema.
    */
   private Label text;

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
    * @param session this is the session map for the persister
    */
   public Schema(Scanner schema, Session session) {      
      this.attributes = schema.getAttributes();
      this.elements = schema.getElements();
      this.validate = schema.getValidate();      
      this.complete = schema.getComplete();
      this.replace = schema.getReplace();
      this.commit = schema.getCommit();      
      this.persist = schema.getPersist();
      this.text = schema.getText();
      this.table = session.getMap();
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
    * This returns the <code>Label</code> that represents the text
    * annotation for the scanned class. Only a single text annotation
    * can be used per class, so this returns only a single label
    * rather than a <code>LabelMap</code> object. Also if this is
    * not null then the elements label map will be empty.
    * 
    * @return this returns the text label for the scanned class
    */
   public Label getText() {
	   return text;
   }
   
   /**
    * This is used to replace the deserialized object with another
    * instance, perhaps of a different type. This is useful when an
    * XML schema class acts as a reference to another XML document
    * which needs to be loaded externally to create an object of
    * a different type.
    * 
    * @param source the source object to invoke the method on
    * 
    * @return this returns the object that acts as the replacement
    * 
    * @throws Exception if the replacement method cannot complete
    */
   public Object replace(Object source) throws Exception {
      if(replace != null) {
         return invoke(source, replace);
      }
      return null;
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
         invoke(source, commit);
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
         invoke(source, validate);
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
         invoke(source, persist);           
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
         invoke(source, complete);
      }
   }

   /**
    * This method is used to invoke the specified method. If it has 
    * a single parameter that takes a <code>Map</code> then the
    * provided method will be invoked with the session map. If it
    * takes no parameters then it is invoked and the return value
    * is returned from the method.
    * 
    * @param source this is the method to invoke the method on
    * @param method this is the method that is to be invoked
    * 
    * @return this is the return value from the specified method
    * 
    * @throws Exception thrown if the method cannot be invoked
    */
   private Object invoke(Object source, Method method) throws Exception {                
      if(isContextual(method)) {              
         return method.invoke(source, table);           
      }
      return method.invoke(source);                    
   }
   
   /**
    * This is used to determine if the replace method has even been
    * defined. This is required as it is perfectly leagal for the
    * replace method to return null. Also if the method does not
    * return a valid object the replace method is considered invalid. 
    * 
    * @return this returns true if the schema has a replace method
    */
   public boolean isReplace() {
      return replace != null;      
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

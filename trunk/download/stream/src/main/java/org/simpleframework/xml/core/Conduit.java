/*
 * Conduit.java June 2007
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

import java.lang.reflect.Method;
import java.util.Map;

/**
 * The <code>Conduit</code> acts as a means for the schema to invoke
 * the callback methods on an object. This ensures that the correct
 * method is invoked within the schema class. If the annotated method
 * accepts a map then this will provide that map to the method. This
 * also ensures that if specific annotation is not present in the 
 * class that no action is taken on a persister callback. 
 * 
 * @author Niall Gallagher
 */
class Conduit {
   
   /**
    * This is the pointer to the schema class commit method.
    */
   private final Method commit;

   /**
    * This is the pointer to the schema class validation method.
    */
   private final Method validate;

   /**
    * This is the pointer to the schema class persist method.
    */
   private final Method persist;

   /**
    * This is the pointer to the schema class complete method.
    */
   private final Method complete;
   
   /**
    * This is the pointer to the schema class replace method.
    */
   private final Method replace;
   
   /**
    * This is the pointer to the schema class resolve method.
    */
   private final Method resolve;
   
   /**
    * Constructor for the <code>conduit</code> object. This is used 
    * to wrap the schema class such that callbacks from the persister
    * can be dealt with in a seamless manner. This ensures that the
    * correct method and arguments are provided to the methods.
    * element and attribute XML annotations scanned from
    * 
    * @param schema this is the scanner that contains the methods
    */
   public Conduit(Scanner schema) {     
      this.validate = schema.getValidate();      
      this.complete = schema.getComplete();
      this.replace = schema.getReplace();
      this.resolve = schema.getResolve();
      this.persist = schema.getPersist();  
      this.commit = schema.getCommit();    
   }
   
   /**
    * This is used to replace the deserialized object with another
    * instance, perhaps of a different type. This is useful when an
    * XML schema class acts as a reference to another XML document
    * which needs to be loaded externally to create an object of
    * a different type.
    * 
    * @param source the source object to invoke the method on
    * @param map this is the session map used by the persister
    * 
    * @return this returns the object that acts as the replacement
    * 
    * @throws Exception if the replacement method cannot complete
    */
   public Object replace(Object source, Map map) throws Exception {
      if(replace != null) {        
         return invoke(source, replace, map);
      }
      return source;
   }
   
   /**
    * This is used to replace the deserialized object with another
    * instance, perhaps of a different type. This is useful when an
    * XML schema class acts as a reference to another XML document
    * which needs to be loaded externally to create an object of
    * a different type.
    * 
    * @param source the source object to invoke the method on
    * @param map this is the session map used by the persister 
    * 
    * @return this returns the object that acts as the replacement
    * 
    * @throws Exception if the replacement method cannot complete
    */
   public Object resolve(Object source, Map map) throws Exception {
      if(resolve != null) {
         return invoke(source, resolve, map);
      }
      return source;
   }
   
   /**
    * This method is used to invoke the provided objects commit method
    * during the deserialization process. The commit method must be
    * marked with the <code>Commit</code> annotation so that when the
    * object is deserialized the persister has a chance to invoke the
    * method so that the object can build further data structures.
    * 
    * @param source this is the object that has just been deserialized
    * @param map this is the session map used by the persister 
    * 
    * @throws Exception thrown if the commit process cannot complete
    */
   public void commit(Object source, Map map) throws Exception {
      if(commit != null) {
         invoke(source, commit, map);
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
    * @param map this is the session map used by the persister 
    * 
    * @throws Exception thrown if the validation process failed
    */
   public void validate(Object source, Map map) throws Exception {
      if(validate != null) {
         invoke(source, validate, map);
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
    * @param map this is the session map used by the persister
    * 
    * @throws Exception thrown if the object cannot be persisted
    */
   public void persist(Object source, Map map) throws Exception {
      if(persist != null) {
         invoke(source, persist, map);
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
    * @param map this is the session map used by the persister
    * 
    * @throws Exception thrown if the object cannot complete
    */
   public void complete(Object source, Map map) throws Exception {
      if(complete != null) {
         invoke(source, complete, map);
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
    * @param map this is the session map used by the persister
    * 
    * @return this is the return value from the specified method
    * 
    * @throws Exception thrown if the method cannot be invoked
    */
   private Object invoke(Object source, Method method, Map map) throws Exception {
      if(source == null) {
         return null;
      }
      if(isContextual(method)) {              
         return method.invoke(source, map);           
      }
      return method.invoke(source);
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

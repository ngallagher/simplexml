package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;

interface Variable {

   /**
    * This will provide the contact type. The contact type is the
    * class that is to be set and get on the object. Typically the
    * type will be a serializable object or a primitive type.
    *
    * @return this returns the type that this contact represents
    */ 
   public Class getType();
   
   /**
    * This provides the dependant class for the contact. This will
    * typically represent a generic type for the actual type. For
    * contacts that use a <code>Collection</code> type this will
    * be the generic type parameter for that collection.
    * 
    * @return this returns the dependant type for the contact
    */
   public Class getDependant();
   
   /**
    * This provides the dependant classes for the contact. This will
    * typically represent a generic types for the actual type. For
    * contacts that use a <code>Map</code> type this will be the 
    * generic type parameter for that map type declaration.
    * 
    * @return this returns the dependant type for the contact
    */
   public Class[] getDependants(); 
   
   /**
    * This is the annotation associated with the point of contact.
    * This will be an XML annotation that describes how the contact
    * should be serialized and deserialized from the object.
    *
    * @return this provides the annotation associated with this
    */
   public Annotation getAnnotation();
   
   /**
    * This represents the name of the object contact. If the contact
    * is a field then the name of the field is provided. If however
    * the contact is a method then the Java Bean name of the method
    * is provided, which will be the decapatilized name of the 
    * method without the get, set, or is prefix to the method.
    * 
    * @return this returns the name of the contact represented
    */
   public String getName() throws Exception;
}

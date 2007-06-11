/*
 * MethodName.java April 2007
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

package simple.xml.load;

import java.lang.reflect.Method;

/**
 * The <code>MethodName</code> object is used to represent the name
 * of a Java Bean method. This contains the Java Bean name the type
 * and the actual method it represents. This allows the scanner to
 * create <code>MethodPart</code> objects based on the method type.
 * 
 * @author Niall Gallagher
 */
class MethodName {
   
   /**
    * This is the type of method this method name represents.
    */
   private MethodType type;
   
   /**
    * This is the actual method that this method name represents.
    */
   private Method method;
   
   /**
    * This is the Java Bean method name that is represented.
    */
   private String name;
   
   /**
    * Constructor for the <code>MethodName</code> objects. This is
    * used to create a method name representation of a method based
    * on the method type and the Java Bean name of that method.
    * 
    * @param method this is the actual method this is representing
    * @param type type used to determine if it is a set or get
    * @param name this is the Java Bean property name of the method
    */
   public MethodName(Method method, MethodType type, String name) {
      this.name = name.intern();
      this.method = method;
      this.type = type;      
   }
   
   /**
    * This provdes the name of the method part as acquired from the
    * method name. The name represents the Java Bean property name
    * of the method and is used to pair getter and setter methods.
    * 
    * @return this returns the Java Bean name of the method part
    */
   public String getName() {    
      return name;
   }
   
   /**
    * This is the method type for the method part. This is used in
    * the scanning process to determine which type of method a
    * instance represents, this allows set and get methods to be
    * paired.
    * 
    * @return the method type that this part represents
    */ 
   public MethodType getType() {
      return type;
   }
   
   /**
    * This is the method for this point of contact. This is what
    * will be invoked by the serialization or deserialization 
    * process when an XML element or attribute is to be used.
    * 
    * @return this returns the method associated with this
    */
   public Method getMethod() {
      return method;      
   }  
}
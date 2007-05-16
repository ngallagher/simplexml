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
final class MethodName {
   
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
   
   public MethodName(Method method, MethodType type, String name) {
      this.name = name.intern();
      this.method = method;
      this.type = type;      
   }
   
   public String getName() {    
      return name;
   }
   
   public MethodType getType() {
      return type;
   }
   
   public Method getMethod() {
      return method;      
   }  
}
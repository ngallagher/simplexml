/*
 * MethodType.java May 2007
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

package org.simpleframework.xml.load;

/**
 * The <code>MethodType</code> enumeration is used to specify a 
 * set of types that can be used to classify Java Beans methods.
 * This creates three types for the get, is, and set methods. The
 * method types allow the <code>MethodScanner</code> to determine
 * what function the method has in creating a contact point for
 * the object. This also enables methods to be parsed correctly.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.load.MethodScanner
 * @see org.simpleframework.xml.load.MethodPart
 */
enum MethodType {
   
   /**
    * This is used to represent a method that acts as a getter.
    */
   GET(3),
   
   /**
    * This is used to represent a method that acts as a getter.
    */
   IS(2),
   
   /**
    * This is used to represent a method that acts as a setter.
    */
   SET(3);   
   
   /**
    * This is the length of the prefix the method type uses.
    */
   private int prefix;
   
   /**
    * Constructor for the <code>MethodType</code> object. This is
    * used to create a method type specifying the length of the
    * prefix. This allows the method name to be parsed easliy.
    * 
    * @param prefix this is the length of the method name prefix
    */
   private MethodType(int prefix) {
      this.prefix = prefix;
   }
   
   /**
    * This is used to acquire the prefix for the method type. The
    * prefix allows the method name to be extracted easily as it
    * is used to determine the character range that forms the name.
    * 
    * @return this returns the method name prefix for the type
    */
   public int getPrefix() {
      return prefix;
   }
}
/*
 * SystemFilter.java May 2006
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

package simple.xml.filter;

/**
 * The <code>SystemFilter</code> object is used to provide a filter
 * that will replace the specified values with system properties.
 * This can be given a delegate filter which can be used to resolve
 * replacements should the value requested not match a property. 
 * 
 * @author Niall Gallagher
 */
public class SystemFilter implements Filter {
   
   /**
    * Filter delegated to if no system property can be resolved.
    */
   private Filter filter;        
        
   /**
    * Constructor for the <code>SystemFilter</code> object. This 
    * creates a filter that will resolve replacements using system
    * properties. Should the system properties not contain the
    * requested mapping this will return a null value.
    */
   public SystemFilter() {
      this(null);           
   }
        
   /**
    * Constructor for the <code>SystemFilter</code> object. This 
    * creates a filter that will resolve replacements using system
    * properties. Should the system properties not contain the
    * requested mapping this delegates to the specified filter.
    * 
    * @param filter the filter delegated to if resolution fails
    */   
   public SystemFilter(Filter filter) {
      this.filter = filter;           
   }
   
   /**
    * Replaces the text provided with the value resolved from the
    * system properties. If the system properties fails this will
    * delegate to the specified <code>Filter</code> if it is not
    * a null object. If no match is found a null is returned.
    * 
    * @param text this is the text value to be replaced
    * 
    * @return this will return the replacement text resolved
    */
   public String replace(String text) {
      String value = System.getProperty(text);           

      if(value != null) {
         return value;                       
      }
      if(filter != null) {
         return filter.replace(text);              
      }      
      return null;
   }   
}

/*
 * TemplateFilter.java May 2005
 *
 * Copyright (C) 2005, Niall Gallagher <niallg@users.sf.net>
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

import org.simpleframework.xml.filter.Filter;

/**
 * The <code>TemplateFilter</code> class is used to provide variables
 * to the template engine. This template acquires variables from two
 * different sources. Firstly this will consult the user contextual
 * <code>Context</code> object, which can contain variables that have
 * been added during the deserialization process. If a variable is
 * not present from this context it asks the <code>Filter</code> that
 * has been specified by the user.
 * 
 * @author Niall Gallagher
 */ 
class TemplateFilter implements Filter {

   /**
    * This is the template context object used by the persister.
    */ 
   private Context context;

   /**
    * This is the filter object provided to the persister.
    */ 
   private Filter filter;
        
   /**
    * Constructor for the <code>TemplateFilter</code> object. This
    * creates a filter object that acquires template values from
    * two different contexts. Firstly the <code>Context</code> is
    * queried for a variables followed by the <code>Filter</code>.
    *
    * @param context this is the context object for the persister
    * @param filter the filter that has been given to the persister
    */ 
   public TemplateFilter(Context context, Filter filter) {
      this.context = context;     
      this.filter = filter;      
   }        

   /**
    * This will acquire the named variable value if it exists. If
    * the named variable cannot be found in either the context or
    * the user specified filter then this returns null.
    * 
    * @param name this is the name of the variable to acquire
    *
    * @return this returns the value mapped to the variable name
    */ 
   public String replace(String name) {
      Object value = context.getAttribute(name);

      if(value != null) {
         return value.toString();              
      }      
      return filter.replace(name);
   }
}

/*
 * PlatformFilter.java May 2006
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

package org.simpleframework.xml.filter;

import java.util.Map;

/**
 * The <code>PlatformFilter</code> object makes use of all filter
 * types this resolves user specified properties first, followed
 * by system properties, and finally environment variables. This
 * filter will be the default filter used by most applications as
 * it can make use of all values within the application platform.
 * 
 * @author Niall Gallagher 
 */
public class PlatformFilter extends StackFilter {

   /**
    * Constructor for the <code>PlatformFilter</code> object. This
    * adds a filter which can be used to resolve environment 
    * variables followed by one that can be used to resolve system
    * properties and finally one to resolve user specified values.
    */
   public PlatformFilter() {
      this(null);
   }
   
   /**
    * Constructor for the <code>PlatformFilter</code> object. This
    * adds a filter which can be used to resolve environment 
    * variables followed by one that can be used to resolve system
    * properties and finally one to resolve user specified values.
    * 
    * @param map this is a map contain the user mappings
    */
   public PlatformFilter(Map map) {
      this.push(new EnvironmentFilter());
      this.push(new SystemFilter());
      this.push(new MapFilter(map));      
   }        
}

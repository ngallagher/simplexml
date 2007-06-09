/*
 * Match.java March 2002
 *
 * Copyright (C) 2001, Niall Gallagher <niallg@users.sf.net>
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
 
package simple.xml.util;

import simple.xml.Attribute;
import simple.xml.Root;

/**
 * This object is stored within a <code>Resolver</code> so that it 
 * can be retrieved using a string that matches its pattern. Any
 * object that extends this can be inserted into the resolver and
 * retrieved using a string that matches its pattern. For example
 * take the following pattern "*.html" this will match the string
 * "/index.html" or "readme.html". This object should be extended
 * to add more XML attributes and elements, which can be retrieved
 * when the <code>Match</code> object is retrieve from a resolver.
 *
 * @author Niall Gallagher
 */
@Root
public abstract class Match {

   /**
    * This is the pattern string that is used by the resolver.
    */ 
   @Attribute        
   protected String pattern;        
}

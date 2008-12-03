/*
 * NamespaceList.java July 2008
 *
 * Copyright (C) 2008, Niall Gallagher <niallg@users.sf.net>
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

package org.simpleframework.xml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The <code>NamespaceList</code> annotation that is used to declare
 * namespaces that can be added to an element. This is used when 
 * there are several namespaces to add to the element without setting
 * any namespace to the element. This is useful when the scope of a
 * namespace needs to span several nodes. All prefixes declared in 
 * the namespaces will be available to the child nodes.
 * <pre>
 * 
 *    &lt;example xmlns:root="http://www.example.com/root"&gt;
 *       &lt;anonymous&gt;anonymous element&lt;/anonymous&gt;
 *    &lt;/example&gt;
 *    
 * </pre>
 * The above XML example shows how a prefixed namespce has been added
 * to the element without qualifying that element. Such declarations
 * will allow child elements to pick up the parents prefix when this
 * is required, this avoids having to redeclare the same namespace.
 * 
 * @author Niall Gallagher
 *
 * @see org.simpleframework.xml.Namespace
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NamespaceList {

   /**
    * This is used to acquire the nanmespaces that are delcared on
    * the class. Any number of namespaces can be declared. None of
    * the declared nanmespaces will be made the elements namespace,
    * instead it will simply declare the namespaces so that the
    * reference URI and prefix will be made available to children.
    * 
    * @return this returns the namespaces that are declared.
    */
   public Namespace[] value() default {};
}

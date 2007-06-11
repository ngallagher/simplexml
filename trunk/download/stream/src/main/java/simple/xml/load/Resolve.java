/*
 * Resolve.java June 2007
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

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
 * The <code>Resolve</code> method is used to resolve an object that
 * has been deserialized from the XML document. This is used when the
 * deserialized object whats to provide a substitute to itself within
 * the object graph. This is particularly useful when an object is
 * used to reference an external XML document, as it allows that XML
 * document to be deserialized in to a new object instance.
 * <p>
 * This is similar to the <code>readResolve</code> method used within
 * Java Object Serialization in that it is used to create a object to 
 * plug in to the object graph after it has been fully deserialized.
 * Care should be taken when using this annotation as the object that
 * is returned from the resolve method must match the field type such
 * that the resolved object is an assignable substitute.
 * 
 * @author Niall Gallagher
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Resolve {  
}
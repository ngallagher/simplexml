/*
 * Replace.java June 2007
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

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
 * The <code>Replace</code> method is used to replace an object that
 * is about to be serialized to an XML document. This is used to so
 * that an object can provide a substitute to itself. Scenarios such
 * as serializing an object to an external file or location can be
 * accommodated using a write replacement method.
 * <p>
 * This is similar to the <code>writeReplace</code> method used within
 * Java Object Serialization in that it is used to plug a replacement
 * in to the resulting stream during the serialization process. Care
 * should be taken to provide a suitable type from the repacement so
 * that the object can be deserialized at a later time.
 * 
 * @author Niall Gallagher
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Replace {  
}

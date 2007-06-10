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

package simple.xml.load;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
 * The <code>Replace</code> method is used to replace an object that
 * has been deserialized from the XML document. This is used when the
 * deserialized object whats to provide a substitute to itself within
 * the object graph. This is particularly useful when an object is
 * used to reference an external XML document, as it allows that XML
 * document to be deserialized in to a new object instance.
 * <p>
 * Caution should be taken when using this annotation as it results 
 * in a serialized XML document that may be dramatically different
 * from the serialized format. Typically this is used when the object
 * being deserialized is to be used for configuration purposes only.
 * 
 * @author Niall Gallagher
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Replace {  
}
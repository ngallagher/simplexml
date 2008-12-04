/*
 * Filter.java May 2006
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

/**
 * The <code>Filter</code> object is used to provide replacement string
 * values for a provided key. This allows values within the XML source
 * document to be replaced using sources such as OS environment variables
 * and Java system properties.
 * <p>
 * All filtered variables appear within the source text using a template
 * and variable keys marked like <code>${example}</code>. When the XML 
 * source file is read all template variables are replaced with the 
 * values provided by the filter. If no replacement exists then the XML
 * source text remains unchanged.
 * 
 * @author Niall Gallagher
 */
public interface Filter {

   /**
    * Replaces the text provided with some property. This method 
    * acts much like a the get method of the <code>Map</code>
    * object, in that it uses the provided text as a key to some 
    * value. However it can also be used to evaluate expressions
    * and output the result for inclusion in the generated XML.
    *
    * @param text this is the text value that is to be replaced
    * 
    * @return returns a replacement for the provided text value
    */
   public String replace(String text);        
}

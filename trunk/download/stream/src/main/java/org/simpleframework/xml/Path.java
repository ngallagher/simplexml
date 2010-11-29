/*
 * Path.java November 2010
 *
 * Copyright (C) 2010, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The <code>Path</code> annotation is used to specify an XML path 
 * where an XML element or attribute is located. The format must be
 * in XPath format. When an XML structure does not map exactly to
 * an object model this annotation can be used to navigate the XML
 * document in order to map attributes and elements to an associated
 * field or method. For example, take the annotation shown below.
 * <pre>
 * 
 *    &#64;Element
 *    &#64;Path("contact-info/phone")
 *    private String number;
 * 
 * </pre>
 * For the above annotation the XPath expression locates the phone
 * number nested within several elements. Such a declaration can 
 * be used when a flat object structure is not suitable. The above
 * annotations will result in the below XML elements.
 * <pre>
 * 
 *    &lt;contact-info&gt;
 *       &lt;phone&gt;
 *          &lt;number&gt;1800123123&lt;/number&gt;
 *       &lt;/phone&gt;
 *    &lt;/contact-info&gt;
 *    
 * </pre>
 * As can be seen from this XML snippet a single field has been
 * mapped with several elements. These XPath expressions can be used
 * with either elements or attributes to convert an otherwise flat
 * object to XML structure in to something more complex. This is
 * useful when mapping objects to foreign XML formats.
 * 
 * @author Niall Gallagher
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {
   
   /**
    * This method is used to provide the XPath expression for the
    * annotation. Only a subset of expressions are supported. All
    * path formats can be parsed. However, if the path does not 
    * match the supported expressions an exception will be thrown.
    * Some examples of the formats supported are shown below.
    * <pre>
    * 
    *    ./example/path
    *    ./example/path/
    *    example/path
    *    example[2]/path
    *    
    * </pre>
    * There is no limit to the level of nesting supported. Also 
    * the <code>Order</code> annotation supports the above formats
    * so that nested elements can be order for serialization of
    * the fields and methods of the annotated types.
    * 
    * @return this returns an XPath expression for the location
    */
   public String value();
}

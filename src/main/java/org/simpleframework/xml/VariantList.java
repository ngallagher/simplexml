/*
 * VariantList.java March 2011
 *
 * Copyright (C) 2011, Niall Gallagher <niallg@users.sf.net>
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
 * The <code>VariantList</code> annotation is used to describe a field 
 * or method that can have a number of different XML schemas depending
 * on an XML element name of the instance type. Here a collection of
 * element list annotations can be declared. Each annotation expresses
 * the types the list can accept. Taking the declaration below, if the
 * annotation is inline, the list can take a number of varying types
 * all determined from the XML element name.
 * <pre>
 * 
 *    &#64;VariantList({
 *       &#64;ElementList(entry="x", inline=true, type=X.class),
 *       &#64;ElementList(entry="y", inline=true, type=Y.class),
 *       &#64;ElementList(entry="z", inline=true, type=Z.class)               
 *    })
 *    private List<Code> codes;
 *    
 * </pre>
 * For the above definition the list field can take any of the declared
 * types. On deserialization the name of the element will determine the
 * type that is instantiated and inserted in to the list. When the list
 * is serialized the list entry instance type will determine the name 
 * of the element the instance will serialized as. This provides a 
 * useful means of consume more complicated sources. 
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.xml.ElementList
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface VariantList {
   
   /**
    * This provides the <code>ElementList</code> annotations that have 
    * been defined for this variant. Each element list describes the 
    * XML class schema to use and the name of the XML element. This 
    * allows the serialization process to determine which elements
    * map to the defined types. Also, the types define how the XML
    * is generated for a given instance.     
    * 
    * @return the element lists defined for the variant declaration
    */   
   public ElementList[] value();
}


/*
 * Namespace.java July 2008
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
 * The <code>Namespace</code> annotation is used to set a namespace
 * on an element or attribute. By annotating a method, field or 
 * class with this annotation that entity assumes the XML namespace
 * provided. When used on a class the annotation describes the
 * namespace that should be used, this however can be overridden by
 * an annotated field or method declaration of that type.
 * <pre>
 *  
 *    &lt;book:book xmlns:book="http://www.example.com/book"&gt;
 *       &lt;book:author&gt;saurabh&lt;/book:author&gt;
 *       &lt;book:title&gt;example title&lt;/book:title&gt;
 *       &lt;book:isbn&gt;ISB-16728-10&lt;/book:isbn&gt;
 *    &lt;/book:book&gt;
 *
 * </pre>
 * In the above XML snippet a namespace has been declared with the
 * prefix "book" and the reference "http://www.example.com/book". If
 * such a namespace is applied to a class, method, or field then 
 * each of the attributes an elements, which does not have an 
 * explicit override then they inherit their parents prefix. 
 * <pre>
 *
 *    &lt;root:example xmlns:root="http://www.example.com/root"&gt;
 *       &lt;child xmlns=""&gt;
 *          &lt;anonymous&gt;anonymous element&lt;/anonymous&gt;
 *       &lt;/child&gt;
 *    &lt;/root:example&gt;
 *
 * </pre>
 * In order to override the parent namespace an anonymous namespace
 * can be declared. This overrides the parents scope with no default
 * namespace tied to the child elements which is declared within a
 * given namespace.
 *
 * @author Niall Gallagher
 */ 
@Retention(RetentionPolicy.RUNTIME)
public @interface Namespace {

   /**
    * This is used to specify the unique reference URI that is used 
    * to define the namespace within the document. This is typically
    * a URI as this is a well know universally unique identifier. 
    * It can be anything unique, but typically should be a unique
    * URI reference. If left as the empty string then this will
    * signify that the anonymous nanmespace will be used.
    *
    * @return this returns the reference used by this namespace    
    */         
   public String reference() default "";

   /**
    * This is used to specify the prefix used for the namespace. If
    * no prefix is specified then the reference becomes the default
    * namespace for the enclosing element. This means that all 
    * attributes and elements that do not contain a prefix belong
    * to the namespace declared by this annotation.
    *
    * @return this returns the prefix used for this namespace
    */ 
   public String prefix() default "";
}

/*
 * CamelCaseStyle.java July 2008
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

package org.simpleframework.xml.stream;

/**
 * The <code>CamelCaseStyle</code> is used to represent an XML style
 * that can be applied to a serialized object. A style can be used to
 * modify the element and attribute names for the generated document.
 * This styles can be used to generate camel case XML.
 * <pre>
 * 
 *    &lt;ExampleElement&gt;
 *        &lt;ChildElement exampleAttribute='example'&gt;
 *           &lt;InnerElement&gt;example&lt;/InnerElement&gt;
 *        &lt;/ChildElement&gt;
 *     &lt;/ExampleElement&gt;
 *     
 * </pre>
 * Above the camel case XML elements and attributes can be generated
 * from a style implementation. Styles enable the same objects to be
 * serialized in different ways, generating different styles of XML
 * without having to modify the class schema for that object.    
 * 
 * @author Niall Gallagher
 */
public class CamelCaseStyle implements Style {
   
   /**
    * This is used to perform the actual building of tokens.
    */
   private final Builder builder;
   
   /**
    * This is the strategy used to generate the style tokens.
    */
   private final Style style;
   
   /**
    * Constructor for the <code>CamelCaseStyle</code> object. This 
    * is used to create a style that will create camel case XML 
    * attributes and elements allowing a consistent format for 
    * generated XML. By default the elements have an upper case 
    * initial character and a lower case attribute. 
    */
   public CamelCaseStyle() {
      this(true, false);
   }
   
   /**
    * Constructor for the <code>CamelCaseStyle</code> object. This 
    * is used to create a style that will create camel case XML 
    * attributes and elements allowing a consistent format for 
    * generated XML. By default the attributes have a lower case 
    * initial character and an configurable element.
    * 
    * @param element if true the element will start as upper case
    */
   public CamelCaseStyle(boolean element) {
      this(element, false);
   }
   
   /**
    * Constructor for the <code>CamelCaseStyle</code> object. This 
    * is used to create a style that will create camel case XML 
    * attributes and elements allowing a consistent format for 
    * generated XML. Both the attribute an elements are configurable.
    * 
    * @param element if true the element will start as upper case
    * @param attribute if true the attribute starts as upper case
    */
   public CamelCaseStyle(boolean element, boolean attribute) {
      this.style = new CamelCaseBuilder(element, attribute);
      this.builder = new Builder(style);
   }

   /**
    * This is used to generate the XML attribute representation of 
    * the specified name. Attribute names should ensure to keep the
    * uniqueness of the name such that two different names will
    * be styled in to two different strings.
    * 
    * @param name this is the attribute name that is to be styled
    * 
    * @return this returns the styled name of the XML attribute
    */
   public String getAttribute(String name) {
      return builder.getAttribute(name); 
   }

   /**
    * This is used to set the attribute values within this builder.
    * Overriding the attribute values ensures that the default
    * algorithm does not need to determine each of the values. It
    * allows special behaviour that the user may require for XML.
    * 
    * @param name the name of the XML attribute to be overridden
    * @param value the value that is to be used for that attribute
    */
   public void setAttribute(String name, String value) {
      builder.setAttribute(name, value);
   }
   
   /**
    * This is used to generate the XML element representation of 
    * the specified name. Element names should ensure to keep the
    * uniqueness of the name such that two different names will
    * be styled in to two different strings.
    * 
    * @param name this is the element name that is to be styled
    * 
    * @return this returns the styled name of the XML element
    */
   public String getElement(String name) {
      return builder.getElement(name);
   }

   /**
    * This is used to set the element values within this builder.
    * Overriding the element values ensures that the default
    * algorithm does not need to determine each of the values. It
    * allows special behaviour that the user may require for XML.
    * 
    * @param name the name of the XML element to be overridden
    * @param value the value that is to be used for that element
    */
   public void setElement(String name, String value) {
      builder.setElement(name, value);
   }
}

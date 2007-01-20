/*
 * Strategy.java July 2006
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

package simple.xml.load;

import simple.xml.stream.NodeMap;
import java.util.Map;

/**
 * The <code>Strategy</code> interface represents a strategy that can be 
 * used to resolve and load the <code>Class</code> objects that compose 
 * a serializable object. A strategy implementation will make use of the
 * provided attribute node map to extract details that can be used to
 * determine what type of object must be used. 
 * <pre>
 * 
 *    &lt;xml version="1.0"&gt;
 *    &lt;example class="some.example.Type"&gt;
 *       &lt;integer&gt;2&lt;/integer&gt;
 *    &lt;/example&gt;
 *    
 * </pre> 
 * The above example shows how the default strategy augments elements
 * with "class" attributes that describe the type that should be used
 * to instantiate a field when an object is deserialized. So looking at
 * the above example the root element would be a "some.example.Type".
 * <p>
 * Custom <code>Strategy</code> implementations give the persister a
 * chance to intercept the class loading and type resolution for XML
 * documents. It also opens up the possibility for class versioning.
 * To establish contextual information a <code>Map</code> object can be
 * used. The map object is a transient object that is created and used
 * for the duration of a single operation of the persister.
 * 
 * @author Niall Gallagher
 *
 * @see simple.xml.load.Persister
 */
public interface Strategy {

   /**
    * This is used to resolve and load the root class type. To perform
    * the resolution this method is given the root node map for the
    * XML document. This is separate to the <code>readElement</code>
    * because the root element may contain details like the location
    * of the byte codes, much like the RMI codebase annotation, or
    * the version of the package for example "some.package.v2". This 
    * must return null if a class cannot be resolved.  
    *  
    * @param field this is the type of the root element expected
    * @param node this is the node map used to resolve an override
    * @param map this is used to maintain contextual information
    * 
    * @return returns the class that should be used for the object
    * 
    * @throws Exception thrown if the class cannot be resolved
    */
   public Class readRoot(Class field, NodeMap node, Map map) throws Exception;

   /**
    * This is used to resolve and load a class for the given element.
    * The class should be of the same type or a subclass of the class
    * specificed. It can be resolved using the details within the
    * provided XML node map, if the details used do not represent any
    * serializable values they should be removed so as not to disrupt
    * the deserialization process. For example the default strategy
    * removes all "class" attributes from the given node map.
    * 
    * @param field this is the type of the root element expected
    * @param node this is the node map used to resolve an override
    * @param map this is used to maintain contextual information
    * 
    * @return returns the class that should be used for the object
    * 
    * @throws Exception thrown if the class cannot be resolved
    */
   public Class readElement(Class field, NodeMap node, Map map) throws Exception;

   /**
    * This is used to attach attributes values to the given node
    * map during the serialization process. This method allows
    * the strategy to augment the XML document so that it can be
    * deserialized using a similar strategy. For example the 
    * default strategy adds a "class" attribute to the node map.
    * The root element is a special element in that it can add
    * details regarding the code base location or version to use. 
    *  
    * @param field this is the declared class for the field used
    * @param value this is the instance variable being serialized
    * @param node this is the node map used to represent the value
    * @param map this is used to maintain contextual information
    * 
    * @throws Exception thrown if the details cannot be set
    */
   public void writeRoot(Class field, Object value, NodeMap node, Map map) throws Exception;

   /**
    * This is used to attach attribute values to the given node
    * map during the serialization process. This method allows
    * the strategy to augment the XML document so that it can be
    * deserialized using a similar strategy. For example the 
    * default strategy adds a "class" attribute to the node map.
    *  
    * @param field this is the declared class for the field used
    * @param value this is the instance variable being serialized
    * @param node this is the node map used to represent the value
    * @param map this is used to maintain contextual information
    * 
    * @throws Exception thrown if the details cannot be set
    */
   public void writeElement(Class field, Object value, NodeMap node, Map map) throws Exception;

}

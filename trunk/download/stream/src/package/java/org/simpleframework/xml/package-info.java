/**
 * Provides all of the core annotations that can be used within a class.
 * All annotations here are used to describe an XML schema based on the
 * fields and methods of a class. Annotations can be placed in various
 * locations within the class, for example take the following class.
 * <pre>
 * 
 *    &#64;Root
 *    public class Example {
 *    
 *       &#64;Attribute
 *       private String name;
 * 
 *       &#64;Element
 *       private String value;
 *   
 *       public Example() {
 *          super();
 *       }
 *       
 *       // ...
 *    }
 *    
 * </pre>
 * When a class is annotated in a similar manner to the above example it
 * can be converted to and from an XML document using an implementation 
 * of the <code>Serializer</code> interface, which is provided in the
 * core package. Below is an example of the resulting XML when writing
 * the above annotated class.
 * <pre>
 *    
 *    &lt;example name='x'&gt;
 *       &lt;value&gt;y&lt;/value&gt;
 *    &lt;/example&gt; 
 *    
 * </pre>
 * The same instance can be recovered from the XML document at a later
 * stage if required. For further information on how to annotate classes
 * see the documentation for the annotations within this package.
 * 
 * @see org.simpleframework.xml.core
 */
package org.simpleframework.xml;

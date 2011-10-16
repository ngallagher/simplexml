/**
 * Provides a collection of filter objects used for text substitution.
 * Text substitution is performed during the serialization process 
 * for any string with of a certain format. An example of the format
 * that is used can be seen below.
 * <pre>
 * 
 *    ${variable.name}
 *    
 * </pre>
 * The serializer will query any <code>Filter</code> implementation 
 * with the name extracted. If value is found then the text is 
 * converted during deserialization. This offers a convenient way
 * to assign dynamic values.
 * 
 * @see org.simpleframework.xml.core
 */
package org.simpleframework.xml.filter;

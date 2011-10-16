/**
 * Provides various strategies that can be used to convert an object.
 * Converting objects using a <code>Converter</code> has a number of 
 * advantages over annotating the classes directly. One of the primary
 * advantages is that it is possible to use external objects, which 
 * can not be annotated in to your schema. Another advantage is that
 * you can convert an object that does not conform to one of the 
 * available annotations.
 * <p>
 * The primary class in this package is the <code>Converter</code>. 
 * It can be implemented to provide a means of converting an object
 * instance directly to XML and back again. For convenience there are
 * a number of ways a converter can be included in the strategy.
 * 
 * @see org.simpleframework.xml.strategy
 */
package org.simpleframework.xml.convert;

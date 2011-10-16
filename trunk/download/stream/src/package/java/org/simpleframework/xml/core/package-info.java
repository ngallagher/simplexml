/**
 * Provides the core persistence framework and associated annotations.
 * In this package the <code>Persister</code> object can be found. 
 * The persister implements the <code>Serializer</code> interface and
 * provides the main means of serializing and deserializing classes.
 * For example take the below snippet.
 * <pre>
 * 
 *    Serializer serializer = new Persister();
 *    InputStream source = new FileInputStream(file);
 *    
 *    Example example = serializer.read(Example.class, source);
 * 
 * </pre>
 * Each persister instance can be configured with various options. 
 * Such options include <code>Style</code> objects that allow you to
 * style and format the resulting XML and <code>Filter</code> objects
 * which allows string substitution during the deserialization of
 * the XML document. Also <code>Strategy</code> objects can be used
 * to configure the serialization process being performed. 
 * 
 * @see org.simpleframework.xml
 */
package org.simpleframework.xml.core;

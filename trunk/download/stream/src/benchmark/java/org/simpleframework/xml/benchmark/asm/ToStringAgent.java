package org.simpleframework.xml.benchmark.asm;


import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * Instrumentation agent to selectively add <code>toString()</code> methods to
 * classes.
 */
public class ToStringAgent implements ClassFileTransformer
{
    /* (non-Javadoc)
     * @see java.lang.instrument.ClassFileTransformer#transform(java.lang.ClassLoader, java.lang.String, java.lang.Class, java.security.ProtectionDomain, byte[])
     */
    public byte[] transform(ClassLoader loader, String cname, Class clas,
        ProtectionDomain domain, byte[] bytes)
        throws IllegalClassFormatException {
        System.out.println("Processing class " + cname);
        try {
            
            // scan class binary format to find fields for toString() method
            ClassReader creader = new ClassReader(bytes);
            FieldCollector visitor = new FieldCollector();
            creader.accept(visitor, ClassReader.SKIP_DEBUG); // creader.accept(visitor, true); skip debug
            FieldInfo[] fields = visitor.getFields();
            if (fields.length > 0) {
                
                // annotated fields present, generate the toString() method
                System.out.println("Modifying " + cname);
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                ToStringGenerator gen = new ToStringGenerator(writer,
                        cname.replace('.', '/'), fields);
                creader.accept(gen, 0); // creader.accept(gen, false); don't skip debug
                return writer.toByteArray();
                
            }
        } catch (IllegalStateException e) {
            throw new IllegalClassFormatException("Error: " + e.getMessage() +
                " on class " + cname);
        }
        return null;
    }
    
    // Required method for instrumentation agent.
    public static void premain(String arglist, Instrumentation inst) {
        inst.addTransformer(new ToStringAgent());
    }
}
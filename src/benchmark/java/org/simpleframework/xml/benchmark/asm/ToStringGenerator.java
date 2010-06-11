package org.simpleframework.xml.benchmark.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Visitor to add <code>toString</code> method to a class.
 */
public class ToStringGenerator extends ClassAdapter
{
    private final ClassWriter m_writer;
    private final String m_internalName;
    private final FieldInfo[] m_fields;
    
    public ToStringGenerator(ClassWriter cw, String iname, FieldInfo[] props) {
        super(cw);
        m_writer = cw;
        m_internalName = iname;
        m_fields = props;
    }
    
    // called at end of class
    public void visitEnd() {
        
        // set up to build the toString() method
        MethodVisitor mv = m_writer.visitMethod(Opcodes.ACC_PUBLIC,
            "toString", "()Ljava/lang/String;", null, null);
        mv.visitCode();
        
        // create and initialize StringBuffer instance
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuffer");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuffer",
            "<init>", "()V");
        
        // start text with class name
        String name = m_internalName;
        int split = name.lastIndexOf('/');
        if (split >= 0) {
            name = name.substring(split+1);
        }
        mv.visitLdcInsn(name + ":");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuffer",
            "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;");
        
        // loop through all field values to be included
        boolean newline = false;
        for (int i = 0; i < m_fields.length; i++) {
            
            // check type of field (objects other than Strings need conversion)
            FieldInfo prop = m_fields[i];
            Type type = prop.getType();
            boolean isobj = type.getSort() == Type.OBJECT &&
                !type.getClassName().equals("java.lang.String");
            
            // format lead text, with newline for object or after object
            String lead = (isobj || newline) ? "\n " : " ";
            if (prop.getText().length() > 0) {
                lead += prop.getText() + "=";
            }
            mv.visitLdcInsn(lead);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "java/lang/StringBuffer", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuffer;");
            
            // load the actual field value and append
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, m_internalName,
                prop.getField(), type.getDescriptor());
            if (isobj) {
                
                // convert objects by calling toString() method
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    type.getInternalName(), "toString",
                    "()Ljava/lang/String;");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "java/lang/StringBuffer", "append",
                    "(Ljava/lang/String;)Ljava/lang/StringBuffer;");
                
            } else {
                
                // append other types directly to StringBuffer
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "java/lang/StringBuffer", "append", "(" +
                    type.getDescriptor() + ")Ljava/lang/StringBuffer;");
                
            }
            newline = isobj;
        }
        
        // finish the method by returning accumulated text
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuffer",
            "toString", "()Ljava/lang/String;");
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(3, 1);
        mv.visitEnd();
        super.visitEnd();
    }
}
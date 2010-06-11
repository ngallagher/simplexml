package org.simpleframework.xml.benchmark.asm;


import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * Visitor implementation to collect field annotation information from class.
 */
public class FieldCollector extends EmptyVisitor
{
    private boolean m_isIncluded;
    private int m_fieldAccess;
    private String m_fieldName;
    private Type m_fieldType;
    private int m_fieldOrder;
    private String m_fieldText;
    private ArrayList m_fields = new ArrayList();
    
    // finish field handling, once we're past it
    private void finishField() {
        if (m_isIncluded) {
            m_fields.add(new FieldInfo(m_fieldName, m_fieldType,
                m_fieldOrder, m_fieldText));
        }
        m_isIncluded = false;
    }
    
    // return array of included field information
    public FieldInfo[] getFields() {
        finishField();
        FieldInfo[] infos =
            (FieldInfo[])m_fields.toArray(new FieldInfo[m_fields.size()]);
        Arrays.sort(infos);
        return infos;
    }
    
    // process field found in class
    public FieldVisitor visitField(int access, String name, String desc,
        String sig, Object init) {
        
        // finish processing of last field
        finishField();
        
        // save information for this field
        m_fieldAccess = access;
        m_fieldName = name;
        m_fieldType = Type.getReturnType(desc);
        m_fieldOrder = Integer.MAX_VALUE;
        
        // default text is empty if non-String object, otherwise from field name
        if (m_fieldType.getSort() == Type.OBJECT &&
            !m_fieldType.getClassName().equals("java.lang.String")) {
            m_fieldText = "";
        } else {
            String text = name;
            if (text.startsWith("m_") && text.length() > 2) {
                text = Character.toLowerCase(text.charAt(2)) +
                    text.substring(3);
            }
            m_fieldText = text;
        }
        return super.visitField(access, name, desc, sig, init);
    }
    
    // process annotation found in class
    public AnnotationVisitor visitAnnotation(String sig, boolean visible) {
        
        // flag field to be included in representation
        if (sig.equals("Lorg/simpleframework/xml/benchmark/asm/ToString;")) {
            if ((m_fieldAccess & Opcodes.ACC_STATIC) == 0) {
                m_isIncluded = true;
            } else {
                throw new IllegalStateException("ToString " +
                    "annotation is not supported for static field +" +
                    " m_fieldName");
            }
        }
        return super.visitAnnotation(sig, visible);
    }
    
    // process annotation name-value pair found in class
    public void visit(String name, Object value) {
        
        // ignore anything except the pair defined for toString() use
        if ("order".equals(name)) {
            m_fieldOrder = ((Integer)value).intValue();
        } else if ("text".equals(name)) {
            m_fieldText = value.toString();
        }
    }
}
package org.simpleframework.xml.benchmark.asm;

import org.objectweb.asm.Type;
/**
 * Information for field value to be included in string representation.
 */
public class FieldInfo implements Comparable
{
    private final String m_field;
    private final Type m_type;
    private final int m_order;
    private final String m_text;
    
    public FieldInfo(String field, Type type, int order,
        String text) {
        m_field = field;
        m_type = type;
        m_order = order;
        m_text = text;
    }
    public String getField() {
        return m_field;
    }
    public Type getType() {
        return m_type;
    }
    public int getOrder() {
        return m_order;
    }
    public String getText() {
        return m_text;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object comp) {
        if (comp instanceof FieldInfo) {
            return m_order - ((FieldInfo)comp).m_order;
        } else {
            throw new IllegalArgumentException("Wrong type for comparison");
        }
    }
}
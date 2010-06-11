package org.simpleframework.xml.benchmark.asm;
;

public class Name
{
    @ToString(order=1, text="") private String m_first;
    @ToString(order=2, text="") private String m_middle;
    @ToString(order=3, text="") private String m_last;
    
    public Name() {}
    public Name(String first, String middle, String last) {
        m_first = first;
        m_middle = middle;
        m_last = last;
    }
    public String getFirst() {
        return m_first;
    }
    public void setFirst(String first) {
        m_first = first;
    }
    public String getLast() {
        return m_last;
    }
    public void setLast(String last) {
        m_last = last;
    }
    public String getMiddle() {
        return m_middle;
    }
    public void setMiddle(String middle) {
        m_middle = middle;
    }
}
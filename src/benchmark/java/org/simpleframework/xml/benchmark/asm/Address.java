package org.simpleframework.xml.benchmark.asm;

public class Address
{
    @ToString private String m_street;
    @ToString private String m_city;
    @ToString private String m_state;
    @ToString private String m_zip;
    
    public Address() {}

    public Address(String street, String city, String state, String zip) {
        m_street = street;
        m_city = city;
        m_state = state;
        m_zip = zip;
    }
    public String getCity() {
        return m_city;
    }
    public void setCity(String city) {
        m_city = city;
    }
    public String getState() {
        return m_state;
    }
    public void setState(String state) {
        m_state = state;
    }
    public String getStreet() {
        return m_street;
    }
    public void setStreet(String street) {
        m_street = street;
    }
    public String getZip() {
        return m_zip;
    }
    public void setZip(String zip) {
        m_zip = zip;
    }
/*    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("Address: street=");
        buff.append(getStreet());
        buff.append(", city=");
        buff.append(getCity());
        buff.append(", state=");
        buff.append(getState());
        buff.append(", zip=");
        buff.append(getZip());
        return buff.toString();
    }   */
}
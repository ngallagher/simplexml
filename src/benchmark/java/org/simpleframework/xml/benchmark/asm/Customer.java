package org.simpleframework.xml.benchmark.asm;

public class Customer
{
    @ToString(order=1, text="#") private long m_number;
    @ToString() private String m_homePhone;
    @ToString() private String m_dayPhone;
    @ToString(order=2) private Name m_name;
    @ToString(order=3) private Address m_address;
    
    public Customer() {}
    public Customer(long number, Name name, Address address, String homeph,
        String dayph) {
        m_number = number;
        m_name = name;
        m_address = address;
        m_homePhone = homeph;
        m_dayPhone = dayph;
    }
    public Address getAddress() {
        return m_address;
    }
    public void setAddress(Address address) {
        m_address = address;
    }
    public String getDayPhone() {
        return m_dayPhone;
    }
    public void setDayPhone(String dayPhone) {
        m_dayPhone = dayPhone;
    }
    public String getHomePhone() {
        return m_homePhone;
    }
    public void setHomePhone(String homePhone) {
        m_homePhone = homePhone;
    }
    public Name getName() {
        return m_name;
    }
    public void setName(Name name) {
        m_name = name;
    }
    public long getNumber() {
        return m_number;
    }
    public void setNumber(long number) {
        m_number = number;
    }
/*    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("Customer #");
        buff.append(getNumber());
        buff.append(":\n ");
        buff.append(getName());
        buff.append("\n ");
        buff.append(getAddress());
        buff.append("\n dayPhone=");
        buff.append(getDayPhone());
        buff.append(", homePhone=");
        buff.append(getHomePhone());
        return buff.toString();
    }   */
}
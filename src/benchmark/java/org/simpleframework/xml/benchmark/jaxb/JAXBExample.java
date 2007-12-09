package org.simpleframework.xml.benchmark.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="example")
@XmlType(name="JAXBExample")
public class JAXBExample {
   
   @XmlAttribute(name="version")
   private int version;
   
   @XmlAttribute(name="name")
   private String name;
   
   @XmlElement(name="element1")
   private JAXBElementExample example1;
   
   @XmlElement(name="element2")
   private JAXBElementExample example2;
   
   @XmlElement(name="element3")
   private JAXBElementExample example3;
   
   private JAXBElementExample example4;
   
   @XmlElement(name="text")
   private String text;
   
   @XmlElement(name="element4")
   public void setElement(JAXBElementExample example) {
      this.example4 = example;
   }
   
   public JAXBElementExample getElement() {
      return example4;
   }
}

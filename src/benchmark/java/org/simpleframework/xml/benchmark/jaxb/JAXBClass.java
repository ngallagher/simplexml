package org.simpleframework.xml.benchmark.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="root")
@XmlType(name="JAXBClass")
public class JAXBClass {

   @XmlElement(name="example1")
   private JAXBExample example1;
   
   @XmlElement(name="example2")
   private JAXBExample example2;
   
   @XmlAttribute(name="id")
   private int id;
   
}

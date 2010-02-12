package org.simpleframework.xml.benchmark.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="element")
@XmlType(name="JAXBElementExample")
public class JAXBElementExample {

   @XmlAttribute(name="text")
   private String text;
   
   @XmlElement(name="integer")
   private int integerValue;
   
   @XmlElement(name="long")
   private long longValue;
   
   @XmlElement(name="boolean")
   private boolean booleanValue;
}

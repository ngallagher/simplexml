package compare.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="example")
@XmlType(name="JAXBExample")
public class JAXBExample {
   
   @XmlAttribute(name="version")
   private int version;
   
   @XmlAttribute(name="name")
   private String text;
}

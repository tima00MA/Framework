package ma.fs.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

    @XmlAccessorType(XmlAccessType.FIELD) // Use fields directly for JAXB mapping
    public class Bean {

        @XmlAttribute // Represents 'id' attribute in XML
        public String id;

        @XmlAttribute(name = "class") // Represents 'class' attribute in XML
        public String className;

        @XmlElement(name = "property") // Maps the 'property' element in XML
        public Property[] properties; // Array of Property objects for injected dependencies
    }


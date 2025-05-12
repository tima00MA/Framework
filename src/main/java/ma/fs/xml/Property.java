package ma.fs.xml;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Property {

    @XmlAttribute
    public String name;

    @XmlAttribute
    public String ref;
}
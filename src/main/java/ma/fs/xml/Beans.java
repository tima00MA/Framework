package ma.fs.xml;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class Beans {

    @XmlElement(name = "bean")
    public List<Bean> beans;
}
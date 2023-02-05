package hr.hsnopek.automatictester.core.model.step.httpmethods.elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class Header {

    @XmlElement(name = "Key")
    private String key;
    @XmlElement(name = "Value")
    private String originalValue;
    @XmlTransient
    private String value;

    public Header(){}

    public Header(String key, String originalValue) {
        this.key = key;
        this.originalValue = originalValue;
    }

    public Header(String key, String originalValue, String value) {
        this.key = key;
        this.originalValue = originalValue;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

package hr.hsnopek.automatictester.core.model.step.httpmethods.elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class Url {

    @XmlValue
    private String originalValue;
    @XmlTransient
    private String value;

    public Url(){}

    public Url(String originalValue) {
        this.originalValue = originalValue;
    }

    public Url(String originalValue, String value) {
        this.originalValue = originalValue;
        this.value = value;
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

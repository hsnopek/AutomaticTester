package hr.hsnopek.automatictester.core.model.configuration;

import hr.hsnopek.automatictester.core.model.security.SecurityRule;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {

    public Configuration(){}

    public Configuration(List<SecurityRule> securityRules) {
        this.securityRules = securityRules;
    }

    @XmlElementWrapper(name="SecurityRules")
    @XmlElement(name="SecurityRule")
    private List<SecurityRule> securityRules;

    public List<SecurityRule> getSecurityRules() {
        return securityRules;
    }

    public void setSecurityRules(List<SecurityRule> securityRules) {
        this.securityRules = securityRules;
    }
}

package hr.hsnopek.automatictester.core.model.security;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class SecurityRule {

    @XmlAttribute(name = "sslEnabled")
    private Boolean sslEnabled;
    @XmlAttribute(name = "keyStore")
    private String keyStore;
    @XmlAttribute(name = "keyStorePassword")
    private String keyStorePassword;
    @XmlAttribute(name = "trustStore")
    private String trustStore;
    @XmlAttribute(name = "trustStorePassword")
    private String trustStorePassword;
    @XmlAttribute(name = "domain")
    private String domain;
    @XmlAttribute(name = "port")
    private String port = "8443";

    public SecurityRule(){}

    public SecurityRule(Boolean sslEnabled, String keyStore, String keyStorePassword, String trustStore, String trustStorePassword, String domain, String port) {
        this.sslEnabled = sslEnabled;
        this.keyStore = keyStore;
        this.keyStorePassword = keyStorePassword;
        this.trustStore = trustStore;
        this.trustStorePassword = trustStorePassword;
        this.domain = domain;
        this.port = port;
    }

    public Boolean getSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(Boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}

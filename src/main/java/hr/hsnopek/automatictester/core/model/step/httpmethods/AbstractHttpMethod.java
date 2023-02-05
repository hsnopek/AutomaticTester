package hr.hsnopek.automatictester.core.model.step.httpmethods;

import hr.hsnopek.automatictester.client.HttpClient;
import hr.hsnopek.automatictester.client.interceptors.HeaderRequestInterceptor;
import hr.hsnopek.automatictester.core.model.configuration.Configuration;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Body;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Header;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Url;
import hr.hsnopek.automatictester.core.model.step.httpmethods.impl.HttpPost;
import hr.hsnopek.automatictester.util.CertUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.annotation.*;
import java.security.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@Slf4j
public abstract class AbstractHttpMethod {

    @XmlAttribute
    private String datasetName;
    @XmlAttribute
    private String scope;
    @XmlAttribute
    private Boolean logHttpResponse;
    @XmlAttribute
    private String iterator;
    @XmlAttribute
    private String iteratorJsonPathExpression;

    @XmlElement(name = "Url")
    private Url url;
    @XmlElementWrapper(name="Headers")
    @XmlElement(name="Header")
    private List<Header> headers;
    @XmlElement(name = "Body")
    private Body body;

    @XmlTransient
    private ResponseEntity<String> responseEntity;

    public AbstractHttpMethod(String datasetName, String scope, Url url, List<Header> headers, Body body) {
        this.datasetName = datasetName;
        this.scope = scope;
        this.url = url;
        this.headers = headers;
        this.body = body;
    }

    public AbstractHttpMethod() {
    }

    public RestTemplate getRestTemplate(Configuration configuration) {
        RestTemplate restTemplate = new RestTemplate();

        if(configuration == null)
            return restTemplate;

        // configure client per http request from global configuration
        configuration.getSecurityRules().forEach( rule -> {
            if(rule.getSslEnabled()){
                if(!getUrl().getOriginalValue().contains(rule.getDomain())){
                    throw new RuntimeException("Request URL doesn't match domain in security configuration.");
                }
                try {
                    KeyStore keyStore = CertUtil.loadKeystoreFromPath(rule.getKeyStore(), rule.getKeyStorePassword());
                    KeyStore trustStore = CertUtil.loadKeystoreFromPath(rule.getTrustStore(), rule.getTrustStorePassword());
                    CloseableHttpClient closeableHttpClient = new HttpClient().build(keyStore, rule.getKeyStorePassword(), trustStore, new HeaderRequestInterceptor(headers));
                    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(closeableHttpClient);
                    restTemplate.setRequestFactory(requestFactory);
                } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException |
                         KeyManagementException e) {
                    throw new RuntimeException("Error happened while trying to configure http client.", e);
                }
            }
        });
        return restTemplate;
    }

    public void validate(){
        if(StringUtils.isBlank(getDatasetName())){
            throw new RuntimeException("All http methods are required to have 'datasetName' property declared. Property can't be empty string.");
        }

        if(StringUtils.isBlank(getScope())){
            throw new RuntimeException("All http methods are required to have 'scope' property declared.");
        } else {
            if(!StringUtils.equalsIgnoreCase("global", getScope())
                    && !StringUtils.equalsIgnoreCase("local", getScope())
            ){
                throw new RuntimeException("Invalid value for 'scope' property. Allowed values are 'global' and 'local'.");
            }
        }

        if (getUrl() == null){
            throw new RuntimeException("All http methods are required to have <Url /> element.");
        } else {
            if(StringUtils.isBlank(getUrl().getOriginalValue())){
                throw new RuntimeException("Invalid URL element. URL elements cannot be blank.");
            }
        }


        // specific rules
        if (this instanceof HttpPost){
            if(this.getBody() == null){
                throw new RuntimeException("<HttpPost> element must have a body.");
            }
        }
    }

    public void logHttpResponse() {
        if(Boolean.TRUE.equals(this.logHttpResponse)) {
            log.info("*** Server response: {} {} ***", this.responseEntity.getStatusCodeValue(), this.responseEntity.getStatusCode().getReasonPhrase());
            log.info("*** Response headers ***");
            this.responseEntity.getHeaders().forEach((key, value) -> {
                log.info("Key=[{}], Value=[{}]", key, value);
            });
            log.info("*** Response body ***");
            log.info(this.responseEntity.getBody());
            log.info("********************* {}", System.lineSeparator());
        }
    }
}

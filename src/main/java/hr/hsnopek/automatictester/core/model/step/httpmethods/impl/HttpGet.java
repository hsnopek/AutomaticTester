package hr.hsnopek.automatictester.core.model.step.httpmethods.impl;

import hr.hsnopek.automatictester.core.model.configuration.Configuration;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Header;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Url;
import hr.hsnopek.automatictester.core.model.step.httpmethods.AbstractHttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class HttpGet extends AbstractHttpMethod {

    public HttpGet(){}
    public HttpGet(String datasetName, String scope, Url url, List<Header> headers) {
        super(datasetName, scope, url, headers, null);
    }
    public ResponseEntity<String> get(Configuration configuration) {
        return getRestTemplate(configuration).getForEntity(getUrl().getValue(), String.class);
    }
}

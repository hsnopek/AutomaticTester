package hr.hsnopek.automatictester.core.model.step.httpmethods.impl;

import hr.hsnopek.automatictester.core.model.configuration.Configuration;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Body;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Header;
import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Url;
import hr.hsnopek.automatictester.core.model.step.httpmethods.AbstractHttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
public class HttpPost extends AbstractHttpMethod {

    public HttpPost(){
        super();
    }

    public HttpPost(String dataset, String scope, Url url, List<Header> headers, Body body) {
        super(dataset, scope, url, headers, body);
    }

    public ResponseEntity<String> post(Configuration configuration){
        ResponseEntity<String> response = null;
        try{
            response = getRestTemplate(configuration).postForEntity(getUrl().getValue(), getBody().getOriginalValue(), String.class);
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return response;
    }

}

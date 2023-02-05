package hr.hsnopek.automatictester.client.interceptors;

import hr.hsnopek.automatictester.core.model.step.httpmethods.elements.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.List;

public class HeaderRequestInterceptor implements HttpRequestInterceptor {

    private final List<Header> headers;

    public HeaderRequestInterceptor(List<Header> headers) {
        this.headers = headers;
    }

    @Override
    public void process(org.apache.http.HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        for(Header header : headers){
            httpRequest.setHeader(header.getKey(), header.getOriginalValue());
        }
    }
}